package com.example.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Task<T>.awaitTask(): T = suspendCoroutine { continuation ->
    addOnSuccessListener { result -> continuation.resume(result) }
    addOnFailureListener { exception -> continuation.resumeWithException(exception) }
    addOnCanceledListener { continuation.resumeWithException(CancellationException("Task was cancelled")) }
}

data class AuthState(
    val isAuthenticated: Boolean = false,
    val user: FirebaseUser? = null,
    val displayName: String = "Guest Creator",
    val email: String? = null,
    val photoUrl: String? = null,
    val isFirebaseReady: Boolean = false,
    val errorMessage: String? = null
)

class FirebaseManager(private val context: Context) {

    private val tag = "PlutogramFirebase"

    val isFirebaseInitialized: Boolean
        get() = try {
            FirebaseApp.getApps(context).isNotEmpty()
        } catch (e: Exception) {
            false
        }

    val auth: FirebaseAuth?
        get() = if (isFirebaseInitialized) {
            try { FirebaseAuth.getInstance() } catch (e: Exception) { null }
        } else null

    val firestore: FirebaseFirestore?
        get() = if (isFirebaseInitialized) {
            try { FirebaseFirestore.getInstance() } catch (e: Exception) { null }
        } else null

    fun getCurrentAuthState(): AuthState {
        val currentAuth = auth
        val currentUser = currentAuth?.currentUser
        return if (currentUser != null) {
            AuthState(
                isAuthenticated = true,
                user = currentUser,
                displayName = currentUser.displayName ?: currentUser.email?.substringBefore("@") ?: "Creator",
                email = currentUser.email,
                photoUrl = currentUser.photoUrl?.toString(),
                isFirebaseReady = true
            )
        } else {
            AuthState(
                isAuthenticated = false,
                isFirebaseReady = isFirebaseInitialized
            )
        }
    }

    suspend fun signInWithGoogle(activity: Activity, webClientId: String = ""): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        val authInstance = auth
            ?: return@withContext Result.failure(IllegalStateException("Firebase is not initialized. Please ensure google-services.json is added."))

        try {
            val credentialManager = CredentialManager.create(activity)

            val googleIdOptionBuilder = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)

            if (webClientId.isNotBlank()) {
                googleIdOptionBuilder.setServerClientId(webClientId)
            }

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOptionBuilder.build())
                .build()

            val result = credentialManager.getCredential(activity, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val authCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = authInstance.signInWithCredential(authCredential).awaitTask()
                val user = authResult.user
                    ?: return@withContext Result.failure(Exception("Firebase user is null after sign in"))

                // Persist new user to Firestore
                syncUserToFirestore(user)

                Result.success(user)
            } else {
                Result.failure(Exception("Unsupported credential type: ${credential.type}"))
            }
        } catch (e: Exception) {
            Log.e(tag, "Google Sign-In failed", e)
            Result.failure(e)
        }
    }

    suspend fun signInWithEmail(email: String, pass: String): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        val authInstance = auth
            ?: return@withContext Result.failure(IllegalStateException("Firebase is not initialized."))
        try {
            val authResult = authInstance.signInWithEmailAndPassword(email, pass).awaitTask()
            val user = authResult.user ?: return@withContext Result.failure(Exception("User is null"))
            syncUserToFirestore(user)
            Result.success(user)
        } catch (e: Exception) {
            // Attempt to create account if doesn't exist
            try {
                val createResult = authInstance.createUserWithEmailAndPassword(email, pass).awaitTask()
                val user = createResult.user ?: return@withContext Result.failure(Exception("User is null"))
                syncUserToFirestore(user)
                Result.success(user)
            } catch (createErr: Exception) {
                Result.failure(createErr)
            }
        }
    }

    suspend fun signInAnonymously(): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        val authInstance = auth
            ?: return@withContext Result.failure(IllegalStateException("Firebase is not initialized."))
        try {
            val res = authInstance.signInAnonymously().awaitTask()
            val user = res.user ?: return@withContext Result.failure(Exception("Anonymous user is null"))
            syncUserToFirestore(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            Log.e(tag, "Error signing out", e)
        }
    }

    // --- Firestore Data Persistence ---
    private suspend fun syncUserToFirestore(user: FirebaseUser) = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext
        val userData = hashMapOf(
            "uid" to user.uid,
            "displayName" to (user.displayName ?: "Creator"),
            "email" to (user.email ?: ""),
            "photoUrl" to (user.photoUrl?.toString() ?: ""),
            "lastLogin" to System.currentTimeMillis()
        )
        try {
            db.collection("users").document(user.uid)
                .set(userData, SetOptions.merge())
                .awaitTask()
        } catch (e: Exception) {
            Log.e(tag, "Failed to sync user to Firestore", e)
        }
    }

    suspend fun saveCreatorProfile(profile: CreatorProfile) = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext
        val uid = auth?.currentUser?.uid ?: "local_user"
        val data = hashMapOf(
            "name" to profile.name,
            "handle" to profile.handle,
            "bio" to profile.bio,
            "totalEarnings" to profile.totalEarnings,
            "pendingPayout" to profile.pendingPayout,
            "stripeConnected" to profile.stripeConnected,
            "stripeAccountId" to profile.stripeAccountId,
            "razorpayConnected" to profile.razorpayConnected,
            "razorpayKeyId" to profile.razorpayKeyId,
            "upiConnected" to profile.upiConnected,
            "upiId" to profile.upiId,
            "updatedAt" to System.currentTimeMillis()
        )
        try {
            db.collection("creator_profiles").document(uid)
                .set(data, SetOptions.merge())
                .awaitTask()
        } catch (e: Exception) {
            Log.e(tag, "Failed to save creator profile to Firestore", e)
        }
    }

    suspend fun saveHireOrder(order: HireOrder) = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext
        val data = hashMapOf(
            "id" to order.id,
            "creatorName" to order.creatorName,
            "creatorHandle" to order.creatorHandle,
            "serviceTitle" to order.serviceTitle,
            "totalAmount" to order.totalAmount,
            "status" to order.status,
            "creationDate" to order.creationDate,
            "projectBrief" to order.projectBrief,
            "paymentMethod" to order.paymentMethod,
            "clientUid" to (auth?.currentUser?.uid ?: "guest")
        )
        try {
            db.collection("hire_orders").document(order.id)
                .set(data)
                .awaitTask()
        } catch (e: Exception) {
            Log.e(tag, "Failed to save hire order to Firestore", e)
        }
    }

    suspend fun saveChatMessage(conversationId: String, message: ChatMessage) = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext
        val data = hashMapOf(
            "id" to message.id,
            "senderName" to message.senderName,
            "isMe" to message.isMe,
            "text" to message.text,
            "timestamp" to message.timestamp,
            "isVoiceNote" to message.isVoiceNote,
            "voiceDuration" to message.voiceDuration
        )
        try {
            db.collection("conversations")
                .document(conversationId)
                .collection("messages")
                .document(message.id)
                .set(data)
                .awaitTask()
        } catch (e: Exception) {
            Log.e(tag, "Failed to save chat message to Firestore", e)
        }
    }
}

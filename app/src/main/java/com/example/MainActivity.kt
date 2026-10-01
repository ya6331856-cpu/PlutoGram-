package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BottomNavBar
import com.example.ui.components.CreateHubDialog
import com.example.ui.components.TopHeader
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TelegramDarkBg
import com.example.viewmodel.TelePulseViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TelePulseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TelePulseApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TelePulseApp(viewModel: TelePulseViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isCreateHubOpen by remember { mutableStateOf(false) }
    var isExploreOpen by remember { mutableStateOf(false) }
    var isAppMenuOpen by remember { mutableStateOf(false) }

    // BackHandler to handle custom screen transitions gracefully
    BackHandler(enabled = uiState.currentTab != 0 || isCreateHubOpen || isExploreOpen || isAppMenuOpen || uiState.isChatOpen || uiState.isCareersOpen || uiState.isCheckoutDialogOpen || uiState.isPaymentConfigOpen || uiState.isAuthDialogOpen || uiState.isTermsDialogOpen || uiState.isCallActive || uiState.isCameraDialogOpen) {
        when {
            isAppMenuOpen -> isAppMenuOpen = false
            isCreateHubOpen -> isCreateHubOpen = false
            isExploreOpen -> isExploreOpen = false
            uiState.isCallActive -> viewModel.endCall()
            uiState.isCameraDialogOpen -> viewModel.openCameraDialog(false)
            uiState.isTermsDialogOpen -> viewModel.openTermsDialog(false)
            uiState.isAuthDialogOpen -> viewModel.openAuthDialog(false)
            uiState.isChatOpen -> viewModel.toggleChat(false)
            uiState.isCareersOpen -> viewModel.toggleCareersPortal(false)
            uiState.isCheckoutDialogOpen -> viewModel.closeHireCheckout()
            uiState.isPaymentConfigOpen -> viewModel.openPaymentConfig(false)
            uiState.currentTab != 0 -> viewModel.selectTab(0)
        }
    }

    val screenTitle = when (uiState.currentTab) {
        0 -> "Plutogram"
        1 -> "Reels"
        2 -> "AI Video Studio"
        3 -> "Long Videos"
        4 -> "Creator Profile"
        else -> "Plutogram"
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(TelegramDarkBg)
            .testTag("plutogram_scaffold"),
        containerColor = TelegramDarkBg,
        topBar = {
            if (uiState.currentTab != 1) { // Reels screen has full-bleed immersive video overlay
                TopHeader(
                    title = screenTitle,
                    unreadMessagesCount = uiState.conversations.sumOf { it.unreadCount },
                    onOpenChat = { viewModel.toggleChat(true) },
                    onOpenExplore = { isExploreOpen = true },
                    onOpenMenu = { isAppMenuOpen = true }
                )
            }
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = uiState.currentTab,
                onTabSelected = { viewModel.selectTab(it) },
                onCenterPlusClick = { isCreateHubOpen = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = if (uiState.currentTab == 1) 0.dp else innerPadding.calculateTopPadding(),
                    bottom = 0.dp
                )
        ) {
            AnimatedContent(
                targetState = uiState.currentTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "tab_navigation"
            ) { tab ->
                when (tab) {
                    0 -> HomeScreen(uiState = uiState, viewModel = viewModel)
                    1 -> ReelsScreen(uiState = uiState, viewModel = viewModel)
                    2 -> VideoStudioScreen(uiState = uiState, viewModel = viewModel)
                    3 -> LongVideoScreen(uiState = uiState, viewModel = viewModel)
                    4 -> ProfileScreen(uiState = uiState, viewModel = viewModel)
                }
            }
        }
    }

    // Modal Overlays
    if (uiState.isChatOpen) {
        ChatScreen(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { viewModel.toggleChat(false) }
        )
    }

    if (uiState.isCareersOpen) {
        CareersPortalDialog(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { viewModel.toggleCareersPortal(false) }
        )
    }

    if (uiState.isCheckoutDialogOpen && uiState.selectedServiceForHire != null) {
        HireCheckoutDialog(
            service = uiState.selectedServiceForHire!!,
            onDismiss = { viewModel.closeHireCheckout() },
            onConfirm = { method, brief ->
                viewModel.confirmClientHire(method, brief)
            }
        )
    }

    if (uiState.isPaymentConfigOpen) {
        PaymentConfigDialog(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { viewModel.openPaymentConfig(false) }
        )
    }

    if (uiState.isAuthDialogOpen) {
        AuthDialog(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { viewModel.openAuthDialog(false) }
        )
    }

    if (uiState.isTermsDialogOpen) {
        TermsAndRulesDialog(
            isAccepted = uiState.hasAcceptedTerms,
            onAccept = { viewModel.acceptTerms() },
            onDismiss = { viewModel.openTermsDialog(false) }
        )
    }

    if (uiState.isCallActive) {
        CallDialog(
            callerName = uiState.activeCallPartner,
            callerAvatar = uiState.activeCallAvatar,
            isVideoCall = uiState.isCallVideo,
            onEndCall = { viewModel.endCall() }
        )
    }

    if (uiState.isCameraDialogOpen) {
        CameraRecordingDialog(
            onDismiss = { viewModel.openCameraDialog(false) },
            onPublishReel = { caption, uri -> viewModel.publishRecordedReel(caption, uri) },
            onPublishStory = { caption, url -> viewModel.publishRecordedStory(caption, url) }
        )
    }

    if (isCreateHubOpen) {
        CreateHubDialog(
            onDismiss = { isCreateHubOpen = false },
            onOpenRecordCamera = { viewModel.openCameraDialog(true) },
            onOpenAiStudio = { viewModel.selectTab(2) },
            onCreatePost = { viewModel.selectTab(0) }
        )
    }

    if (isExploreOpen) {
        ExploreModalDialog(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { isExploreOpen = false }
        )
    }

    if (isAppMenuOpen) {
        AppMenuScreen(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { isAppMenuOpen = false },
            onNavigateToProfile = {
                isAppMenuOpen = false
                viewModel.selectTab(4)
            },
            onOpenSearch = {
                isAppMenuOpen = false
                isExploreOpen = true
            }
        )
    }
}

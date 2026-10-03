package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun CreatorTipDialog(
    isOpen: Boolean,
    creatorName: String,
    creatorAvatar: String,
    contentTitle: String,
    onSendTip: (amount: Double, gateway: String, message: String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val coroutineScope = rememberCoroutineScope()
    var selectedAmount by remember { mutableStateOf(5.0) }
    var isCustomAmount by remember { mutableStateOf(false) }
    var customAmountText by remember { mutableStateOf("") }
    var selectedGateway by remember { mutableStateOf("UPI (GPay / PhonePe)") }
    var tipMessage by remember { mutableStateOf("Love your content! Keep creating! 🔥") }

    var isProcessing by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    val presetAmounts = listOf(
        Pair(2.0, "☕ $2"),
        Pair(5.0, "⭐ $5"),
        Pair(10.0, "🚀 $10"),
        Pair(25.0, "👑 $25")
    )

    val gateways = listOf(
        Pair("UPI (GPay / PhonePe)", "⚡ Instant UPI"),
        Pair("Stripe (Card)", "💳 Stripe Connect"),
        Pair("Telegram Stars", "⭐ Stars"),
        Pair("TON Wallet", "💎 TON Crypto")
    )

    Dialog(onDismissRequest = {
        if (!isProcessing) onDismiss()
    }) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("creator_tip_dialog"),
            borderColor = Color(0xFFFFD700)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = creatorAvatar,
                            contentDescription = creatorName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Color(0xFFFFD700), CircleShape)
                        )
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Tip $creatorName",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = TelegramCyanAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "Supporting: ${contentTitle.take(28)}...",
                                color = TelegramTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (!isProcessing && !isSuccess) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TelegramTextSecondary)
                        }
                    }
                }

                // Animation Content State
                AnimatedContent(
                    targetState = when {
                        isSuccess -> "SUCCESS"
                        isProcessing -> "PROCESSING"
                        else -> "INPUT"
                    },
                    label = "tip_state"
                ) { state ->
                    when (state) {
                        "PROCESSING" -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xFFFFD700),
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "Simulating transaction via $selectedGateway...",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Verifying cryptographic signature & updating creator balance...",
                                    color = TelegramTextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        "SUCCESS" -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(TelegramEmerald.copy(alpha = 0.2f))
                                        .border(2.dp, TelegramEmerald, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Success",
                                        tint = TelegramEmerald,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Text(
                                    text = "Tip Sent Successfully! 🎉",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                val effectiveAmount = if (isCustomAmount) customAmountText.toDoubleOrNull() ?: 5.0 else selectedAmount
                                Text(
                                    text = "$${String.format(Locale.US, "%.2f", effectiveAmount)} has been credited to $creatorName's creator dashboard.",
                                    color = TelegramEmerald,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        else -> {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                // Preset Amount Pills
                                Text("Select Tip Amount", color = TelegramTextSecondary, fontSize = 11.sp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    presetAmounts.forEach { (amt, label) ->
                                        val isSelected = !isCustomAmount && selectedAmount == amt
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(
                                                    if (isSelected) Color(0xFFFFD700).copy(alpha = 0.25f)
                                                    else Color(0x331C2938)
                                                )
                                                .border(
                                                    1.dp,
                                                    if (isSelected) Color(0xFFFFD700) else TelegramGlassBorderSubtle,
                                                    RoundedCornerShape(10.dp)
                                                )
                                                .clickable {
                                                    isCustomAmount = false
                                                    selectedAmount = amt
                                                }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                color = if (isSelected) Color(0xFFFFD700) else Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                // Custom Amount input option
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = customAmountText,
                                        onValueChange = {
                                            customAmountText = it.filter { c -> c.isDigit() || c == '.' }
                                            if (customAmountText.isNotEmpty()) {
                                                isCustomAmount = true
                                            }
                                        },
                                        placeholder = { Text("Custom Amount ($)", fontSize = 11.sp, color = TelegramTextSecondary) },
                                        leadingIcon = { Text("$", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFFFFD700),
                                            unfocusedBorderColor = TelegramGlassBorderSubtle,
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = TelegramTextPrimary
                                        )
                                    )
                                }

                                // Payment Method Picker
                                Text("Payment Gateway", color = TelegramTextSecondary, fontSize = 11.sp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    gateways.forEach { (gwKey, gwLabel) ->
                                        val isSel = selectedGateway == gwKey
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSel) TelegramBlue.copy(alpha = 0.4f) else Color(0x221C2938))
                                                .border(1.dp, if (isSel) TelegramCyanAccent else TelegramGlassBorderSubtle, RoundedCornerShape(8.dp))
                                                .clickable { selectedGateway = gwKey }
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = gwLabel,
                                                color = if (isSel) Color.White else TelegramTextSecondary,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                // Cheer Message
                                Text("Supporter Note (Optional)", color = TelegramTextSecondary, fontSize = 11.sp)
                                OutlinedTextField(
                                    value = tipMessage,
                                    onValueChange = { tipMessage = it },
                                    placeholder = { Text("Write a message...", fontSize = 11.sp, color = TelegramTextSecondary) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = TelegramCyanAccent,
                                        unfocusedBorderColor = TelegramGlassBorderSubtle,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = TelegramTextPrimary
                                    )
                                )

                                val finalAmount = if (isCustomAmount) customAmountText.toDoubleOrNull() ?: 5.0 else selectedAmount

                                Button(
                                    onClick = {
                                        isProcessing = true
                                        coroutineScope.launch {
                                            delay(1200) // realistic simulated transaction delay
                                            isProcessing = false
                                            isSuccess = true
                                            onSendTip(finalAmount, selectedGateway, tipMessage)
                                            delay(1400)
                                            onDismiss()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .testTag("confirm_send_tip_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFFD700)
                                    )
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MonetizationOn,
                                            contentDescription = null,
                                            tint = TelegramDarkBg,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "Send Tip $${String.format(Locale.US, "%.2f", finalAmount)}",
                                            color = TelegramDarkBg,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

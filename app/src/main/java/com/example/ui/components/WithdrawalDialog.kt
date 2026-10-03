package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.PayoutGateway
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun WithdrawalDialog(
    isOpen: Boolean,
    availableBalance: Double,
    onRequestWithdrawal: (Double, PayoutGateway, String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    var selectedGateway by remember { mutableStateOf(PayoutGateway.UPI_INSTANT) }
    var withdrawalAmountText by remember { mutableStateOf(String.format(Locale.US, "%.2f", availableBalance)) }
    var destinationInput by remember { mutableStateOf("alexvance@okaxis") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xBB080E17))
                .clickable { onDismiss() }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            FrostedGlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 440.dp)
                    .clickable(enabled = false) { }
                    .testTag("withdrawal_dialog"),
                borderColor = TelegramEmerald
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(TelegramEmerald.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = TelegramEmerald,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Withdraw Creator Earnings",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Available: $${String.format(Locale.US, "%.2f", availableBalance)}",
                                    color = TelegramEmerald,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        IconButton(onClick = onDismiss, modifier = Modifier.size(30.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TelegramTextSecondary)
                        }
                    }

                    // Gateway Selection
                    Text(
                        text = "Select Payout Method:",
                        color = TelegramTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PayoutGateway.entries.forEach { gateway ->
                            val isSelected = gateway == selectedGateway
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) TelegramBlueBright.copy(alpha = 0.2f) else Color(0x221C2938))
                                    .border(1.dp, if (isSelected) TelegramEmerald else TelegramGlassBorderSubtle, RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedGateway = gateway
                                        destinationInput = when (gateway) {
                                            PayoutGateway.UPI_INSTANT -> "alexvance@okaxis"
                                            PayoutGateway.BANK_TRANSFER -> "HDFC000124 • A/C ...9842"
                                            PayoutGateway.STRIPE_CONNECT -> "acct_1NZ4209xStripe"
                                            PayoutGateway.TONCOIN_WALLET -> "EQB8xQ...TONWallet"
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(gateway.iconEmoji, fontSize = 18.sp)
                                    Column {
                                        Text(gateway.title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                        Text(gateway.subtitle, color = TelegramTextSecondary, fontSize = 10.sp)
                                    }
                                }

                                Text(gateway.processingTime, color = TelegramEmerald, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    // Amount input & Quick percentage buttons
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Withdrawal Amount ($):", color = TelegramTextSecondary, fontSize = 12.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(25, 50, 100).forEach { pct ->
                                    Text(
                                        text = "$pct%",
                                        color = TelegramCyanAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0x331C2938))
                                            .clickable {
                                                val amt = availableBalance * (pct / 100.0)
                                                withdrawalAmountText = String.format(Locale.US, "%.2f", amt)
                                            }
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = withdrawalAmountText,
                            onValueChange = { withdrawalAmountText = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TelegramEmerald,
                                unfocusedBorderColor = TelegramGlassBorderSubtle,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = TelegramTextPrimary,
                                focusedContainerColor = Color(0x221C2938),
                                unfocusedContainerColor = Color(0x221C2938)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }

                    // Destination ID
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Destination Details / Account:", color = TelegramTextSecondary, fontSize = 12.sp)
                        OutlinedTextField(
                            value = destinationInput,
                            onValueChange = { destinationInput = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TelegramEmerald,
                                unfocusedBorderColor = TelegramGlassBorderSubtle,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = TelegramTextPrimary,
                                focusedContainerColor = Color(0x221C2938),
                                unfocusedContainerColor = Color(0x221C2938)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }

                    // Confirm Action Button
                    Button(
                        onClick = {
                            val amount = withdrawalAmountText.toDoubleOrNull() ?: 0.0
                            onRequestWithdrawal(amount, selectedGateway, destinationInput)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("confirm_withdrawal_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TelegramEmerald)
                    ) {
                        Text(
                            text = "Confirm & Transfer Funds",
                            color = TelegramDarkBg,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

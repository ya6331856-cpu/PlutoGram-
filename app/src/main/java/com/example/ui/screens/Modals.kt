package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.FreelanceService
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel

@Composable
fun HireCheckoutDialog(
    service: FreelanceService,
    onDismiss: () -> Unit,
    onConfirm: (paymentMethod: String, brief: String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("Stripe") }
    var projectBrief by remember { mutableStateOf("") }
    val deposit = service.price * 0.50
    val finalMilestone = service.price * 0.50

    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("hire_checkout_dialog"),
            borderColor = TelegramBlueBright
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hire Creator & Escrow",
                            color = TelegramTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Milestone Protected Checkout",
                            color = TelegramCyanAccent,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TelegramTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Service summary box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33121E2C))
                        .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = service.title,
                            color = TelegramTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Delivery: ${service.turnaround} • Total: $${service.price.toInt()}",
                            color = TelegramTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Escrow Milestone Schedule
                Text(
                    text = "Milestone Escrow Schedule:",
                    color = TelegramTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    MilestoneScheduleRow(
                        title = "1. Upfront Deposit (Escrow Vault)",
                        amount = "$${deposit.toInt()}",
                        note = "Held securely until first cut approved"
                    )
                    MilestoneScheduleRow(
                        title = "2. Final 4K Delivery & Approval",
                        amount = "$${finalMilestone.toInt()}",
                        note = "Released only upon client signoff"
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Payment Method Selector (Stripe, Razorpay, UPI)
                Text(
                    text = "Select Direct Payment Gateway:",
                    color = TelegramTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Stripe", "Razorpay", "UPI").forEach { method ->
                        val isSelected = selectedMethod == method
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) TelegramBlue.copy(alpha = 0.3f) else Color(0x33172534)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) TelegramBlue else TelegramGlassBorderSubtle,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedMethod = method }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = method,
                                color = if (isSelected) TelegramTextPrimary else TelegramTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Project Brief Field
                OutlinedTextField(
                    value = projectBrief,
                    onValueChange = { projectBrief = it },
                    placeholder = {
                        Text(
                            text = "Link video or instructions for hook editing...",
                            color = TelegramTextMuted,
                            fontSize = 11.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TelegramBlueBright,
                        unfocusedBorderColor = TelegramGlassBorder,
                        focusedContainerColor = Color(0x22111B27),
                        unfocusedContainerColor = Color(0x22111B27),
                        focusedTextColor = TelegramTextPrimary,
                        unfocusedTextColor = TelegramTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Confirm Escrow Deposit Button
                Button(
                    onClick = {
                        onConfirm(selectedMethod, projectBrief)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("confirm_escrow_deposit_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Authorize Escrow Deposit ($${deposit.toInt()})",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MilestoneScheduleRow(title: String, amount: String, note: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x221C2B3C))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TelegramTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = note,
                color = TelegramTextMuted,
                fontSize = 9.sp
            )
        }
        Text(
            text = amount,
            color = TelegramCyanAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PaymentConfigDialog(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    onDismiss: () -> Unit
) {
    var stripeAccount by remember { mutableStateOf(uiState.profile.stripeAccountId) }
    var razorpayKey by remember { mutableStateOf(uiState.profile.razorpayKeyId) }
    var upiId by remember { mutableStateOf(uiState.profile.upiId) }

    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("payment_config_dialog"),
            borderColor = TelegramCyanAccent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Link Payment Gateway",
                        color = TelegramTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TelegramTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stripe
                Text(text = "Stripe Connect Account ID:", color = TelegramTextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = stripeAccount,
                    onValueChange = { stripeAccount = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Razorpay
                Text(text = "Razorpay Key ID / Account:", color = TelegramTextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = razorpayKey,
                    onValueChange = { razorpayKey = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // UPI ID
                Text(text = "UPI ID (Instant VPA):", color = TelegramTextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.updatePaymentGateway("Stripe", stripeAccount)
                        viewModel.updatePaymentGateway("Razorpay", razorpayKey)
                        viewModel.updatePaymentGateway("UPI", upiId)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramEmerald),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_gateway_config_button")
                ) {
                    Text(
                        text = "Save & Verify Gateways",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

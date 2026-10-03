package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PayoutRecord
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun PayoutHistoryView(
    availableBalance: Double,
    payoutHistory: List<PayoutRecord>,
    onOpenWithdrawalModal: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedRecordForReceipt by remember { mutableStateOf<PayoutRecord?>(null) }

    val filterOptions = listOf("All", "Completed", "Processing", "Fan Tips")

    val filteredRecords = remember(selectedFilter, payoutHistory) {
        when (selectedFilter) {
            "Completed" -> payoutHistory.filter { it.status.contains("Completed", ignoreCase = true) }
            "Processing" -> payoutHistory.filter { it.status.contains("Processing", ignoreCase = true) }
            "Fan Tips" -> payoutHistory.filter { it.status.contains("Tip", ignoreCase = true) }
            else -> payoutHistory
        }
    }

    val totalCompletedPayouts = remember(payoutHistory) {
        payoutHistory
            .filter { it.status.contains("Completed", ignoreCase = true) }
            .sumOf { it.amount }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("payout_history_view"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Summary Cards Row ---
        item {
            FrostedGlassBox(
                modifier = Modifier.fillMaxWidth(),
                borderColor = TelegramEmerald
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Available for Withdrawal", color = TelegramTextSecondary, fontSize = 11.sp)
                            Text(
                                text = "$${String.format(Locale.US, "%,.2f", availableBalance)}",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Button(
                            onClick = onOpenWithdrawalModal,
                            colors = ButtonDefaults.buttonColors(containerColor = TelegramEmerald),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = TelegramDarkBg, modifier = Modifier.size(16.dp))
                                Text("Withdraw", color = TelegramDarkBg, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    HorizontalDivider(color = TelegramGlassBorderSubtle)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Paid Out", color = TelegramTextSecondary, fontSize = 10.sp)
                            Text(
                                text = "$${String.format(Locale.US, "%,.2f", totalCompletedPayouts)}",
                                color = TelegramCyanAccent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Column {
                            Text("Platform Payout Fee", color = TelegramTextSecondary, fontSize = 10.sp)
                            Text(
                                text = "0% (Partner Tier)",
                                color = TelegramEmerald,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column {
                            Text("Status", color = TelegramTextSecondary, fontSize = 10.sp)
                            Text(
                                text = "Instant Auto-Clear",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // --- 2. Filter Pills ---
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) TelegramBlue.copy(alpha = 0.4f) else Color(0x331C2938))
                            .border(1.dp, if (isSelected) TelegramCyanAccent else TelegramGlassBorderSubtle, RoundedCornerShape(8.dp))
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.White else TelegramTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // --- 3. Transactions Section Header ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaction Ledger (${filteredRecords.size})",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tap item for receipt",
                    color = TelegramTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        // --- 4. Transaction Items List ---
        if (filteredRecords.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No payout transactions in this filter.",
                        color = TelegramTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            items(filteredRecords, key = { it.id }) { record ->
                PayoutTransactionRow(
                    record = record,
                    onClick = { selectedRecordForReceipt = record }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }

    // Receipt Dialog
    selectedRecordForReceipt?.let { record ->
        AlertDialog(
            onDismissRequest = { selectedRecordForReceipt = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = TelegramCyanAccent)
                    Text("Payment Receipt", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ReceiptDetailRow("Transaction ID", record.id)
                    ReceiptDetailRow("Date & Time", record.date)
                    ReceiptDetailRow("Method / Gateway", record.method)
                    ReceiptDetailRow("Amount", "$${String.format(Locale.US, "%.2f", record.amount)}")
                    ReceiptDetailRow("Platform Fee (0%)", "$0.00")
                    ReceiptDetailRow("Net Transferred", "$${String.format(Locale.US, "%.2f", record.amount)}")
                    ReceiptDetailRow("Status", record.status)
                    ReceiptDetailRow("Security Verification", "SHA256 Encrypted ✅")
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedRecordForReceipt = null },
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlueBright)
                ) {
                    Text("Close Receipt", color = TelegramDarkBg, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xF00D1622)
        )
    }
}

@Composable
private fun PayoutTransactionRow(
    record: PayoutRecord,
    onClick: () -> Unit
) {
    val isTip = record.status.contains("Tip", ignoreCase = true)
    val isCompleted = record.status.contains("Completed", ignoreCase = true) || isTip

    FrostedGlassBox(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        borderColor = if (isTip) Color(0xFFFFD700).copy(alpha = 0.35f) else TelegramGlassBorderSubtle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                        .background(
                            if (isTip) Color(0xFFFFD700).copy(alpha = 0.2f)
                            else if (isCompleted) TelegramEmerald.copy(alpha = 0.2f)
                            else TelegramAmber.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isTip) Icons.Default.Favorite else if (isCompleted) Icons.Default.CheckCircle else Icons.Outlined.HourglassTop,
                        contentDescription = null,
                        tint = if (isTip) Color(0xFFFFD700) else if (isCompleted) TelegramEmerald else TelegramAmber,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = record.method,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${record.date} • ${record.id}",
                        color = TelegramTextSecondary,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (isTip) "+" else "-"}$${String.format(Locale.US, "%,.2f", record.amount)}",
                    color = if (isTip) TelegramEmerald else Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = record.status,
                    color = if (isCompleted) TelegramEmerald else TelegramAmber,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ReceiptDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TelegramTextSecondary, fontSize = 11.sp)
        Text(value, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

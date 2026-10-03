package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.HireOrder
import com.example.model.Milestone
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun CreatorMindsetDialog(
    isOpen: Boolean,
    minutesConsumed: Int,
    minutesCreated: Int,
    availableBalance: Double,
    escrowOrders: List<HireOrder>,
    onReleaseMilestone: (orderId: String, milestoneTitle: String) -> Unit,
    onLaunchHookStudio: () -> Unit,
    onOpenMonetization: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .testTag("creator_mindset_dialog"),
            borderColor = TelegramCyanAccent
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
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
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(TelegramBlueBright, TelegramCyanAccent)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Creator Mindset & Value Hub",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "From Passive Scroller to Digital Asset Owner",
                                    color = TelegramCyanAccent,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TelegramTextSecondary
                            )
                        }
                    }
                }

                // --- 1. Productivity vs. Passive Consumption Gauge ---
                item {
                    FrostedGlassBox(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = TelegramEmerald
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Daily Time Economics",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Positive ROI ⚡",
                                    color = TelegramEmerald,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Progress Bar: Consumption vs Creation
                            val totalMinutes = (minutesConsumed + minutesCreated).coerceAtLeast(1)
                            val createdRatio = minutesCreated.toFloat() / totalMinutes

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Created: ${minutesCreated}m (Monetizing)", color = TelegramEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("Consumed: ${minutesConsumed}m", color = TelegramTextSecondary, fontSize = 10.sp)
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(createdRatio)
                                            .fillMaxHeight()
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(TelegramEmerald, Color(0xFF00E5FF))
                                                )
                                            )
                                    )
                                    Box(
                                        modifier = Modifier
                                            .weight(1f - createdRatio)
                                            .fillMaxHeight()
                                            .background(Color(0x44263548))
                                    )
                                }
                            }

                            Text(
                                text = "💡 Mindset Rule: Every 15 minutes spent scrolling is attention donated to other creators. Spending that 15 minutes generating a viral hook in AI Hook Studio creates lifetime royalties.",
                                color = TelegramTextSecondary,
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }

                // --- 2. Live Cloud Backend Integration Status ---
                item {
                    FrostedGlassBox(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = TelegramBlueBright
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Cloud Escrow & Profile Architecture",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                FirestoreBadge("creator_profiles", "Sync: LIVE ✅")
                                FirestoreBadge("hire_orders", "Escrow: ACTIVE 🔒")
                            }
                        }
                    }
                }

                // --- 3. Active Milestone Escrow Contracts ($ Locked) ---
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Milestone Escrow Deals (${escrowOrders.size})",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val totalLocked = escrowOrders.sumOf { it.totalAmount }
                        Text(
                            text = "$${String.format(Locale.US, "%,.2f", totalLocked)} Locked",
                            color = TelegramEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                items(escrowOrders) { order ->
                    EscrowOrderCard(
                        order = order,
                        onReleaseMilestone = { milestoneTitle ->
                            onReleaseMilestone(order.id, milestoneTitle)
                        }
                    )
                }

                // --- 4. The 4 Creator Economy Value Pillars ---
                item {
                    Text(
                        text = "The 4 Pillars of Creator Independence",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        MindsetPillarItem(
                            icon = Icons.Default.AutoAwesome,
                            title = "1. AI Hook Leverage",
                            description = "Use gemini-3.5-flash to extract high-tension 0-3s openings. Never start from a blank timeline.",
                            tint = TelegramCyanAccent
                        )
                        MindsetPillarItem(
                            icon = Icons.Default.Security,
                            title = "2. Milestone Escrow Deals",
                            description = "Never work on trust. Client funds are 100% deposited into smart escrow before delivery.",
                            tint = TelegramEmerald
                        )
                        MindsetPillarItem(
                            icon = Icons.Default.MonetizationOn,
                            title = "3. 70% Ad Share & Stars",
                            description = "Earn $4.85+ RPM on long video ads and direct micropayments from fans.",
                            tint = Color(0xFFFFD700)
                        )
                        MindsetPillarItem(
                            icon = Icons.Default.AccountBalanceWallet,
                            title = "4. Instant Direct Payouts",
                            description = "Withdraw anytime via UPI, Bank Wire, Stripe Connect, or decentralized TON wallet.",
                            tint = TelegramBlueBright
                        )
                    }
                }

                // Actions Footer
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                onDismiss()
                                onLaunchHookStudio()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TelegramBlueBright),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Launch AI Studio ⚡", color = TelegramDarkBg, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                onDismiss()
                                onOpenMonetization()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TelegramEmerald),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Withdraw ($${String.format(Locale.US, "%.0f", availableBalance)})", color = TelegramDarkBg, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FirestoreBadge(
    collection: String,
    status: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0x331C2938))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "/$collection",
            color = TelegramTextSecondary,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = status,
            color = TelegramEmerald,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EscrowOrderCard(
    order: HireOrder,
    onReleaseMilestone: (String) -> Unit
) {
    FrostedGlassBox(
        modifier = Modifier.fillMaxWidth(),
        borderColor = TelegramGlassBorderSubtle
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.serviceTitle,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Payment: ${order.paymentMethod} • ${order.status}",
                        color = TelegramCyanAccent,
                        fontSize = 10.sp
                    )
                }

                Text(
                    text = "$${String.format(Locale.US, "%,.2f", order.totalAmount)}",
                    color = TelegramEmerald,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Milestones list
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                order.milestones.forEach { m ->
                    val milestoneVal = (order.totalAmount * m.percentage) / 100.0
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x221C2938))
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (m.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.HourglassTop,
                                contentDescription = null,
                                tint = if (m.isCompleted) TelegramEmerald else TelegramAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = m.title,
                                color = if (m.isCompleted) TelegramTextSecondary else Color.White,
                                fontSize = 10.sp
                            )
                        }

                        if (!m.isCompleted) {
                            TextButton(
                                onClick = { onReleaseMilestone(m.title) },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                modifier = Modifier.height(26.dp)
                            ) {
                                Text(
                                    text = "Release +$${milestoneVal.toInt()} ⚡",
                                    color = TelegramEmerald,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Text(
                                text = "Released ✅",
                                color = TelegramEmerald,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MindsetPillarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    tint: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x221C2938))
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color = TelegramTextSecondary,
                fontSize = 9.sp,
                lineHeight = 13.sp
            )
        }
    }
}

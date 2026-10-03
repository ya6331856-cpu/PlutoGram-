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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CreatorMonetizationState
import com.example.model.VideoMonetizationItem
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun CreatorMonetizationHub(
    monetization: CreatorMonetizationState,
    onToggleVideoMonetization: (String) -> Unit,
    onToggleGlobalMonetization: (Boolean) -> Unit,
    onOpenWithdrawal: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Available Balance & Earnings Hero Card ---
        item {
            FrostedGlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("monetization_hero_card"),
                borderColor = TelegramEmerald
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(TelegramEmerald.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = TelegramEmerald,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Creator Revenue Hub",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = monetization.partnerTier,
                                    color = TelegramEmerald,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Partner verified pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(TelegramEmerald.copy(alpha = 0.15f))
                                .border(1.dp, TelegramEmerald.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "70% Split Active",
                                color = TelegramEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Balance Display
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Available for Instant Withdrawal",
                            color = TelegramTextSecondary,
                            fontSize = 11.sp
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "$${String.format(Locale.US, "%,.2f", monetization.availableBalance)}",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "USD",
                                color = TelegramEmerald,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }

                    // Action: Withdraw Button
                    Button(
                        onClick = onOpenWithdrawal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("withdraw_funds_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TelegramEmerald)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalance,
                                contentDescription = null,
                                tint = TelegramDarkBg,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Withdraw Funds (UPI / Bank / Wallet)",
                                color = TelegramDarkBg,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Secondary Metrics Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x331C2938))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricItem(label = "Lifetime Gross", value = "$${String.format(Locale.US, "%,.0f", monetization.totalLifetimeEarnings)}")
                        MetricItem(label = "Avg RPM", value = "$${String.format(Locale.US, "%.2f", monetization.averageRpm)}")
                        MetricItem(label = "Monetized Views", value = "1.84M")
                        MetricItem(label = "Stars Received", value = "48.5K ⭐")
                    }
                }
            }
        }

        // --- 2. The 4 Revenue Pillars Breakdown ---
        item {
            FrostedGlassBox(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Monthly Revenue Channels",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    PillarRow(
                        title = "Ad Revenue Share (70% Split)",
                        subtitle = "Pre-roll & mid-roll ads on 16:9 videos and Reels",
                        amount = monetization.revenueBreakdown.adRevenue,
                        icon = "🎬",
                        color = TelegramBlueBright
                    )

                    PillarRow(
                        title = "Direct Super Thanks & Stars",
                        subtitle = "Instant micro-tips from viewers on viral hooks",
                        amount = monetization.revenueBreakdown.superThanksAndStars,
                        icon = "⭐",
                        color = Color(0xFFFFD700)
                    )

                    PillarRow(
                        title = "Channel VIP Memberships",
                        subtitle = "${monetization.activeMembershipsCount} subscribers @ $4.99/mo",
                        amount = monetization.revenueBreakdown.channelMemberships,
                        icon = "💎",
                        color = TelegramCyanAccent
                    )

                    PillarRow(
                        title = "Smart Escrow Brand Collabs",
                        subtitle = "Sponsored hook placements & custom edits",
                        amount = monetization.revenueBreakdown.brandSponsorships,
                        icon = "💼",
                        color = TelegramEmerald
                    )
                }
            }
        }

        // --- 3. Partner Program Eligibility Tracker ---
        item {
            FrostedGlassBox(
                modifier = Modifier.fillMaxWidth(),
                borderColor = TelegramBlueBright
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Partner Program Status",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = TelegramEmerald, modifier = Modifier.size(14.dp))
                            Text("Active Partner", color = TelegramEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    EligibilityProgressRow(
                        label = "Subscribers / Followers",
                        current = "463.5K",
                        required = "1,000 required",
                        progress = 1.0f
                    )

                    EligibilityProgressRow(
                        label = "Public Watch Hours (12 Months)",
                        current = "48,200 hrs",
                        required = "4,000 hrs required",
                        progress = 1.0f
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Community Guidelines Strikes", color = TelegramTextSecondary, fontSize = 11.sp)
                        Text("0 Active Strikes (Clean)", color = TelegramEmerald, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // --- 4. Per-Video Monetization List (YouTube Studio Style) ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Video Monetization Management",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${monetization.videoEarningsList.size} Videos Tracked",
                    color = TelegramTextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        items(monetization.videoEarningsList) { item ->
            VideoMonetizationCard(
                item = item,
                onToggleMonetization = { onToggleVideoMonetization(item.videoId) }
            )
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(label, color = TelegramTextSecondary, fontSize = 9.sp)
    }
}

@Composable
private fun PillarRow(
    title: String,
    subtitle: String,
    amount: Double,
    icon: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x221C2938))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(icon, fontSize = 18.sp)
            Column {
                Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = TelegramTextSecondary, fontSize = 10.sp)
            }
        }

        Text(
            text = "$${String.format(Locale.US, "%.2f", amount)}",
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun EligibilityProgressRow(
    label: String,
    current: String,
    required: String,
    progress: Float
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = TelegramTextPrimary, fontSize = 11.sp)
            Text("$current ($required)", color = TelegramCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = TelegramEmerald,
            trackColor = Color(0x3300E676)
        )
    }
}

@Composable
private fun VideoMonetizationCard(
    item: VideoMonetizationItem,
    onToggleMonetization: () -> Unit
) {
    FrostedGlassBox(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (item.isMonetized) TelegramEmerald.copy(alpha = 0.4f) else TelegramGlassBorderSubtle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.views, color = TelegramTextSecondary, fontSize = 10.sp)
                    Text("RPM: $${String.format(Locale.US, "%.2f", item.rpm)}", color = TelegramCyanAccent, fontSize = 10.sp)
                    Text("${item.superThanksCount} ⭐ tips", color = Color(0xFFFFD700), fontSize = 10.sp)
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$${String.format(Locale.US, "%.2f", item.earnings)}",
                    color = if (item.isMonetized) TelegramEmerald else TelegramTextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (item.isMonetized) "Ads ON" else "Ads OFF",
                        color = if (item.isMonetized) TelegramEmerald else TelegramTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = item.isMonetized,
                        onCheckedChange = { onToggleMonetization() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = TelegramEmerald
                        ),
                        modifier = Modifier.scale(0.75f)
                    )
                }
            }
        }
    }
}

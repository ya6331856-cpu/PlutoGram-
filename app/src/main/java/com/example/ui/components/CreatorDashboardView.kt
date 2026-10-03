package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.*
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun CreatorDashboardView(
    dashboardData: CreatorDashboardData,
    profile: CreatorProfile,
    monetization: CreatorMonetizationState,
    activeSubTab: Int = 0,
    onSelectSubTab: (Int) -> Unit = {},
    selectedTimeRange: String = "28D",
    onSelectTimeRange: (String) -> Unit = {},
    onUploadClick: () -> Unit,
    onLaunchHookStudio: () -> Unit,
    onOpenMonetization: () -> Unit,
    onHeartComment: (String) -> Unit,
    onReplyComment: (String, String) -> Unit,
    onOpenMindsetHub: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var replyDialogComment by remember { mutableStateOf<CreatorPriorityComment?>(null) }
    var replyInputText by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        // Sub-Tab Segment Switcher: [ 📊 Overview ] [ 📈 Analytics ] [ 💰 Payouts ]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x331C2938))
                .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val tabs = listOf(
                Pair(0, "📊 Overview"),
                Pair(1, "📈 Analytics"),
                Pair(2, "💰 Payouts")
            )
            tabs.forEach { (index, title) ->
                val isSelected = activeSubTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) TelegramBlue.copy(alpha = 0.45f) else Color.Transparent)
                        .clickable { onSelectSubTab(index) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else TelegramTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        when (activeSubTab) {
            1 -> {
                CreatorAnalyticsView(
                    dashboardData = dashboardData,
                    monetization = monetization,
                    selectedTimeRange = selectedTimeRange,
                    onSelectTimeRange = onSelectTimeRange
                )
            }
            2 -> {
                PayoutHistoryView(
                    availableBalance = monetization.availableBalance,
                    payoutHistory = monetization.payoutHistory,
                    onOpenWithdrawalModal = onOpenMonetization
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("creator_dashboard_root"),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
        // --- 1. Channel Profile & Live Status Header ---
        item {
            FrostedGlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_header_card"),
                borderColor = TelegramBlueBright
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AsyncImage(
                                model = profile.avatar,
                                contentDescription = profile.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, TelegramCyanAccent, CircleShape)
                            )
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = profile.name,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified",
                                        tint = TelegramCyanAccent,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                                Text(
                                    text = profile.handle,
                                    color = TelegramTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Studio Partner Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(TelegramBlue.copy(alpha = 0.4f), TelegramCyanAccent.copy(alpha = 0.3f))
                                    )
                                )
                                .border(1.dp, TelegramCyanAccent.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🏆 Studio Partner",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Real-time Subscriber Count
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x331C2938))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Subscribers",
                                color = TelegramTextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = String.format(Locale.US, "%,d", dashboardData.channelOverview.subscribersCount),
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = TelegramEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "+${dashboardData.channelOverview.subscribersGrowth28d} (28d)",
                                color = TelegramEmerald,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Studio Quick Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StudioActionButton(
                            icon = Icons.Default.CloudUpload,
                            label = "Upload",
                            modifier = Modifier.weight(1f),
                            onClick = onUploadClick
                        )
                        StudioActionButton(
                            icon = Icons.Default.AutoAwesome,
                            label = "Hook AI",
                            highlight = true,
                            modifier = Modifier.weight(1f),
                            onClick = onLaunchHookStudio
                        )
                        StudioActionButton(
                            icon = Icons.Default.MonetizationOn,
                            label = "Payout",
                            modifier = Modifier.weight(1f),
                            onClick = onOpenMonetization
                        )
                        StudioActionButton(
                            icon = Icons.Default.Psychology,
                            label = "Mindset",
                            modifier = Modifier.weight(1f),
                            onClick = onOpenMindsetHub
                        )
                    }
                }
            }
        }

        // --- 2. 28-Day Channel Overview Metrics (4 Cards) ---
        item {
            Text(
                text = "Channel Analytics (Last 28 Days)",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricOverviewCard(
                        title = "Views",
                        value = "${String.format(Locale.US, "%.2f", dashboardData.channelOverview.totalViews28d / 1_000_000.0)}M",
                        growth = "+${dashboardData.channelOverview.viewsGrowthPct}%",
                        icon = Icons.Default.Visibility,
                        color = TelegramBlueBright,
                        modifier = Modifier.weight(1f)
                    )
                    MetricOverviewCard(
                        title = "Watch Time",
                        value = "${String.format(Locale.US, "%.1f", dashboardData.channelOverview.watchTimeHours28d / 1000.0)}K hrs",
                        growth = "+${dashboardData.channelOverview.watchTimeGrowthPct}%",
                        icon = Icons.Default.AccessTime,
                        color = TelegramCyanAccent,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricOverviewCard(
                        title = "Est. Revenue",
                        value = "$${String.format(Locale.US, "%,.2f", dashboardData.channelOverview.estimatedRevenue28d)}",
                        growth = "+${dashboardData.channelOverview.revenueGrowthPct}%",
                        icon = Icons.Default.MonetizationOn,
                        color = TelegramEmerald,
                        modifier = Modifier.weight(1f)
                    )
                    MetricOverviewCard(
                        title = "Average RPM",
                        value = "$${String.format(Locale.US, "%.2f", dashboardData.channelOverview.averageRpm)}",
                        growth = "Per 1K views",
                        icon = Icons.Default.Speed,
                        color = Color(0xFFFFD700),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- 3. Latest Upload Performance (YouTube Studio Style) ---
        item {
            FrostedGlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("latest_upload_performance_card"),
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
                        Text(
                            text = "Latest Video Performance",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TelegramEmerald.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🏆 #${dashboardData.latestUploadPerformance.rankingAmongLast10} of 10",
                                color = TelegramEmerald,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Video Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = dashboardData.latestUploadPerformance.thumbnail,
                            contentDescription = dashboardData.latestUploadPerformance.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 96.dp, height = 58.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(8.dp))
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = dashboardData.latestUploadPerformance.title,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = dashboardData.latestUploadPerformance.uploadTimeAgo,
                                color = TelegramTextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    HorizontalDivider(color = TelegramGlassBorderSubtle)

                    // Performance Breakdown Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PerformanceMetric(
                            label = "Views",
                            value = String.format(Locale.US, "%,d", dashboardData.latestUploadPerformance.views),
                            subtext = "Typical: ${dashboardData.latestUploadPerformance.typicalViewsRange}",
                            isGood = true
                        )
                        PerformanceMetric(
                            label = "Impressions CTR",
                            value = "${dashboardData.latestUploadPerformance.impressionsCtrPct}%",
                            subtext = "Above Average",
                            isGood = true
                        )
                        PerformanceMetric(
                            label = "Avg Duration",
                            value = dashboardData.latestUploadPerformance.averageViewDuration,
                            subtext = "${dashboardData.latestUploadPerformance.averagePercentageViewed}% viewed",
                            isGood = true
                        )
                    }
                }
            }
        }

        // --- 4. Real-Time 48-Hour Live Velocity & Hourly Sparkline ---
        item {
            FrostedGlassBox(modifier = Modifier.fillMaxWidth()) {
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
                            Text(
                                text = "Realtime Activity",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Updating live • Views in last 48 hours",
                                color = TelegramTextSecondary,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = String.format(Locale.US, "%,d", dashboardData.realTimeActivity.viewsLast48Hours),
                            color = TelegramCyanAccent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Mini Custom Canvas Bar Chart for Hourly Views
                    HourlyViewsBarChart(
                        hourlyPoints = dashboardData.realTimeActivity.hourlyDataPoints,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    )

                    Text(
                        text = "Top Realtime Content Velocity",
                        color = TelegramTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        dashboardData.realTimeActivity.topRealTimeVideos.forEach { top ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0x221C2938))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = top.title,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = top.velocity,
                                        color = TelegramCyanAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = top.delta,
                                        color = TelegramEmerald,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 5. Audience Demographics & Retention ---
        item {
            FrostedGlassBox(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Audience Insights",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Returning Viewers Bar
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Returning: ${dashboardData.audienceInsights.returningViewersPct.toInt()}%", color = TelegramBlueBright, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("New Viewers: ${dashboardData.audienceInsights.newViewersPct.toInt()}%", color = TelegramCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(dashboardData.audienceInsights.returningViewersPct)
                                    .fillMaxHeight()
                                    .background(TelegramBlueBright)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(dashboardData.audienceInsights.newViewersPct)
                                    .fillMaxHeight()
                                    .background(TelegramCyanAccent)
                            )
                        }
                    }

                    // Top Countries
                    Text(
                        text = "Top Geographies",
                        color = TelegramTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    dashboardData.audienceInsights.topGeographies.forEach { geo ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(geo.country, color = Color.White, fontSize = 11.sp)
                            Text("${geo.percentage.toInt()}%", color = TelegramCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- 6. Super Thanks & Priority Comments Manager ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Priority Comments & Tips",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${dashboardData.priorityComments.size} Pending",
                    color = TelegramTextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        items(dashboardData.priorityComments) { comment ->
            PriorityCommentCard(
                comment = comment,
                onHeart = { onHeartComment(comment.id) },
                onReply = {
                    replyDialogComment = comment
                    replyInputText = comment.creatorReply ?: ""
                }
            )
        }

        // --- 7. Copyright & Content ID Health Card ---
        item {
            FrostedGlassBox(
                modifier = Modifier.fillMaxWidth(),
                borderColor = TelegramEmerald
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(TelegramEmerald.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = TelegramEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Content Rights & Channel Health",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "0 Copyright Strikes • Content ID Active",
                                color = TelegramEmerald,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Text(
                        text = "100% Clean",
                        color = TelegramEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
        }
    }

    // Reply Dialog
    replyDialogComment?.let { c ->
        AlertDialog(
            onDismissRequest = { replyDialogComment = null },
            title = {
                Text("Reply to ${c.author}", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(c.content, color = TelegramTextSecondary, fontSize = 12.sp)
                    OutlinedTextField(
                        value = replyInputText,
                        onValueChange = { replyInputText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Write creator reply...", color = TelegramTextSecondary, fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TelegramCyanAccent,
                            unfocusedBorderColor = TelegramGlassBorderSubtle,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = TelegramTextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReplyComment(c.id, replyInputText)
                        replyDialogComment = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlueBright)
                ) {
                    Text("Send Reply", color = TelegramDarkBg, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { replyDialogComment = null }) {
                    Text("Cancel", color = TelegramTextSecondary)
                }
            },
            containerColor = Color(0xF00D1622)
        )
    }
}
}

@Composable
private fun StudioActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    highlight: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .then(
                if (highlight)
                    Modifier.background(Brush.linearGradient(listOf(TelegramBlue.copy(alpha = 0.5f), TelegramCyanAccent.copy(alpha = 0.4f))))
                else
                    Modifier.background(Color(0x331C2938))
            )
            .border(1.dp, if (highlight) TelegramCyanAccent.copy(alpha = 0.6f) else TelegramGlassBorderSubtle, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (highlight) Color.White else TelegramCyanAccent,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = label,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MetricOverviewCard(
    title: String,
    value: String,
    growth: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    FrostedGlassBox(
        modifier = modifier,
        borderColor = color.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = TelegramTextSecondary, fontSize = 11.sp)
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(15.dp))
            }
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = growth,
                color = if (growth.startsWith("+")) TelegramEmerald else TelegramTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PerformanceMetric(
    label: String,
    value: String,
    subtext: String,
    isGood: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, color = TelegramTextSecondary, fontSize = 10.sp)
        Text(value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (isGood) {
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = null,
                    tint = TelegramEmerald,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(subtext, color = if (isGood) TelegramEmerald else TelegramTextSecondary, fontSize = 9.sp)
        }
    }
}

@Composable
private fun HourlyViewsBarChart(
    hourlyPoints: List<HourlyViewPoint>,
    modifier: Modifier = Modifier
) {
    val maxView = hourlyPoints.maxOfOrNull { it.viewCount } ?: 5000

    Canvas(modifier = modifier) {
        val barCount = hourlyPoints.size
        val barSpacing = 8.dp.toPx()
        val totalSpacing = barSpacing * (barCount - 1)
        val barWidth = (size.width - totalSpacing) / barCount

        hourlyPoints.forEachIndexed { index, point ->
            val barHeight = (point.viewCount.toFloat() / maxView.toFloat()) * (size.height - 12.dp.toPx())
            val x = index * (barWidth + barSpacing)
            val y = size.height - barHeight

            val brush = if (index == barCount - 1) {
                Brush.verticalGradient(listOf(Color(0xFF00E5FF), TelegramBlueBright))
            } else {
                Brush.verticalGradient(listOf(TelegramBlueBright.copy(alpha = 0.7f), Color(0x332AABEE)))
            }

            drawRoundRect(
                brush = brush,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
        }
    }
}

@Composable
private fun PriorityCommentCard(
    comment: CreatorPriorityComment,
    onHeart: () -> Unit,
    onReply: () -> Unit
) {
    FrostedGlassBox(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (comment.isSuperThanks) Color(0xFFFFD700).copy(alpha = 0.4f) else TelegramGlassBorderSubtle
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        model = comment.authorAvatar,
                        contentDescription = comment.author,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                    )
                    Column {
                        Text(comment.author, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(comment.timeAgo, color = TelegramTextSecondary, fontSize = 9.sp)
                    }
                }

                if (comment.isSuperThanks) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFD700).copy(alpha = 0.2f))
                            .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "⭐ ${comment.starsTipped} Stars",
                            color = Color(0xFFFFD700),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = comment.content,
                color = TelegramTextPrimary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            // Creator Reply if any
            comment.creatorReply?.let { reply ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(TelegramBlue.copy(alpha = 0.2f))
                        .padding(8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("↪ You:", color = TelegramCyanAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(reply, color = Color.White, fontSize = 10.sp)
                    }
                }
            }

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "on ${comment.videoTitle}",
                    color = TelegramTextSecondary,
                    fontSize = 9.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onHeart,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (comment.isHeartedByCreator) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Heart",
                            tint = if (comment.isHeartedByCreator) TelegramCoral else TelegramTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    TextButton(
                        onClick = onReply,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text("Reply", color = TelegramCyanAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

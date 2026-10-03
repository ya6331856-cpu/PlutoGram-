package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun CreatorAnalyticsView(
    dashboardData: CreatorDashboardData,
    monetization: CreatorMonetizationState,
    selectedTimeRange: String,
    onSelectTimeRange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val timeRanges = listOf("7D", "28D", "90D", "365D")

    val multiplier = when (selectedTimeRange) {
        "7D" -> 0.28
        "28D" -> 1.0
        "90D" -> 2.85
        else -> 9.4
    }

    val adjustedViews = (dashboardData.channelOverview.totalViews28d * multiplier).toLong()
    val adjustedWatchTime = (dashboardData.channelOverview.watchTimeHours28d * multiplier).toLong()
    val adjustedRevenue = dashboardData.channelOverview.estimatedRevenue28d * multiplier

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("creator_analytics_view"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Time Range Selector ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Channel Growth & Metrics",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x331C2938))
                        .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(10.dp))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    timeRanges.forEach { range ->
                        val isSelected = selectedTimeRange == range
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) TelegramBlue.copy(alpha = 0.5f) else Color.Transparent)
                                .clickable { onSelectTimeRange(range) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = range,
                                color = if (isSelected) Color.White else TelegramTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // --- 2. Primary KPI Cards Grid ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnalyticsKpiCard(
                        title = "Total Views",
                        value = "${String.format(Locale.US, "%.2f", adjustedViews / 1_000_000.0)}M",
                        delta = "+${dashboardData.channelOverview.viewsGrowthPct}%",
                        icon = Icons.Default.Visibility,
                        tint = TelegramBlueBright,
                        modifier = Modifier.weight(1f)
                    )
                    AnalyticsKpiCard(
                        title = "Est. Revenue",
                        value = "$${String.format(Locale.US, "%,.2f", adjustedRevenue)}",
                        delta = "+${dashboardData.channelOverview.revenueGrowthPct}%",
                        icon = Icons.Default.MonetizationOn,
                        tint = TelegramEmerald,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnalyticsKpiCard(
                        title = "Watch Time",
                        value = "${String.format(Locale.US, "%.1f", adjustedWatchTime / 1000.0)}K hrs",
                        delta = "+${dashboardData.channelOverview.watchTimeGrowthPct}%",
                        icon = Icons.Default.AccessTime,
                        tint = TelegramCyanAccent,
                        modifier = Modifier.weight(1f)
                    )
                    AnalyticsKpiCard(
                        title = "Average RPM",
                        value = "$${String.format(Locale.US, "%.2f", dashboardData.channelOverview.averageRpm)}",
                        delta = "Per 1K Views",
                        icon = Icons.Default.Speed,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- 3. Interactive Views & Audience Retention Curve Chart ---
        item {
            FrostedGlassBox(
                modifier = Modifier.fillMaxWidth(),
                borderColor = TelegramCyanAccent.copy(alpha = 0.4f)
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
                            Text("Views & Engagement Trajectory", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Realtime hourly audience retention curve", color = TelegramTextSecondary, fontSize = 10.sp)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TelegramEmerald.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("High Velocity 🔥", color = TelegramEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Canvas Curve Chart
                    AnalyticsSplineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Beginning ($selectedTimeRange)", color = TelegramTextSecondary, fontSize = 10.sp)
                        Text("Peak Momentum", color = TelegramCyanAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("Today", color = TelegramTextSecondary, fontSize = 10.sp)
                    }
                }
            }
        }

        // --- 4. Traffic Sources Breakdown ---
        item {
            FrostedGlassBox(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Traffic Sources",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    TrafficSourceItem("For You / Reels Algorithmic Feed", 48.2f, TelegramBlueBright)
                    TrafficSourceItem("Explore & Topic Search", 26.4f, TelegramCyanAccent)
                    TrafficSourceItem("Telegram Channels & Direct Chats", 15.8f, TelegramEmerald)
                    TrafficSourceItem("External Links & Notifications", 9.6f, Color(0xFFFFD700))
                }
            }
        }

        // --- 5. Demographics & Geographies ---
        item {
            FrostedGlassBox(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Audience Geography & Devices",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
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

                    HorizontalDivider(color = TelegramGlassBorderSubtle)

                    // Device Types
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DeviceSharePill("Android", "82%", Icons.Default.PhoneAndroid, TelegramEmerald)
                        DeviceSharePill("iOS", "15%", Icons.Default.PhoneIphone, TelegramBlueBright)
                        DeviceSharePill("Web", "3%", Icons.Default.Laptop, TelegramTextSecondary)
                    }
                }
            }
        }

        // --- 6. Top Monetized Videos Performance ---
        item {
            Text(
                text = "Top Monetized Content",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(monetization.videoEarningsList) { video ->
            FrostedGlassBox(
                modifier = Modifier.fillMaxWidth(),
                borderColor = TelegramGlassBorderSubtle
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 72.dp, height = 48.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF162231)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PlayCircle, contentDescription = null, tint = TelegramCyanAccent, modifier = Modifier.size(24.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.title,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${video.views} • RPM $${video.rpm}",
                            color = TelegramTextSecondary,
                            fontSize = 10.sp
                        )
                    }

                    Text(
                        text = "$${String.format(Locale.US, "%.2f", video.earnings)}",
                        color = TelegramEmerald,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun AnalyticsKpiCard(
    title: String,
    value: String,
    delta: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    FrostedGlassBox(
        modifier = modifier,
        borderColor = tint.copy(alpha = 0.3f)
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
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(15.dp))
            }
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (delta.startsWith("+")) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = TelegramEmerald, modifier = Modifier.size(12.dp))
                }
                Text(delta, color = if (delta.startsWith("+")) TelegramEmerald else TelegramTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AnalyticsSplineChart(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val points = listOf(
            Offset(0f, height * 0.85f),
            Offset(width * 0.15f, height * 0.72f),
            Offset(width * 0.32f, height * 0.65f),
            Offset(width * 0.50f, height * 0.40f),
            Offset(width * 0.70f, height * 0.48f),
            Offset(width * 0.85f, height * 0.22f),
            Offset(width, height * 0.15f)
        )

        val path = Path()
        val fillPath = Path()

        path.moveTo(points[0].x, points[0].y)
        fillPath.moveTo(points[0].x, points[0].y)

        for (i in 0 until points.size - 1) {
            val p0 = points[i]
            val p1 = points[i + 1]
            val controlPoint1 = Offset((p0.x + p1.x) / 2f, p0.y)
            val controlPoint2 = Offset((p0.x + p1.x) / 2f, p1.y)

            path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, p1.x, p1.y)
            fillPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, p1.x, p1.y)
        }

        fillPath.lineTo(width, height)
        fillPath.lineTo(0f, height)
        fillPath.close()

        // Gradient under curve
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(TelegramCyanAccent.copy(alpha = 0.35f), Color.Transparent),
                startY = 0f,
                endY = height
            )
        )

        // Curve Line
        drawPath(
            path = path,
            brush = Brush.horizontalGradient(listOf(TelegramBlueBright, TelegramCyanAccent, Color(0xFF00E5FF))),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Peak Dot
        val peak = points[points.size - 1]
        drawCircle(
            color = Color(0xFF00E5FF),
            radius = 5.dp.toPx(),
            center = peak
        )
    }
}

@Composable
private fun TrafficSourceItem(
    name: String,
    pct: Float,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, color = Color.White, fontSize = 11.sp)
            Text("${pct}%", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0x331C2938))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(pct / 100f)
                    .background(color)
            )
        }
    }
}

@Composable
private fun DeviceSharePill(
    device: String,
    share: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x221C2938))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
        Text(device, color = TelegramTextSecondary, fontSize = 10.sp)
        Text(share, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

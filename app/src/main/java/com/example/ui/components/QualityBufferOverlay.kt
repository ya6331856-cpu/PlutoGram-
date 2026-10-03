package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoQualityPreset
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Custom, polished, minimalist video loading animation.
 * Replaces standard material progress bars with a bespoke cinematic orbital
 * ring, fluid gradient shimmer track, and unobtrusive typography so the UI
 * remains clean and modern while waiting for sufficient network bandwidth.
 */
@Composable
fun QualityBufferOverlay(
    isBuffering: Boolean,
    preset: VideoQualityPreset,
    bufferPercent: Int,
    simulatedSpeedMbps: Float,
    statusMessage: String,
    onRetryBuffer: () -> Unit,
    onRestoreSpeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isBuffering,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.95f),
        exit = fadeOut(animationSpec = tween(250)) + scaleOut(targetScale = 0.95f),
        modifier = modifier
    ) {
        // Smooth animated buffer value (0f to 1f)
        val animatedProgress by animateFloatAsState(
            targetValue = (bufferPercent.coerceIn(0, 100)) / 100f,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "buffer_progress_anim"
        )

        // Semi-transparent cinematic dark vignette over the video
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xB3080E17),
                            Color(0xE605080E)
                        )
                    )
                )
                .clickable(enabled = false) { }
                .padding(20.dp)
                .testTag("quality_buffer_overlay"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.widthIn(max = 320.dp)
            ) {
                // Bespoke Orbital Loader
                CinematicOrbitalLoader(
                    targetResolution = preset.badge
                )

                // Clean Minimalist Status Information
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Buffering ${preset.badge}",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.4.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Zero Quality Drop • Strict HD Lock",
                        color = TelegramCyanAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.2.sp
                    )
                }

                // Custom Shimmering Linear Buffer Track (Replaces standard LinearProgressIndicator)
                CustomShimmerBufferTrack(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth(0.78f)
                        .height(3.5.dp)
                )

                // Buffer Telemetry & Bandwidth Line
                Row(
                    modifier = Modifier.fillMaxWidth(0.82f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$bufferPercent% loaded",
                        color = TelegramTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(if (simulatedSpeedMbps < 5.0f) TelegramCoral else TelegramEmerald)
                        )
                        Text(
                            text = String.format(java.util.Locale.US, "%.1f Mbps", simulatedSpeedMbps),
                            color = TelegramTextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Minimalist Floating Action Pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MinimalistActionChip(
                        label = "Retry Buffer",
                        icon = Icons.Default.Refresh,
                        onClick = onRetryBuffer
                    )

                    MinimalistActionChip(
                        label = "Restore Speed",
                        icon = Icons.Default.Speed,
                        highlight = true,
                        onClick = onRestoreSpeed
                    )
                }
            }
        }
    }
}

/**
 * Custom Canvas-based Orbital Ring Animation:
 * Features dual counter-rotating gradient arcs, a leading luminous orb,
 * and a breathing center emblem.
 */
@Composable
private fun CinematicOrbitalLoader(
    targetResolution: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbital_transition")

    // Clockwise primary rotation
    val primaryAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1350, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "primary_angle"
    )

    // Counter-clockwise secondary rotation
    val secondaryAngle by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "secondary_angle"
    )

    // Subtle breathing scale for center badge
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath_scale"
    )

    Box(
        modifier = modifier.size(76.dp),
        contentAlignment = Alignment.Center
    ) {
        // Custom Canvas drawing the orbital tracks & glowing particle
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 2.5.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val arcSize = Size(diameter, diameter)
            val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

            // 1. Subtle Background Guide Track
            drawArc(
                color = Color(0x1A2AABEE),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth * 0.8f)
            )

            // 2. Secondary Counter-Rotating Ambient Arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Transparent,
                        TelegramBlue.copy(alpha = 0.35f),
                        TelegramCyanAccent.copy(alpha = 0.55f)
                    )
                ),
                startAngle = secondaryAngle,
                sweepAngle = 100f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // 3. Primary Glowing High-Definition Arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Transparent,
                        TelegramBlueBright.copy(alpha = 0.4f),
                        TelegramCyanAccent
                    )
                ),
                startAngle = primaryAngle,
                sweepAngle = 130f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth * 1.2f, cap = StrokeCap.Round)
            )

            // 4. Luminous Particle at the leading tip of the primary arc
            val leadAngleRad = Math.toRadians((primaryAngle + 130f).toDouble())
            val radius = diameter / 2f
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val particleX = (centerX + radius * cos(leadAngleRad)).toFloat()
            val particleY = (centerY + radius * sin(leadAngleRad)).toFloat()

            // Outer particle glow
            drawCircle(
                color = TelegramCyanAccent.copy(alpha = 0.35f),
                radius = 5.5.dp.toPx(),
                center = Offset(particleX, particleY)
            )
            // Core white particle
            drawCircle(
                color = Color.White,
                radius = 2.2.dp.toPx(),
                center = Offset(particleX, particleY)
            )
        }

        // Center Minimalist Disc
        Box(
            modifier = Modifier
                .size((38 * breathScale).dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x3300E5FF),
                            Color(0x11162230)
                        )
                    )
                )
                .border(1.dp, TelegramCyanAccent.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (targetResolution.contains("4K", ignoreCase = true)) "4K" else "HD",
                color = TelegramCyanAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * Custom Shimmer Buffer Track:
 * Replaces standard LinearProgressIndicator with an ultra-thin, smooth gradient
 * track featuring an animated light shimmer that travels across the buffered segment.
 */
@Composable
private fun CustomShimmerBufferTrack(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer_transition")
    val shimmerPhase by infiniteTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_phase"
    )

    Canvas(modifier = modifier) {
        val cornerRadius = size.height / 2f

        // Track Background
        drawRoundRect(
            color = Color(0x24FFFFFF),
            size = size,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius)
        )

        // Buffered Progress Fill
        val bufferedWidth = size.width * progress.coerceIn(0f, 1f)
        if (bufferedWidth > 0f) {
            val progressSize = Size(bufferedWidth, size.height)

            // Base gradient fill
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        TelegramBlueBright,
                        TelegramCyanAccent
                    ),
                    startX = 0f,
                    endX = bufferedWidth
                ),
                size = progressSize,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius)
            )

            // Animated light gleam shimmer over buffered progress
            val shimmerStart = bufferedWidth * (shimmerPhase - 0.2f)
            val shimmerEnd = bufferedWidth * (shimmerPhase + 0.2f)
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.65f),
                        Color.Transparent
                    ),
                    startX = shimmerStart,
                    endX = shimmerEnd
                ),
                size = progressSize,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius)
            )
        }
    }
}

/**
 * Minimalist, lightweight clickable chip for quick actions during buffering.
 */
@Composable
private fun MinimalistActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    highlight: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (highlight)
                    TelegramCyanAccent.copy(alpha = 0.15f)
                else
                    Color(0x331C2938)
            )
            .border(
                1.dp,
                if (highlight) TelegramCyanAccent.copy(alpha = 0.5f) else TelegramGlassBorderSubtle,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (highlight) TelegramCyanAccent else TelegramTextSecondary,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = label,
                color = if (highlight) TelegramCyanAccent else TelegramTextPrimary,
                fontSize = 11.sp,
                fontWeight = if (highlight) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

/**
 * Minimalist Quality Badge displayed on top of the player.
 */
@Composable
fun VideoQualityBadge(
    preset: VideoQualityPreset,
    isStrictQuality: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xBB0E1724))
            .border(
                1.dp,
                if (isStrictQuality) TelegramCyanAccent.copy(alpha = 0.5f) else TelegramGlassBorderSubtle,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (isStrictQuality) TelegramCyanAccent else TelegramEmerald)
            )
            Text(
                text = "${preset.badge} • Zero-Drop",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

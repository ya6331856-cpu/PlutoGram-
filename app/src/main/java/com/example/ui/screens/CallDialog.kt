package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun CallDialog(
    callerName: String,
    callerAvatar: String,
    isVideoCall: Boolean,
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isVideoEnabled by remember { mutableStateOf(isVideoCall) }
    var isSpeakerOn by remember { mutableStateOf(true) }
    var callSeconds by remember { mutableIntStateOf(0) }

    // Call duration timer
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            callSeconds++
        }
    }

    val formattedDuration = remember(callSeconds) {
        val mins = callSeconds / 60
        val secs = callSeconds % 60
        "%02d:%02d".format(mins, secs)
    }

    // Pulse animation for avatar
    val infiniteTransition = rememberInfiniteTransition(label = "call_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "avatar_pulse"
    )

    Dialog(
        onDismissRequest = onEndCall,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF090F17))
                .testTag("call_screen")
        ) {
            // Background subtle gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                TelegramBlue.copy(alpha = 0.25f),
                                Color(0xFF060A10)
                            )
                        )
                    )
            )

            // Top Status Bar: Telegram Security & Duration
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 24.dp, start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Encryption badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x332AABEE))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = TelegramCyanAccent,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "End-to-End Encrypted Call",
                        color = TelegramCyanAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = callerName,
                    color = TelegramTextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (callSeconds == 0) "Connecting..." else formattedDuration,
                    color = if (callSeconds == 0) TelegramCyanAccent else TelegramEmerald,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Center: Caller Profile or Video Preview
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isVideoEnabled) {
                    // Video View Simulation
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.5.dp, TelegramGlassBorder, RoundedCornerShape(24.dp))
                    ) {
                        AsyncImage(
                            model = callerAvatar,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Floating Self PIP window in corner
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(14.dp)
                                .size(width = 80.dp, height = 110.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x88111D2A))
                                .border(1.dp, TelegramCyanAccent, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "You (HD)",
                                color = TelegramCyanAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Video stream active tag
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(14.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x99000000))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "720p 60fps • Plutogram Mesh",
                                color = TelegramTextPrimary,
                                fontSize = 10.sp
                            )
                        }
                    }
                } else {
                    // Voice Call Ripple Avatar
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            // Outer pulse rings
                            Box(
                                modifier = Modifier
                                    .size(170.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(TelegramBlue.copy(alpha = 0.15f))
                            )
                            Box(
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(CircleShape)
                                    .background(TelegramBlue.copy(alpha = 0.3f))
                            )

                            // Avatar
                            AsyncImage(
                                model = callerAvatar,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, TelegramCyanAccent, CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Voice waveform simulation
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val barHeights = listOf(14.dp, 24.dp, 36.dp, 20.dp, 40.dp, 28.dp, 16.dp, 32.dp, 18.dp)
                            barHeights.forEach { h ->
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .height(h)
                                        .clip(CircleShape)
                                        .background(TelegramCyanAccent)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Call Controls Row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 36.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FrostedGlassBox(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = TelegramGlassBorder
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mute button
                        IconButton(
                            onClick = { isMuted = !isMuted },
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (isMuted) TelegramCoral else Color(0x332AABEE))
                        ) {
                            Icon(
                                imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "Mute",
                                tint = Color.White
                            )
                        }

                        // Video toggle button
                        IconButton(
                            onClick = { isVideoEnabled = !isVideoEnabled },
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (isVideoEnabled) TelegramCyanAccent else Color(0x332AABEE))
                        ) {
                            Icon(
                                imageVector = if (isVideoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
                                contentDescription = "Video",
                                tint = if (isVideoEnabled) Color(0xFF0D1520) else Color.White
                            )
                        }

                        // Speaker button
                        IconButton(
                            onClick = { isSpeakerOn = !isSpeakerOn },
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (isSpeakerOn) TelegramEmerald else Color(0x332AABEE))
                        ) {
                            Icon(
                                imageVector = if (isSpeakerOn) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeMute,
                                contentDescription = "Speaker",
                                tint = Color.White
                            )
                        }

                        // End Call Red Button
                        IconButton(
                            onClick = onEndCall,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(TelegramCoral)
                                .testTag("end_call_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CallEnd,
                                contentDescription = "End Call",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

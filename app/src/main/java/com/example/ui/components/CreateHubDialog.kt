package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun CreateHubDialog(
    onDismiss: () -> Unit,
    onOpenRecordCamera: () -> Unit,
    onOpenAiStudio: () -> Unit,
    onCreatePost: () -> Unit,
    onOpenCreatorDashboard: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("create_hub_dialog"),
            borderColor = TelegramCyanAccent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
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
                                .background(
                                    Brush.linearGradient(listOf(TelegramBlueBright, TelegramCyanAccent))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = TelegramDarkBg,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "Plutogram Creator Hub",
                            color = TelegramTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TelegramTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Option 1: Live Camera Reel / Story
                CreateHubOption(
                    title = "Record 9:16 Reel or Story",
                    subtitle = "CameraX live video capture with flashlight & flip",
                    icon = Icons.Default.Videocam,
                    iconGradient = listOf(TelegramCoral, Color(0xFFFF8E53)),
                    onClick = {
                        onDismiss()
                        onOpenRecordCamera()
                    },
                    tag = "create_reel_option"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Option 2: AI Video Hook Studio
                CreateHubOption(
                    title = "AI Video Hook Studio",
                    subtitle = "Extract 3s viral hooks from long podcasts & vlogs",
                    icon = Icons.Default.AutoAwesome,
                    iconGradient = listOf(TelegramBlueBright, TelegramCyanAccent),
                    onClick = {
                        onDismiss()
                        onOpenAiStudio()
                    },
                    tag = "create_ai_studio_option"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Option 3: Post to Feed
                CreateHubOption(
                    title = "Publish New Feed Post",
                    subtitle = "Share photos, carousels, and project milestones",
                    icon = Icons.Default.PostAdd,
                    iconGradient = listOf(TelegramEmerald, Color(0xFF00E676)),
                    onClick = {
                        onDismiss()
                        onCreatePost()
                    },
                    tag = "create_post_option"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Option 4: Creator Studio & Analytics
                CreateHubOption(
                    title = "Creator Studio & Analytics",
                    subtitle = "Track subscribers, revenue, and 48h realtime views",
                    icon = Icons.Default.Dashboard,
                    iconGradient = listOf(Color(0xFF7C4DFF), TelegramBlueBright),
                    onClick = {
                        onDismiss()
                        onOpenCreatorDashboard()
                    },
                    tag = "create_dashboard_option"
                )
            }
        }
    }
}

@Composable
private fun CreateHubOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconGradient: List<Color>,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0x33162231))
            .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag(tag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(iconGradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TelegramTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = TelegramTextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TelegramTextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

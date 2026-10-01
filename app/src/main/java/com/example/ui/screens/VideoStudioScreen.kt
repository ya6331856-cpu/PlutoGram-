package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.SampleData
import com.example.model.ExtractedHook
import com.example.model.LongVideo
import com.example.ui.components.AudioWaveformVisualizer
import com.example.ui.components.FrostedChip
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*
import com.example.viewmodel.StudioSubTab
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel

@Composable
fun VideoStudioScreen(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TelegramDarkBg)
            .testTag("video_studio_screen")
    ) {
        // Studio Segment Switcher: [ Long Videos ] [ AI Video Hook Studio ]
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0x331C2938))
                    .border(1.dp, TelegramGlassBorder, RoundedCornerShape(22.dp))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Long Videos Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (uiState.activeStudioMode == StudioSubTab.LONG_VIDEOS)
                                TelegramBlue.copy(alpha = 0.35f)
                            else Color.Transparent
                        )
                        .clickable { viewModel.setStudioSubTab(StudioSubTab.LONG_VIDEOS) }
                        .testTag("studio_tab_long_videos"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = if (uiState.activeStudioMode == StudioSubTab.LONG_VIDEOS) TelegramBlueBright else TelegramTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Long Videos",
                            color = if (uiState.activeStudioMode == StudioSubTab.LONG_VIDEOS) Color.White else TelegramTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // AI Video Hook Studio Tab
                Box(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .then(
                            if (uiState.activeStudioMode == StudioSubTab.HOOK_STUDIO) {
                                Modifier.background(
                                    Brush.linearGradient(
                                        listOf(TelegramBlue.copy(alpha = 0.5f), TelegramCyanAccent.copy(alpha = 0.4f))
                                    )
                                )
                            } else Modifier
                        )
                        .clickable { viewModel.setStudioSubTab(StudioSubTab.HOOK_STUDIO) }
                        .testTag("studio_tab_ai_hook_studio"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (uiState.activeStudioMode == StudioSubTab.HOOK_STUDIO) TelegramCyanAccent else TelegramTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "AI Hook Studio",
                            color = if (uiState.activeStudioMode == StudioSubTab.HOOK_STUDIO) Color.White else TelegramTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Screen Body based on selected sub-tab
        AnimatedContent(
            targetState = uiState.activeStudioMode,
            label = "studio_content"
        ) { mode ->
            when (mode) {
                StudioSubTab.LONG_VIDEOS -> {
                    LongVideosStreamingView(uiState = uiState, viewModel = viewModel)
                }
                StudioSubTab.HOOK_STUDIO -> {
                    AiVideoHookStudioView(uiState = uiState, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun LongVideosStreamingView(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel
) {
    val filters = listOf("All", "Live", "Videos", "Podcasts", "Gaming", "Tech")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("long_videos_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Category Pills
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { f ->
                    FrostedChip(
                        text = f,
                        selected = f == "All",
                        onClick = { viewModel.filterCategory(f) }
                    )
                }
            }
        }

        // Spotlight Featured Card
        item {
            FrostedGlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(TelegramCoral)
                            )
                            Text(
                                text = "TRENDING LONG STREAM",
                                color = TelegramCoral,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "4K HDR",
                            color = TelegramCyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Global Cyber Championship - Finals (Grand Arena Stage 4)",
                        color = TelegramTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Simulated Video Preview Player
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF1E1428), Color(0xFF121B27))
                                )
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color(0x990E1724))
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xCC000000))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "LIVE • 14.2K watching",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Extract in AI Studio CTA
                    Button(
                        onClick = {
                            val liveVideo = uiState.longVideos.find { it.isLive } ?: uiState.longVideos.first()
                            viewModel.selectVideoForStudio(liveVideo)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("extract_live_hooks_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Extract Real-time Clutches in AI Hook Studio",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Long Video Catalog
        items(uiState.longVideos, key = { it.id }) { video ->
            LongVideoCard(
                video = video,
                onExtractHooks = {
                    viewModel.selectVideoForStudio(video)
                }
            )
        }
    }
}

@Composable
fun LongVideoCard(
    video: LongVideo,
    onExtractHooks: () -> Unit
) {
    FrostedGlassBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 7.dp)
            .testTag("long_video_card_${video.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Thumbnail container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(video.videoPreviewColor))
                    .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
            ) {
                // Video thumbnail overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0xAA000000))
                            )
                        )
                )

                // Play icon
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0x88000000))
                        .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Duration badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xDD000000))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = video.duration,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Hook Score Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xCC0E1724))
                        .border(1.dp, TelegramGlassBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = TelegramAmber,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Virality Potential: ${video.viralityScore}%",
                            color = TelegramTextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Channel & Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AsyncImage(
                    model = video.channelAvatar,
                    contentDescription = video.channelName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.dp, TelegramGlassBorder, CircleShape)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.title,
                        color = TelegramTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${video.channelName} • ${video.views} • ${video.timeAgo}",
                        color = TelegramTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action: AI Hook Studio Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onExtractHooks,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TelegramCyanAccent),
                    border = BorderStroke(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(TelegramBlue, TelegramCyanAccent)
                        )
                    ),
                    modifier = Modifier.testTag("extract_hooks_btn_${video.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Extract Hooks in AI Studio",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AiVideoHookStudioView(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel
) {
    val selectedVideo = uiState.selectedVideoForStudio ?: uiState.longVideos.first()
    var isWaveformPlaying by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("hook_studio_layout"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Studio Interactive Player Box
        item {
            FrostedGlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("studio_player_box")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartDisplay,
                                contentDescription = null,
                                tint = TelegramBlueBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "STUDIO TIMELINE ANALYZER",
                                color = TelegramBlueBright,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0x440E1724))
                                .border(1.dp, TelegramGlassBorder, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "4K • 60 FPS",
                                color = TelegramCyanAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = selectedVideo.title,
                        color = TelegramTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Waveform Timeline Scrubber
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F1823))
                            .border(1.dp, TelegramGlassBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "01:24 (Hook Point Alpha)",
                                    color = TelegramCyanAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = selectedVideo.duration,
                                    color = TelegramTextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            AudioWaveformVisualizer(
                                isPlaying = isWaveformPlaying,
                                activeColor = TelegramBlueBright
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Playback Controls Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            IconButton(
                                onClick = { isWaveformPlaying = !isWaveformPlaying },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(TelegramBlue)
                            ) {
                                Icon(
                                    imageVector = if (isWaveformPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Text(
                                text = "Audio Energy: 98.4 dB (Spike detected)",
                                color = TelegramTextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = "9:16 Auto-Crop: Ready",
                            color = TelegramEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(14.dp)) }

        // AI Text Prompt Engine Box
        item {
            FrostedGlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_prompt_box")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = TelegramCyanAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "AI HOOK PROMPT INSTRUCTION",
                            color = TelegramCyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.studioPrompt,
                        onValueChange = { viewModel.updateStudioPrompt(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("studio_prompt_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TelegramBlueBright,
                            unfocusedBorderColor = TelegramGlassBorder,
                            focusedContainerColor = Color(0x33111B27),
                            unfocusedContainerColor = Color(0x33111B27),
                            focusedTextColor = TelegramTextPrimary,
                            unfocusedTextColor = TelegramTextPrimary
                        ),
                        placeholder = {
                            Text(
                                text = "e.g. Find controversy, 1v5 gaming clutch, curiosity gap...",
                                color = TelegramTextMuted,
                                fontSize = 12.sp
                            )
                        },
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Preset Hook Prompts
                    Text(
                        text = "Quick Presets:",
                        color = TelegramTextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "Curiosity Gap" to "Find the biggest unanswered question and teases in the first 15 seconds",
                            "Tension Clutch" to "Find extreme tension turnaround with explosive audio spike",
                            "Agency Myth" to "Extract controversy statement that challenges consensus"
                        ).forEach { (label, promptVal) ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x331A2736))
                                    .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.updateStudioPrompt(promptVal) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = TelegramTextPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Run Analysis Button with Loading Bar
                    Button(
                        onClick = { viewModel.analyzeVideoHooks() },
                        enabled = !uiState.isAnalyzingHook,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TelegramBlue,
                            disabledContainerColor = TelegramBlue.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("run_hook_analysis_button")
                    ) {
                        if (uiState.isAnalyzingHook) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.hookAnalysisStatusText,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Extract Viral Hooks with Gemini AI",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (uiState.isAnalyzingHook) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { uiState.hookAnalysisProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = TelegramCyanAccent,
                            trackColor = Color(0x33111B27)
                        )
                    }
                }
            }
        }

        // Extracted Viral Hooks Output Section
        if (uiState.hookAnalysisResult != null) {
            val result = uiState.hookAnalysisResult

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Extracted Hooks (${result.hooks.size})",
                        color = TelegramTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TelegramAmber.copy(alpha = 0.2f))
                            .border(1.dp, TelegramAmber, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Overall Virality: ${result.overallScore}/100",
                            color = TelegramAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            items(result.hooks, key = { it.id }) { hook ->
                HookResultCard(
                    hook = hook,
                    isSelected = uiState.selectedHook?.id == hook.id,
                    onSelect = { viewModel.selectHook(hook) },
                    onExportReel = {
                        // Switch to Reels tab to show published/preview reel
                        viewModel.selectTab(1)
                    },
                    onHireEditor = {
                        // Switch to profile and open hire checkout
                        viewModel.selectTab(4)
                        viewModel.openHireService(SampleData.freelanceServices.first())
                    }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun HookResultCard(
    hook: ExtractedHook,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onExportReel: () -> Unit,
    onHireEditor: () -> Unit
) {
    FrostedGlassBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onSelect)
            .testTag("hook_card_${hook.id}"),
        borderColor = if (isSelected) TelegramBlue else TelegramGlassBorder
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timestamp range
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TelegramBlue.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⏱ ${hook.timestampStart} - ${hook.timestampEnd} (18s)",
                        color = TelegramBlueBright,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Retention Boost Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TelegramEmerald.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = hook.retentionBoost,
                        color = TelegramEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = hook.title,
                color = TelegramTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = hook.transcriptSnippet,
                color = TelegramTextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = TelegramAmber,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "Virality Trigger: ${hook.viralityReason}",
                    color = TelegramTextMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row: Export to 9:16 Reel OR Hire Editor
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onExportReel,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("export_reel_${hook.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.IosShare,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Export Reel", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onHireEditor,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TelegramCyanAccent),
                    border = BorderStroke(
                        1.dp,
                        Brush.horizontalGradient(listOf(TelegramBlue, TelegramCyanAccent))
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hire_editor_${hook.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Hire Editor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

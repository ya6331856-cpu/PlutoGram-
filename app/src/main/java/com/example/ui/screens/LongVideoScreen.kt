package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Comment
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.model.LongVideo
import com.example.ui.components.FrostedChip
import com.example.ui.components.FrostedGlassBox
import com.example.ui.components.QualityBufferOverlay
import com.example.ui.components.VideoQualityBadge
import com.example.ui.theme.*
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel
import kotlinx.coroutines.delay

@Composable
fun LongVideoScreen(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    modifier: Modifier = Modifier
) {
    var activeVideo by remember { mutableStateOf(uiState.longVideos.firstOrNull()) }
    var isPlaying by remember { mutableStateOf(true) }
    var currentProgress by remember { mutableFloatStateOf(0.35f) }
    var isSubscribed by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    var showThumbnailDialog by remember { mutableStateOf(false) }
    var showAudioTranscribeDialog by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    var isDownloaded by remember { mutableStateOf(false) }
    var showCommentsSheet by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var showRemixDialog by remember { mutableStateOf(false) }
    var isCreatorMode by remember { mutableStateOf(false) }

    val currentVideo = activeVideo ?: uiState.longVideos.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TelegramDarkBg)
            .testTag("long_video_screen"),
        contentPadding = PaddingValues(bottom = 88.dp)
    ) {
        // --- 16:9 Cinema Widescreen Video Player ---
        item {
            if (currentVideo != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color(currentVideo.videoPreviewColor))
                        .testTag("cinema_widescreen_player")
                ) {
                    // Video Thumbnail Backdrop
                    AsyncImage(
                        model = currentVideo.thumbnailUrl,
                        contentDescription = currentVideo.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0x66000000),
                                        Color.Transparent,
                                        Color(0xDD0A1017)
                                    )
                                )
                            )
                    )

                    // Center Play / Pause Indicator
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0x88000000))
                            .clickable { isPlaying = !isPlaying },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Top Quality Pill with Strict Zero-Drop indicator
                    VideoQualityBadge(
                        preset = uiState.selectedQualityPreset,
                        isStrictQuality = uiState.isStrictQualityMode,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        onClick = { viewModel.simulateSlowNetworkToggle() }
                    )

                    // Bottom Controls Bar (Scrubber & Timestamps)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "08:35 / ${currentVideo.duration}",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = "Fullscreen",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Custom Scrubbing Progress Slider
                        Slider(
                            value = currentProgress,
                            onValueChange = { currentProgress = it },
                            colors = SliderDefaults.colors(
                                thumbColor = TelegramCyanAccent,
                                activeTrackColor = TelegramBlueBright,
                                inactiveTrackColor = Color(0x55FFFFFF)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                        )
                    }

                    // Strict Quality Protection Buffering Overlay HUD
                    QualityBufferOverlay(
                        isBuffering = uiState.isBufferingDueToQualityLock,
                        preset = uiState.selectedQualityPreset,
                        bufferPercent = uiState.bufferProgressPercent,
                        simulatedSpeedMbps = uiState.simulatedSpeedMbps,
                        statusMessage = uiState.bufferStatusMessage,
                        onRetryBuffer = { viewModel.retryHighQualityBuffer() },
                        onRestoreSpeed = { viewModel.simulateSlowNetworkToggle() }
                    )
                }
            }
        }

        // --- Video Title & Metadata ---
        item {
            if (currentVideo != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = currentVideo.title,
                        color = TelegramTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${currentVideo.views} • ${currentVideo.timeAgo}",
                            color = TelegramTextSecondary,
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0x332AABEE))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = currentVideo.category,
                                color = TelegramCyanAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Channel Row: Avatar + Subscribe
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AsyncImage(
                                model = currentVideo.channelAvatar,
                                contentDescription = currentVideo.channelName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, TelegramGlassBorder, CircleShape)
                            )
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = currentVideo.channelName,
                                        color = TelegramTextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified",
                                        tint = TelegramBlueBright,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = "1.48M subscribers",
                                    color = TelegramTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Button(
                            onClick = { isSubscribed = !isSubscribed },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSubscribed) Color(0x332AABEE) else TelegramCoral
                            ),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(
                                text = if (isSubscribed) "Subscribed ✓" else "Subscribe",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mode Switcher Header: Clearly distinguishes Creator Profile vs Viewer Experience
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isCreatorMode) "👑 Creator Studio Suite (Owner View)" else "Viewer Actions",
                            color = if (isCreatorMode) TelegramAmber else TelegramTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Toggle button for user to test / view as Creator or Regular Viewer
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isCreatorMode) TelegramAmber.copy(alpha = 0.15f) else Color(0x331C2836))
                                .border(1.dp, if (isCreatorMode) TelegramAmber else TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                                .clickable { isCreatorMode = !isCreatorMode }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("toggle_creator_mode_btn")
                        ) {
                            Text(
                                text = if (isCreatorMode) "Switch to Viewer" else "Creator Profile ⚙️",
                                color = if (isCreatorMode) TelegramAmber else TelegramCyanAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // If user is a Creator (Creator Profile mode active): Show specialized AI Hook & Audio tools
                    if (isCreatorMode) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.testTag("creator_tools_row")
                        ) {
                            // Creator Tool 1: Extract 3s Hook to Reels
                            item {
                                CreatorToolChip(
                                    icon = Icons.Default.AutoAwesome,
                                    label = "Extract 3s Hook",
                                    tint = TelegramCyanAccent,
                                    onClick = {
                                        viewModel.selectVideoForStudio(currentVideo)
                                        viewModel.analyzeVideoHooks()
                                        viewModel.selectTab(2) // Jump to Studio
                                    },
                                    tag = "tool_extract_hook"
                                )
                            }

                            // Creator Tool: Video Monetization & RPM
                            item {
                                val itemEarnings = uiState.monetization.videoEarningsList.find { it.videoId == currentVideo.id }?.earnings ?: 840.50
                                CreatorToolChip(
                                    icon = Icons.Default.MonetizationOn,
                                    label = "Earned $${String.format(java.util.Locale.US, "%.2f", itemEarnings)}",
                                    tint = TelegramEmerald,
                                    onClick = {
                                        viewModel.selectTab(4)
                                    },
                                    tag = "tool_video_monetization"
                                )
                            }

                            // Creator Tool 2: Transcribe Audio (gemini-3.5-transcribe)
                            item {
                                CreatorToolChip(
                                    icon = Icons.Default.Mic,
                                    label = "Transcribe Audio",
                                    tint = TelegramAmber,
                                    onClick = { showAudioTranscribeDialog = true },
                                    tag = "tool_transcribe_audio"
                                )
                            }

                            // Creator Tool 3: Generate Thumbnail (gemini-3.1-flash-image-preview)
                            item {
                                CreatorToolChip(
                                    icon = Icons.Default.Image,
                                    label = "AI Thumbnail",
                                    tint = TelegramEmerald,
                                    onClick = { showThumbnailDialog = true },
                                    tag = "tool_ai_thumbnail"
                                )
                            }

                            // Creator Tool 4: Retention Analytics
                            item {
                                CreatorToolChip(
                                    icon = Icons.Default.Analytics,
                                    label = "74% Retention",
                                    tint = TelegramBlueBright,
                                    onClick = { },
                                    tag = "tool_retention_analytics"
                                )
                            }
                        }
                    } else {
                        // FOR REGULAR USERS / VIEWERS: Standard Social Interaction Row (Like, Dislike, Comment, Share, Remix, Download, Save)
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.testTag("viewer_actions_row")
                        ) {
                            // 1. Like Button (with live like counter)
                            item {
                                ViewerActionChip(
                                    icon = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                    label = if (isLiked) "48.3K" else "48.2K",
                                    isActive = isLiked,
                                    activeTint = TelegramCyanAccent,
                                    onClick = {
                                        isLiked = !isLiked
                                        if (isLiked) isDisliked = false
                                    },
                                    tag = "viewer_action_like"
                                )
                            }

                            // 2. Dislike Button
                            item {
                                ViewerActionChip(
                                    icon = if (isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                                    label = "Dislike",
                                    isActive = isDisliked,
                                    activeTint = TelegramAmber,
                                    onClick = {
                                        isDisliked = !isDisliked
                                        if (isDisliked) isLiked = false
                                    },
                                    tag = "viewer_action_dislike"
                                )
                            }

                            // 3. Comments Button (Opens interactive comments sheet)
                            item {
                                ViewerActionChip(
                                    icon = Icons.AutoMirrored.Outlined.Comment,
                                    label = "1.2K",
                                    onClick = { showCommentsSheet = true },
                                    tag = "viewer_action_comments"
                                )
                            }

                            // 4. Share Button (Opens interactive share dialog)
                            item {
                                ViewerActionChip(
                                    icon = Icons.Outlined.Share,
                                    label = "Share",
                                    onClick = { showShareDialog = true },
                                    tag = "viewer_action_share"
                                )
                            }

                            // 5. Super Thanks & Direct Stars Tipping
                            item {
                                ViewerActionChip(
                                    icon = Icons.Default.Stars,
                                    label = "Thanks ⭐",
                                    isActive = true,
                                    activeTint = Color(0xFFFFD700),
                                    onClick = {
                                        viewModel.openSuperThanksDialog(
                                            currentVideo.title,
                                            currentVideo.channelName
                                        )
                                    },
                                    tag = "viewer_action_super_thanks"
                                )
                            }

                            // 5. Remix / Clip Button (cuts 15s snippet)
                            item {
                                ViewerActionChip(
                                    icon = Icons.Default.ContentCut,
                                    label = "Remix",
                                    onClick = { showRemixDialog = true },
                                    tag = "viewer_action_remix"
                                )
                            }

                            // 6. Download / Offline Playback Button
                            item {
                                ViewerActionChip(
                                    icon = if (isDownloaded) Icons.Filled.CheckCircle else Icons.Default.FileDownload,
                                    label = if (isDownloaded) "Downloaded" else "Download",
                                    isActive = isDownloaded,
                                    activeTint = TelegramEmerald,
                                    onClick = { isDownloaded = !isDownloaded },
                                    tag = "viewer_action_download"
                                )
                            }

                            // 7. Save / Watch Later Button
                            item {
                                ViewerActionChip(
                                    icon = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    label = if (isSaved) "Saved" else "Save",
                                    isActive = isSaved,
                                    activeTint = TelegramCoral,
                                    onClick = { isSaved = !isSaved },
                                    tag = "viewer_action_save"
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Video Chapters Timeline ---
        item {
            if (currentVideo != null && currentVideo.chapters.isNotEmpty()) {
                FrostedGlassBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatListNumbered,
                                contentDescription = null,
                                tint = TelegramCyanAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Video Chapters & Hook Moments",
                                color = TelegramTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        currentVideo.chapters.forEach { chapter ->
                            val parts = chapter.split(" ", limit = 2)
                            val timestamp = parts.getOrNull(0) ?: "00:00"
                            val title = parts.getOrNull(1) ?: chapter

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { }
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0x332AABEE))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = timestamp,
                                            color = TelegramCyanAccent,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = title,
                                        color = TelegramTextPrimary,
                                        fontSize = 12.sp
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = TelegramTextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Category Filter Chips for Long Videos ---
        item {
            Column(modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)) {
                Text(
                    text = "Explore Categories • Long Videos",
                    color = TelegramTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("long_videos_category_row"),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf("All", "Tech", "Gaming", "Entertainment", "Lifestyle", "Art", "Podcasts", "Masterclasses")
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        FrostedChip(
                            text = category,
                            selected = isSelected,
                            onClick = { selectedCategory = category }
                        )
                    }
                }
            }
        }

        // --- Stream of Related Long-Form Videos (Filtered by Selected Category) ---
        val displayedVideos = if (selectedCategory == "All") {
            uiState.longVideos
        } else {
            val filtered = uiState.longVideos.filter {
                it.category.equals(selectedCategory, ignoreCase = true)
            }
            if (filtered.isNotEmpty()) filtered else uiState.longVideos
        }

        items(displayedVideos) { video ->
            LongVideoCardItem(
                video = video,
                onClick = { activeVideo = video }
            )
        }
    }

    // --- Modal: Audio Transcription (gemini-3.5-transcribe) ---
    if (showAudioTranscribeDialog) {
        AudioTranscribeDialog(
            onDismiss = { showAudioTranscribeDialog = false }
        )
    }

    // --- Modal: AI Thumbnail Studio (gemini-3.1-flash-image-preview) ---
    if (showThumbnailDialog) {
        ThumbnailGeneratorDialog(
            currentTitle = currentVideo?.title ?: "Masterclass",
            onDismiss = { showThumbnailDialog = false }
        )
    }

    // --- Modal: Viewer Comments Sheet ---
    if (showCommentsSheet) {
        LongVideoCommentsDialog(
            videoTitle = currentVideo?.title ?: "Video",
            onDismiss = { showCommentsSheet = false }
        )
    }

    // --- Modal: Viewer Share Dialog ---
    if (showShareDialog) {
        LongVideoShareDialog(
            videoTitle = currentVideo?.title ?: "Video",
            onDismiss = { showShareDialog = false }
        )
    }

    // --- Modal: Viewer Remix Clip Dialog ---
    if (showRemixDialog) {
        LongVideoRemixDialog(
            videoTitle = currentVideo?.title ?: "Video",
            onRemixToReel = {
                showRemixDialog = false
                if (currentVideo != null) {
                    viewModel.selectVideoForStudio(currentVideo)
                    viewModel.analyzeVideoHooks()
                    viewModel.selectTab(2)
                }
            },
            onDismiss = { showRemixDialog = false }
        )
    }
}

@Composable
private fun ViewerActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean = false,
    activeTint: Color = TelegramCyanAccent,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isActive) activeTint.copy(alpha = 0.18f) else Color(0x331E2D3E))
            .border(
                1.dp,
                if (isActive) activeTint else TelegramGlassBorderSubtle,
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag(tag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) activeTint else TelegramTextPrimary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                color = if (isActive) activeTint else TelegramTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CreatorToolChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x331E2D3E))
            .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag(tag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            Text(text = label, color = TelegramTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun LongVideoCardItem(
    video: LongVideo,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("long_video_card_${video.id}")
    ) {
        Column {
            // Widescreen 16:9 Thumbnail
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(video.videoPreviewColor))
            ) {
                AsyncImage(
                    model = video.thumbnailUrl,
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Duration Tag
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xCC000000))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = video.duration,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Details Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AsyncImage(
                    model = video.channelAvatar,
                    contentDescription = video.channelName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.title,
                        color = TelegramTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${video.channelName} • ${video.views} • ${video.timeAgo}",
                        color = TelegramTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// --- Audio Transcription Dialog using model gemini-3.5-transcribe ---
@Composable
fun AudioTranscribeDialog(onDismiss: () -> Unit) {
    var isRecording by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var transcriptionText by remember { mutableStateOf("") }
    var recordedSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordedSeconds = 0
            while (isRecording) {
                delay(1000)
                recordedSeconds++
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .testTag("audio_transcribe_dialog"),
            borderColor = TelegramAmber
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
                                .background(TelegramAmber.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = TelegramAmber,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Voice Hook Transcriber",
                                color = TelegramTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Model: gemini-3.5-transcribe",
                                color = TelegramAmber,
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TelegramTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Speak into your microphone or play audio to generate high-retention transcript subtitles.",
                    color = TelegramTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Microphone Record Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x33172433))
                        .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(14.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(if (isRecording) TelegramCoral else TelegramBlue)
                                .clickable {
                                    if (isRecording) {
                                        isRecording = false
                                        isProcessing = true
                                    } else {
                                        isRecording = true
                                        transcriptionText = ""
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = "Record",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isRecording) "Recording... %02d:%02d".format(recordedSeconds / 60, recordedSeconds % 60) else "Tap to Speak",
                            color = if (isRecording) TelegramCoral else TelegramTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Processing or Results
                LaunchedEffect(isProcessing) {
                    if (isProcessing) {
                        delay(1200)
                        transcriptionText = "\"In the next 30 seconds, I am going to reveal the exact viral retention loop that gained us 4.2 million organic views on Plutogram without spending a single dollar on ads.\""
                        isProcessing = false
                    }
                }

                if (isProcessing) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = TelegramAmber,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Transcribing with gemini-3.5-transcribe...",
                            color = TelegramAmber,
                            fontSize = 11.sp
                        )
                    }
                }

                if (transcriptionText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Transcribed Hook Script:",
                        color = TelegramTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x44263342))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = transcriptionText,
                            color = TelegramTextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

// --- Thumbnail Creator & Editor using gemini-3.1-flash-image-preview ---
@Composable
fun ThumbnailGeneratorDialog(
    currentTitle: String,
    onDismiss: () -> Unit
) {
    var promptInput by remember { mutableStateOf("Cinematic neon 4K YouTube thumbnail for: $currentTitle, vibrant cyber lighting") }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedImage by remember { mutableStateOf<String?>("https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800") }

    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .testTag("ai_thumbnail_dialog"),
            borderColor = TelegramEmerald
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = null,
                                tint = TelegramEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "AI Thumbnail Studio",
                                color = TelegramTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Model: gemini-3.1-flash-image-preview",
                                color = TelegramEmerald,
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TelegramTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Generated Image Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF14202E)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isGenerating) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = TelegramEmerald, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Rendering 16:9 thumbnail with gemini-3.1-flash-image-preview...",
                                color = TelegramEmerald,
                                fontSize = 11.sp
                            )
                        }
                    } else if (generatedImage != null) {
                        AsyncImage(
                            model = generatedImage,
                            contentDescription = "Generated Thumbnail",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = promptInput,
                    onValueChange = { promptInput = it },
                    label = { Text("Prompt (Create or Edit Art)", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        isGenerating = true
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramEmerald),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Brush, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Generate with gemini-3.1-flash-image-preview", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                LaunchedEffect(isGenerating) {
                    if (isGenerating) {
                        delay(1500)
                        generatedImage = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800"
                        isGenerating = false
                    }
                }
            }
        }
    }
}

// --- Comments Dialog for Regular Viewers ---
private data class ViewerCommentItem(
    val id: String,
    val authorName: String,
    val authorAvatar: String,
    val text: String,
    val timeAgo: String,
    var likesCount: Int,
    var isLiked: Boolean = false
)

@Composable
fun LongVideoCommentsDialog(
    videoTitle: String,
    onDismiss: () -> Unit
) {
    var commentsList by remember {
        mutableStateOf(
            listOf(
                ViewerCommentItem("c1", "Sophia Chen", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200", "The 3-second hook principle doubled my reel retention! Incredible pacing breakdown.", "2h ago", 142, false),
                ViewerCommentItem("c2", "Marcus Kane", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200", "That single softbox lighting trick saved our client studio setup. Top-tier quality!", "5h ago", 88, false),
                ViewerCommentItem("c3", "Aria Tanaka", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200", "Can you do a follow-up on audio waveforms and tonal drops? Subscribed!", "1d ago", 45, false)
            )
        )
    }
    var newCommentText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(12.dp)
                .testTag("long_video_comments_dialog"),
            borderColor = TelegramBlueBright
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Comments",
                            color = TelegramTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${commentsList.size} comments on this video",
                            color = TelegramTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TelegramTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = TelegramGlassBorderSubtle, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(10.dp))

                // Comments List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(commentsList, key = { it.id }) { comment ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AsyncImage(
                                model = comment.authorAvatar,
                                contentDescription = comment.authorName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = comment.authorName,
                                        color = TelegramTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = comment.timeAgo,
                                        color = TelegramTextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = comment.text,
                                    color = TelegramTextPrimary.copy(alpha = 0.9f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.clickable {
                                            commentsList = commentsList.map {
                                                if (it.id == comment.id) {
                                                    it.copy(
                                                        isLiked = !it.isLiked,
                                                        likesCount = if (it.isLiked) it.likesCount - 1 else it.likesCount + 1
                                                    )
                                                } else it
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (comment.isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                            contentDescription = "Like",
                                            tint = if (comment.isLiked) TelegramCyanAccent else TelegramTextSecondary,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = comment.likesCount.toString(),
                                            color = if (comment.isLiked) TelegramCyanAccent else TelegramTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                    Text(text = "Reply", color = TelegramTextSecondary, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Comment Input Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0x331C2836))
                        .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(24.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newCommentText,
                        onValueChange = { newCommentText = it },
                        placeholder = { Text("Add a comment...", color = TelegramTextSecondary, fontSize = 12.sp) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = TelegramTextPrimary,
                            unfocusedTextColor = TelegramTextPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (newCommentText.isNotBlank()) {
                                commentsList = commentsList + ViewerCommentItem(
                                    id = "c_${System.currentTimeMillis()}",
                                    authorName = "You",
                                    authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
                                    text = newCommentText.trim(),
                                    timeAgo = "Just now",
                                    likesCount = 0
                                )
                                newCommentText = ""
                            }
                        },
                        enabled = newCommentText.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (newCommentText.isNotBlank()) TelegramCyanAccent else TelegramTextSecondary.copy(alpha = 0.4f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// --- Share Dialog for Regular Viewers ---
@Composable
fun LongVideoShareDialog(
    videoTitle: String,
    onDismiss: () -> Unit
) {
    var isCopied by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("long_video_share_dialog"),
            borderColor = TelegramCyanAccent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Share Video",
                        color = TelegramTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TelegramTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = videoTitle,
                    color = TelegramTextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Share Targets Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ShareTargetItem(
                        icon = Icons.AutoMirrored.Filled.Send,
                        label = "Telegram",
                        tint = TelegramBlueBright,
                        onClick = onDismiss
                    )
                    ShareTargetItem(
                        icon = Icons.Default.ChatBubble,
                        label = "WhatsApp",
                        tint = TelegramEmerald,
                        onClick = onDismiss
                    )
                    ShareTargetItem(
                        icon = Icons.Default.ContentCopy,
                        label = if (isCopied) "Copied! ✓" else "Copy Link",
                        tint = TelegramCyanAccent,
                        onClick = { isCopied = true }
                    )
                    ShareTargetItem(
                        icon = Icons.Default.Public,
                        label = "Social Feed",
                        tint = TelegramAmber,
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}

@Composable
private fun ShareTargetItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.18f))
                .border(1.dp, tint.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, color = TelegramTextPrimary, fontSize = 11.sp)
    }
}

// --- Remix / Clip Dialog for Viewers ---
@Composable
fun LongVideoRemixDialog(
    videoTitle: String,
    onRemixToReel: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("long_video_remix_dialog"),
            borderColor = TelegramCoral
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
                                .background(TelegramCoral.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.ContentCut, contentDescription = null, tint = TelegramCoral, modifier = Modifier.size(18.dp))
                        }
                        Text(text = "Remix into Reel", color = TelegramTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TelegramTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Clip 15-30 seconds of this long video with sound design and publish it as a vertical Reel with creator attribution.",
                    color = TelegramTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onRemixToReel,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramCoral),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Clip & Send to Hook Studio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

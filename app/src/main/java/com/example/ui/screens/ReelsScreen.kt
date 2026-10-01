package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Reel
import com.example.ui.components.AudioWaveformVisualizer
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*
import com.example.viewmodel.StudioSubTab
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReelsScreen(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    modifier: Modifier = Modifier
) {
    val reels = uiState.reels
    val pagerState = rememberPagerState(pageCount = { reels.size })

    LaunchedEffect(pagerState.currentPage) {
        viewModel.setCurrentReelIndex(pagerState.currentPage)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TelegramDarkBg)
            .testTag("reels_screen")
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val reel = reels[page]
            ReelItem(
                reel = reel,
                onLike = { viewModel.toggleLikeReel(reel.id) },
                onFollow = { viewModel.toggleFollowReelCreator(reel.id) },
                onSendHookToStudio = {
                    viewModel.selectTab(2) // Jump to AI Studio
                    viewModel.setStudioSubTab(StudioSubTab.HOOK_STUDIO)
                }
            )
        }

        // Top Subtle Glass Overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reels",
                color = TelegramTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x770E1724))
                        .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "9:16 Studio",
                        color = TelegramCyanAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun ReelItem(
    reel: Reel,
    onLike: () -> Unit,
    onFollow: () -> Unit,
    onSendHookToStudio: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var showHeartAnimation by remember { mutableStateOf(false) }
    var showDetailsSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        onLike()
                        showHeartAnimation = true
                        coroutineScope.launch {
                            delay(800)
                            showHeartAnimation = false
                        }
                    },
                    onTap = { isPlaying = !isPlaying }
                )
            }
    ) {
        // High fidelity simulated video backdrop with deep stylized gradients
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(reel.mediaColorHex),
                            Color(0xFF0A1017),
                            Color(reel.mediaColorHex).copy(alpha = 0.85f),
                            TelegramDarkBg
                        )
                    )
                )
        ) {
            // Stylized frosted lighting grid in background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                TelegramBlueBright.copy(alpha = 0.12f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Play / Pause indicator when paused
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0x99000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Paused",
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            // Big Double-Tap Heart Animation
            if (showHeartAnimation) {
                val scale by animateFloatAsState(
                    targetValue = if (showHeartAnimation) 1.4f else 0.5f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "heart_scale"
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .scale(scale),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = TelegramCoral,
                        modifier = Modifier.size(90.dp)
                    )
                }
            }
        }

        // Bottom Shadow Scrim for text readability
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xAA0B141D),
                            TelegramDarkBg
                        )
                    )
                )
        )

        // Right Action Rail (Likes, Comments, Share, Bookmark, Hook Studio Button)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like Action
            ReelActionItem(
                icon = if (reel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                label = formatCount(reel.likesCount),
                tint = if (reel.isLiked) TelegramCoral else Color.White,
                onClick = onLike,
                tag = "reel_like_${reel.id}"
            )

            // Comments
            ReelActionItem(
                icon = Icons.Outlined.ChatBubbleOutline,
                label = formatCount(reel.commentsCount),
                tint = Color.White,
                onClick = { },
                tag = "reel_comment_${reel.id}"
            )

            // Share / Telegram Send
            ReelActionItem(
                icon = Icons.AutoMirrored.Filled.Send,
                label = formatCount(reel.sharesCount),
                tint = Color.White,
                onClick = { },
                tag = "reel_share_${reel.id}"
            )

            // AI Hook Studio Quick Action
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(TelegramBlueBright, TelegramCyanAccent)
                        )
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                    .clickable(onClick = onSendHookToStudio)
                    .testTag("reel_extract_hook_${reel.id}"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Extract Hook",
                    tint = TelegramDarkBg,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = "Hook AI",
                color = TelegramCyanAccent,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Clean & Unobstructed Bottom Left: Only Creator Handle, Audio, and Subtle Info Pill
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.76f)
                .padding(start = 16.dp, bottom = 88.dp)
        ) {
            // Creator Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = reel.creatorAvatar,
                    contentDescription = reel.creatorName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.White, CircleShape)
                )

                Text(
                    text = reel.creatorHandle,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (reel.isFollowed) Color(0x44FFFFFF) else TelegramBlueBright)
                        .clickable(onClick = onFollow)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (reel.isFollowed) "Following" else "+ Follow",
                        color = if (reel.isFollowed) Color.White else TelegramDarkBg,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Audio & Details Pill Row (Unobtrusive)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Subtle audio ticker
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x55000000))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Audio Track",
                        tint = TelegramCyanAccent,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "${reel.soundTrackTitle.take(18)}...",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 10.sp
                    )
                }

                // Expandable info button: Opens Description, Hashtags, and Algorithm data
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x66162434))
                        .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                        .clickable { showDetailsSheet = true }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .testTag("reel_open_details_${reel.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Details",
                            tint = TelegramCyanAccent,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Info & Tags",
                            color = TelegramCyanAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Expandable Bottom Sheet for Description, Hashtag Keywords & Algorithm Signals
        if (showDetailsSheet) {
            ReelDetailsSheet(
                reel = reel,
                onDismiss = { showDetailsSheet = false }
            )
        }
    }
}

@Composable
fun ReelDetailsSheet(
    reel: Reel,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("reel_details_dialog"),
            borderColor = TelegramCyanAccent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
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
                        AsyncImage(
                            model = reel.creatorAvatar,
                            contentDescription = reel.creatorName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                        Column {
                            Text(
                                text = reel.creatorName,
                                color = TelegramTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = reel.creatorHandle,
                                color = TelegramTextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TelegramTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Caption / Description
                Text(
                    text = "Description",
                    color = TelegramTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reel.caption,
                    color = TelegramTextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Algorithm & Search Keywords / Hashtags
                Text(
                    text = "Algorithm Tags & Keywords",
                    color = TelegramTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val allTags = reel.tags.ifEmpty { listOf("#Plutogram", "#ViralHook", "#Reels", "#${reel.category}") }
                    allTags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x332AABEE))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tag,
                                color = TelegramCyanAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // AI Hook & Algorithm Retention Signals
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x22162231))
                        .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = TelegramAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "AI Recommendation Score: ${reel.viralityScore}/100",
                                color = TelegramTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Hook Type: ${reel.hookSummary} • Category: ${reel.category}",
                            color = TelegramTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReelActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0x55111B27))
                .border(1.dp, Color(0x33FFFFFF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}

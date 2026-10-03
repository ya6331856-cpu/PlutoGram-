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
import com.example.model.Post
import com.example.model.Story
import com.example.ui.components.FrostedChip
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel

@Composable
fun HomeScreen(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_feed_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Stories Row (Telegram styled)
        item {
            StoriesSection(stories = uiState.stories)
        }

        // Productivity vs. Consumption Motivational Banner
        item {
            FrostedGlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("productivity_vs_consumption_banner"),
                borderColor = TelegramCyanAccent
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
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
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(TelegramBlueBright, TelegramCyanAccent)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Productivity vs. Consumption",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Shift from passive scrolling to active creation & monetization",
                                    color = TelegramTextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TelegramEmerald.copy(alpha = 0.2f))
                                .border(1.dp, TelegramEmerald.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "+$84.50 Today",
                                color = TelegramEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Metric Strip & Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = TelegramCyanAccent,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "${uiState.creatorMinutesCreatedToday}m Created vs ${uiState.creatorMinutesConsumedToday}m Consumed",
                                color = TelegramCyanAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            TextButton(
                                onClick = { viewModel.openCreatorMindsetDialog(true) },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Value Hub 🧠", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    viewModel.selectTab(2)
                                    viewModel.setStudioSubTab(com.example.viewmodel.StudioSubTab.HOOK_STUDIO)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = TelegramBlueBright),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("AI Studio ⚡", color = TelegramDarkBg, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Posts List (Uncluttered main social feed)
        items(uiState.posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                onLike = { viewModel.toggleLikePost(post.id) },
                onReact = { emoji -> viewModel.reactToPost(post.id, emoji) },
                onSave = { viewModel.toggleSavePost(post.id) },
                onComment = { viewModel.recordInteraction(post.category, 2, "Commented on post") },
                onShare = { viewModel.recordInteraction(post.category, 3, "Shared post") },
                onTip = { viewModel.openTipCreatorDialog(post.authorName, post.authorAvatar, post.content) }
            )
        }
    }
}

@Composable
fun StoriesSection(stories: List<Story>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(stories, key = { it.id }) { story ->
            StoryItem(story = story)
        }
    }
}

@Composable
fun StoryItem(story: Story) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clickable { }
            .testTag("story_item_${story.id}")
    ) {
        Box(
            modifier = Modifier.size(62.dp),
            contentAlignment = Alignment.Center
        ) {
            // Gradient Ring for active/unseen story
            val ringBrush = if (story.hasUnseen) {
                Brush.sweepGradient(listOf(TelegramBlueBright, TelegramCyanAccent, TelegramPurple, TelegramBlueBright))
            } else if (story.isUserStory) {
                Brush.linearGradient(listOf(TelegramBlue, TelegramBlueDark))
            } else {
                Brush.linearGradient(listOf(Color(0x33FFFFFF), Color(0x22FFFFFF)))
            }

            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(ringBrush)
                    .padding(2.5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(TelegramDarkBg)
                        .padding(2.dp)
                ) {
                    AsyncImage(
                        model = story.authorAvatar,
                        contentDescription = story.authorName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }

            if (story.isUserStory) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(TelegramBlueBright)
                        .border(2.dp, TelegramDarkBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Story",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (story.isUserStory) "Your Story" else story.authorName.split(" ").first(),
            color = TelegramTextPrimary,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PostCard(
    post: Post,
    onLike: () -> Unit,
    onReact: (String) -> Unit,
    onSave: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onTip: () -> Unit = {}
) {
    var showReactionPicker by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    var isCommentFocused by remember { mutableStateOf(false) }

    FrostedGlassBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 7.dp)
            .testTag("post_card_${post.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Author Row
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
                        model = post.authorAvatar,
                        contentDescription = post.authorName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.dp, TelegramGlassBorder, CircleShape)
                    )

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = post.authorName,
                                color = TelegramTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (post.isVerified) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = TelegramBlueBright,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Text(
                            text = "${post.authorHandle} • ${post.timeAgo}",
                            color = TelegramTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Follow Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(TelegramBlue.copy(alpha = 0.15f))
                            .clickable { }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "+ Follow",
                            color = TelegramBlueBright,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Optional Options Menu (Shifted category & advanced post settings here)
                    var showPostMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(
                            onClick = { showPostMenu = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = TelegramTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showPostMenu,
                            onDismissRequest = { showPostMenu = false },
                            modifier = Modifier
                                .background(TelegramCardBg)
                                .border(1.dp, TelegramGlassBorder, RoundedCornerShape(12.dp))
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text("Topic / Category", fontSize = 10.sp, color = TelegramTextSecondary)
                                        Text(post.category, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TelegramCyanAccent)
                                    }
                                },
                                onClick = { showPostMenu = false },
                                leadingIcon = {
                                    Icon(Icons.Default.Category, contentDescription = null, tint = TelegramCyanAccent, modifier = Modifier.size(16.dp))
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Not Interested", fontSize = 12.sp, color = TelegramTextPrimary) },
                                onClick = { showPostMenu = false },
                                leadingIcon = {
                                    Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = TelegramTextSecondary, modifier = Modifier.size(16.dp))
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Save to Collection", fontSize = 12.sp, color = TelegramTextPrimary) },
                                onClick = { showPostMenu = false },
                                leadingIcon = {
                                    Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = TelegramTextSecondary, modifier = Modifier.size(16.dp))
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Text Content
            Text(
                text = post.content,
                color = TelegramTextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            if (post.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    post.tags.take(3).forEach { tag ->
                        Text(
                            text = tag,
                            color = TelegramTextLink,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Media Preview
            if (post.mediaUrl != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF131E2A))
                        .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(14.dp))
                ) {
                    AsyncImage(
                        model = post.mediaUrl,
                        contentDescription = "Post Media",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Frosted Video Play overlay badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0x990E1724))
                            .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Reaction Bar & Counters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reactions Display with quick picker toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clickable { showReactionPicker = !showReactionPicker }
                ) {
                    post.reactions.entries.take(3).forEach { (emoji, count) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x331C2938))
                                .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "$emoji $count",
                                color = TelegramTextPrimary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Action Icons (Like, Comment, Share, Save)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable(onClick = onLike)
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) TelegramCoral else TelegramTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = post.likesCount.toString(),
                            color = if (post.isLiked) TelegramCoral else TelegramTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable(onClick = onComment)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comments",
                            tint = TelegramTextSecondary,
                            modifier = Modifier.size(19.dp)
                        )
                        Text(
                            text = post.commentsCount.toString(),
                            color = TelegramTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = TelegramTextSecondary,
                        modifier = Modifier
                            .size(19.dp)
                            .clickable(onClick = onShare)
                    )

                    // Tip Creator Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFFFFD700).copy(alpha = 0.2f),
                                        Color(0xFFFF9100).copy(alpha = 0.2f)
                                    )
                                )
                            )
                            .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .clickable(onClick = onTip)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                            .testTag("tip_creator_button_${post.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Tip Creator",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Tip",
                                color = Color(0xFFFFD700),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Icon(
                        imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (post.isSaved) TelegramBlueBright else TelegramTextSecondary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(onClick = onSave)
                    )
                }
            }

            // Quick Reaction Popup
            if (showReactionPicker) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xEE1A2736))
                        .border(1.dp, TelegramGlassBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf("❤️", "🔥", "🚀", "👏", "💡", "🤯").forEach { emoji ->
                        Text(
                            text = emoji,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .clickable {
                                    onReact(emoji)
                                    showReactionPicker = false
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // Inline Telegram Comment Box
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x3317222E))
                    .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = "Write a comment...",
                    color = TelegramTextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onComment() }
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = TelegramBlue,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onComment() }
                )
            }
        }
    }
}

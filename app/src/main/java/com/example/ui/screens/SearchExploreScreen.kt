package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel

data class ExploreMediaItem(
    val id: String,
    val isVideo: Boolean,
    val mediaUrl: String,
    val title: String,
    val authorHandle: String,
    val viewsOrLikes: String,
    val durationText: String = ""
)

data class SearchableCreatorProfile(
    val id: String,
    val name: String,
    val handle: String,
    val avatar: String,
    val bio: String,
    val followers: String,
    val isVerified: Boolean = true,
    var isFollowing: Boolean = false
)

@Composable
fun SearchExploreScreen(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    modifier: Modifier = Modifier,
    onNavigateToProfile: () -> Unit = { viewModel.selectTab(4) },
    onNavigateToReels: () -> Unit = { viewModel.selectTab(1) },
    onNavigateToHome: () -> Unit = { viewModel.selectTab(0) }
) {
    // Curated Explore media stream of Short Videos (Reels) and Photo/Image Posts
    val exploreMediaItems = remember {
        listOf(
            ExploreMediaItem(
                id = "em1",
                isVideo = true,
                mediaUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500",
                title = "3-Second Hook Retention Formula",
                authorHandle = "@alexvance_fx",
                viewsOrLikes = "1.2M",
                durationText = "0:15"
            ),
            ExploreMediaItem(
                id = "em2",
                isVideo = false,
                mediaUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=500",
                title = "Cinematic Studio Lighting Grade",
                authorHandle = "@elena_cinema",
                viewsOrLikes = "342 likes"
            ),
            ExploreMediaItem(
                id = "em3",
                isVideo = true,
                mediaUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=500",
                title = "1v5 Clutch Tournament Finals",
                authorHandle = "@ghostrider_fps",
                viewsOrLikes = "890K",
                durationText = "0:30"
            ),
            ExploreMediaItem(
                id = "em4",
                isVideo = false,
                mediaUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=500",
                title = "Kyoto Minimalist Creative Morning",
                authorHandle = "@maya_nordic",
                viewsOrLikes = "245 likes"
            ),
            ExploreMediaItem(
                id = "em5",
                isVideo = true,
                mediaUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=500",
                title = "Stripe Escrow Milestone Release",
                authorHandle = "@kaitechtalks",
                viewsOrLikes = "450K",
                durationText = "0:24"
            ),
            ExploreMediaItem(
                id = "em6",
                isVideo = false,
                mediaUrl = "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=500",
                title = "Analog Synth Studio Master Tape",
                authorHandle = "@devon_beats",
                viewsOrLikes = "612 likes"
            ),
            ExploreMediaItem(
                id = "em7",
                isVideo = true,
                mediaUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=500",
                title = "Tokyo Rain Cyberpunk Unreal 5",
                authorHandle = "@aria_art",
                viewsOrLikes = "780K",
                durationText = "0:18"
            ),
            ExploreMediaItem(
                id = "em8",
                isVideo = false,
                mediaUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=500",
                title = "Lighting Keyframe Breakdown",
                authorHandle = "@soravfx",
                viewsOrLikes = "520 likes"
            ),
            ExploreMediaItem(
                id = "em9",
                isVideo = true,
                mediaUrl = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=500",
                title = "4K Color Science Workflow",
                authorHandle = "@elena_cinema",
                viewsOrLikes = "940K",
                durationText = "0:28"
            ),
            ExploreMediaItem(
                id = "em10",
                isVideo = false,
                mediaUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=500",
                title = "Tokyo Neon Portraiture",
                authorHandle = "@aria_art",
                viewsOrLikes = "1.1K likes"
            ),
            ExploreMediaItem(
                id = "em11",
                isVideo = true,
                mediaUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=500",
                title = "Blender 4.2 Lighting Setup",
                authorHandle = "@kane_fx",
                viewsOrLikes = "610K",
                durationText = "0:19"
            ),
            ExploreMediaItem(
                id = "em12",
                isVideo = false,
                mediaUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=500",
                title = "High-Speed AI Architecture Render",
                authorHandle = "@kaitechtalks",
                viewsOrLikes = "890 likes"
            )
        )
    }

    // Comprehensive list of Creators shown when the user searches
    var allCreators by remember {
        mutableStateOf(
            listOf(
                SearchableCreatorProfile(
                    id = "c1",
                    name = "Elena Rostova",
                    handle = "@elena_cinema",
                    avatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300",
                    bio = "Cinema & AI Lighting • Top Hook Creator",
                    followers = "840K followers",
                    isVerified = true,
                    isFollowing = false
                ),
                SearchableCreatorProfile(
                    id = "c2",
                    name = "GhostRider Gaming",
                    handle = "@ghostrider_fps",
                    avatar = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=300",
                    bio = "Esports Clutch Highlights • Tournament Finalist",
                    followers = "1.2M followers",
                    isVerified = true,
                    isFollowing = true
                ),
                SearchableCreatorProfile(
                    id = "c3",
                    name = "Kai Tech Lab",
                    handle = "@kaitechtalks",
                    avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=300",
                    bio = "Creator Economy Tools & Escrow Payouts",
                    followers = "450K followers",
                    isVerified = true,
                    isFollowing = false
                ),
                SearchableCreatorProfile(
                    id = "c4",
                    name = "Alex Vance Studio",
                    handle = "@alexvance_fx",
                    avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
                    bio = "Plutogram Director • 12M+ Retention Repurposing",
                    followers = "1.48M followers",
                    isVerified = true,
                    isFollowing = true
                ),
                SearchableCreatorProfile(
                    id = "c5",
                    name = "Maya Botanical",
                    handle = "@maya_nordic",
                    avatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300",
                    bio = "Kyoto Studio Minimalist • Creative Routine",
                    followers = "210K followers",
                    isVerified = false,
                    isFollowing = false
                ),
                SearchableCreatorProfile(
                    id = "c6",
                    name = "Devon Miles",
                    handle = "@devon_beats",
                    avatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300",
                    bio = "Audio Master & Sound Design Waveforms",
                    followers = "390K followers",
                    isVerified = false,
                    isFollowing = false
                ),
                SearchableCreatorProfile(
                    id = "c7",
                    name = "Aria Chen",
                    handle = "@aria_art",
                    avatar = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=300",
                    bio = "Unreal Engine 5 & 3D Lighting Artist",
                    followers = "620K followers",
                    isVerified = true,
                    isFollowing = false
                ),
                SearchableCreatorProfile(
                    id = "c8",
                    name = "Marcus Kane",
                    handle = "@kane_fx",
                    avatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300",
                    bio = "Video Retention Architect & Hook Breakdown",
                    followers = "980K followers",
                    isVerified = true,
                    isFollowing = false
                )
            )
        )
    }

    val isSearching = uiState.searchQuery.trim().isNotEmpty()

    val filteredCreators = remember(uiState.searchQuery, allCreators) {
        if (!isSearching) emptyList()
        else {
            val q = uiState.searchQuery.trim().lowercase()
            allCreators.filter {
                it.name.lowercase().contains(q) ||
                it.handle.lowercase().contains(q) ||
                it.bio.lowercase().contains(q)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TelegramDarkBg)
            .testTag("search_explore_screen")
    ) {
        // --- 1. Top Search Bar ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (isSearching) TelegramCyanAccent else TelegramTextSecondary
                    )
                },
                placeholder = {
                    Text(
                        text = "Search creators and profiles...",
                        color = TelegramTextMuted,
                        fontSize = 13.sp
                    )
                },
                trailingIcon = {
                    if (isSearching) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = TelegramTextSecondary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TelegramCyanAccent,
                    unfocusedBorderColor = TelegramGlassBorder,
                    focusedContainerColor = Color(0x33172332),
                    unfocusedContainerColor = Color(0x33172332),
                    focusedTextColor = TelegramTextPrimary,
                    unfocusedTextColor = TelegramTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("explore_search_bar")
            )
        }

        // --- 2. Content Switching ---
        if (isSearching) {
            // WHEN SEARCHING: Show ONLY Creator Profiles List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("search_profiles_list"),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "Profiles (${filteredCreators.size})",
                        color = TelegramTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                if (filteredCreators.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.PersonSearch,
                                    contentDescription = null,
                                    tint = TelegramTextSecondary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "No creators found for \"${uiState.searchQuery}\"",
                                    color = TelegramTextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                } else {
                    items(filteredCreators, key = { it.id }) { creator ->
                        CreatorProfileSearchRow(
                            creator = creator,
                            onToggleFollow = {
                                allCreators = allCreators.map {
                                    if (it.id == creator.id) it.copy(isFollowing = !it.isFollowing) else it
                                }
                            },
                            onProfileClick = onNavigateToProfile
                        )
                    }
                }
            }
        } else {
            // WHEN NOT SEARCHING: Show ONLY Short Videos & Images Explore Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("explore_media_grid"),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(exploreMediaItems, key = { it.id }) { item ->
                    ExploreMediaTile(
                        item = item,
                        onClick = {
                            if (item.isVideo) {
                                onNavigateToReels()
                            } else {
                                onNavigateToHome()
                            }
                        }
                    )
                }
            }
        }
    }
}

// --- Instagram-style Explore Tile for Short Video or Photo ---
@Composable
private fun ExploreMediaTile(
    item: ExploreMediaItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(if (item.isVideo) 0.72f else 1f) // Short videos 9:16 taller feel, photos square
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x331C2836))
            .border(0.5.dp, TelegramGlassBorderSubtle, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .testTag("explore_tile_${item.id}")
    ) {
        AsyncImage(
            model = item.mediaUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Top-Right Icon Badge: Only show Reel icon for videos (pure clean Instagram explore grid)
        if (item.isVideo) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color(0x88000000)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Reel",
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

// --- Creator Profile Row shown when searching ---
@Composable
private fun CreatorProfileSearchRow(
    creator: SearchableCreatorProfile,
    onToggleFollow: () -> Unit,
    onProfileClick: () -> Unit
) {
    FrostedGlassBox(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick)
            .testTag("search_profile_${creator.id}"),
        borderColor = TelegramGlassBorderSubtle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Profile Avatar with verified border
            AsyncImage(
                model = creator.avatar,
                contentDescription = creator.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .border(
                        1.5.dp,
                        if (creator.isVerified) TelegramBlueBright else TelegramGlassBorderSubtle,
                        CircleShape
                    )
            )

            // Name, Handle, Bio & Followers
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = creator.name,
                        color = TelegramTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (creator.isVerified) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = TelegramBlueBright,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Text(
                    text = "${creator.handle} • ${creator.followers}",
                    color = TelegramTextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = creator.bio,
                    color = TelegramTextMuted,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Follow / Following Button
            Button(
                onClick = onToggleFollow,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (creator.isFollowing) Color(0x332AABEE) else TelegramBlue
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = if (creator.isFollowing) "Following" else "Follow",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

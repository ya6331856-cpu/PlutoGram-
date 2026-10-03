package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.model.FreelanceService
import com.example.model.PayoutRecord
import com.example.ui.components.CreatorMonetizationHub
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel

enum class InstagramProfileTab {
    GRID_POSTS,
    REELS,
    SERVICES,
    MONETIZATION
}

data class StoryHighlight(
    val title: String,
    val iconEmoji: String,
    val coverUrl: String
)

@Composable
fun ProfileScreen(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(InstagramProfileTab.GRID_POSTS) }
    var showWalletLockDialog by remember { mutableStateOf(false) }
    var showSettingsMenuDialog by remember { mutableStateOf(false) }

    val profile = uiState.profile
    val authState = uiState.authState

    val highlights = remember {
        listOf(
            StoryHighlight("Hooks ⚡", "⚡", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200"),
            StoryHighlight("Reels 🎬", "🎬", "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=200"),
            StoryHighlight("Escrow 💼", "💼", "https://images.unsplash.com/photo-1551836022-d5d88e9218df?w=200"),
            StoryHighlight("Viral 🚀", "🚀", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200"),
            StoryHighlight("Awards 🏆", "🏆", "https://images.unsplash.com/photo-1518770660439-4636190af475?w=200")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TelegramDarkBg)
            .testTag("profile_screen")
    ) {
        // --- Instagram Style Top Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Username with verified badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (authState.isAuthenticated) (authState.email?.substringBefore("@")?.let { "@$it" } ?: profile.handle) else profile.handle,
                    color = TelegramTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (profile.isVerified || authState.isAuthenticated) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = TelegramBlueBright,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Top-right stealth icons: Lock (Payment Escrow Wallet) & Menu (Settings, Rules, Google Auth)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Secure Lock Icon for Payment & Escrow Wallet
                IconButton(
                    onClick = { showWalletLockDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x331C2836))
                        .border(1.dp, TelegramGlassBorderSubtle, CircleShape)
                        .testTag("profile_payment_lock_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Secure Creator Wallet",
                        tint = TelegramCyanAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Hamburger Menu for Settings, Rules & Google Auth
                IconButton(
                    onClick = { showSettingsMenuDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x331C2836))
                        .border(1.dp, TelegramGlassBorderSubtle, CircleShape)
                        .testTag("profile_settings_menu_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Settings & Rules",
                        tint = TelegramTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // --- Main Instagram Profile Feed ---
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Profile Header Info: Avatar + Stats
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar with Story Gradient Ring & Add "+" Badge
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.sweepGradient(
                                            listOf(
                                                TelegramBlueBright,
                                                TelegramCyanAccent,
                                                Color(0xFF8A2BE2),
                                                TelegramBlueBright
                                            )
                                        )
                                    )
                                    .padding(2.5.dp)
                            ) {
                                AsyncImage(
                                    model = authState.photoUrl ?: profile.avatar,
                                    contentDescription = profile.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }

                            // Small Plus Badge
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(TelegramBlueBright)
                                    .border(2.dp, TelegramDarkBg, CircleShape)
                                    .clickable { viewModel.openCameraDialog(true) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Story",
                                    tint = TelegramDarkBg,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        // 3 Stats Columns: Posts, Followers, Following
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            InstagramStatColumn(count = profile.postsCount.toString(), label = "Posts")
                            InstagramStatColumn(count = profile.followersCount, label = "Followers")
                            InstagramStatColumn(count = profile.followingCount, label = "Following")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bio Details
                    Text(
                        text = if (authState.isAuthenticated) authState.displayName else profile.name,
                        color = TelegramTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Digital Creator • AI Video Architect 🎬",
                        color = TelegramTextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = profile.bio,
                        color = TelegramTextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = null,
                            tint = TelegramCyanAccent,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "plutogram.io/alexvance",
                            color = TelegramCyanAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons Row: [ Edit profile ] [ Share profile ]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x33263548)),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                        ) {
                            Text(
                                text = "Edit profile",
                                color = TelegramTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x33263548)),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                        ) {
                            Text(
                                text = "Share profile",
                                color = TelegramTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        IconButton(
                            onClick = { viewModel.toggleCareersPortal(true) },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33263548))
                        ) {
                            Icon(
                                imageVector = Icons.Default.BusinessCenter,
                                contentDescription = "Work With Us",
                                tint = TelegramCyanAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Creator Studio & Analytics Direct Access Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        TelegramBlue.copy(alpha = 0.35f),
                                        TelegramCyanAccent.copy(alpha = 0.25f)
                                    )
                                )
                            )
                            .border(1.dp, TelegramCyanAccent.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.selectTab(2)
                                viewModel.setStudioSubTab(com.example.viewmodel.StudioSubTab.CREATOR_DASHBOARD)
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dashboard,
                                contentDescription = null,
                                tint = TelegramCyanAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Creator Studio & Analytics Dashboard",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = TelegramCyanAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Story Highlights
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(highlights) { hl ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(58.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x441F2E3E))
                                        .border(1.dp, TelegramGlassBorderSubtle, CircleShape)
                                        .padding(3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = hl.coverUrl,
                                        contentDescription = hl.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                }
                                Text(
                                    text = hl.title,
                                    color = TelegramTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Instagram Tab Selector: [ Grid ] [ Reels ] [ Services ]
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 0.5.dp,
                            color = TelegramGlassBorderSubtle,
                            shape = RoundedCornerShape(0.dp)
                        ),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    InstagramTabItem(
                        icon = Icons.Default.GridOn,
                        isSelected = selectedTab == InstagramProfileTab.GRID_POSTS,
                        onClick = { selectedTab = InstagramProfileTab.GRID_POSTS },
                        tag = "tab_profile_grid"
                    )
                    InstagramTabItem(
                        icon = Icons.Default.VideoLibrary,
                        isSelected = selectedTab == InstagramProfileTab.REELS,
                        onClick = { selectedTab = InstagramProfileTab.REELS },
                        tag = "tab_profile_reels"
                    )
                    InstagramTabItem(
                        icon = Icons.Default.WorkOutline,
                        isSelected = selectedTab == InstagramProfileTab.SERVICES,
                        onClick = { selectedTab = InstagramProfileTab.SERVICES },
                        tag = "tab_profile_services"
                    )
                    InstagramTabItem(
                        icon = Icons.Default.MonetizationOn,
                        isSelected = selectedTab == InstagramProfileTab.MONETIZATION,
                        onClick = { selectedTab = InstagramProfileTab.MONETIZATION },
                        tag = "tab_profile_monetization"
                    )
                }
            }

            // Grid Content
            when (selectedTab) {
                InstagramProfileTab.GRID_POSTS -> {
                    item {
                        MediaGridSection(
                            posts = uiState.posts,
                            onPostClick = { }
                        )
                    }
                }
                InstagramProfileTab.REELS -> {
                    item {
                        ReelsGridSection(
                            reels = uiState.reels,
                            onReelClick = { viewModel.selectTab(1) }
                        )
                    }
                }
                InstagramProfileTab.SERVICES -> {
                    item {
                        ServicesListSection(
                            services = profile.services,
                            onHire = { viewModel.openHireService(it) }
                        )
                    }
                }
                InstagramProfileTab.MONETIZATION -> {
                    item {
                        CreatorMonetizationHub(
                            monetization = uiState.monetization,
                            onToggleVideoMonetization = { viewModel.toggleVideoMonetization(it) },
                            onToggleGlobalMonetization = { viewModel.toggleGlobalMonetization(it) },
                            onOpenWithdrawal = { viewModel.openPayoutModal(true) }
                        )
                    }
                }
            }
        }
    }

    // --- Modal 1: Creator Escrow & Payment Wallet Sheet (Opened via Lock 🔒 Icon) ---
    if (showWalletLockDialog) {
        CreatorEscrowWalletDialog(
            profile = profile,
            onDismiss = { showWalletLockDialog = false },
            onConfigureGateways = {
                showWalletLockDialog = false
                viewModel.openPaymentConfig(true)
            }
        )
    }

    // --- Modal 2: Settings, Google Auth & Rules Sheet (Opened via Menu ☰ Icon) ---
    if (showSettingsMenuDialog) {
        ProfileSettingsMenuDialog(
            uiState = uiState,
            viewModel = viewModel,
            onDismiss = { showSettingsMenuDialog = false }
        )
    }
}

@Composable
private fun InstagramStatColumn(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = TelegramTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = TelegramTextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun InstagramTabItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) TelegramBlueBright else TelegramTextMuted,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(2.dp)
                        .background(TelegramBlueBright)
                )
            }
        }
    }
}

@Composable
private fun MediaGridSection(
    posts: List<com.example.model.Post>,
    onPostClick: (com.example.model.Post) -> Unit
) {
    val sampleImages = listOf(
        "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=500",
        "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=500",
        "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=500",
        "https://images.unsplash.com/photo-1518770660439-4636190af475?w=500",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500",
        "https://images.unsplash.com/photo-1551836022-d5d88e9218df?w=500"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        for (i in sampleImages.indices step 3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (j in 0 until 3) {
                    val index = i + j
                    if (index < sampleImages.size) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .background(Color(0xFF14202E))
                                .clickable { }
                        ) {
                            AsyncImage(
                                model = sampleImages[index],
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ReelsGridSection(
    reels: List<com.example.model.Reel>,
    onReelClick: (com.example.model.Reel) -> Unit
) {
    val sampleReels = listOf(
        "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=400" to "42.8K",
        "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400" to "128.5K",
        "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=400" to "89.1K",
        "https://images.unsplash.com/photo-1518770660439-4636190af475?w=400" to "65.4K",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400" to "210.3K",
        "https://images.unsplash.com/photo-1551836022-d5d88e9218df?w=400" to "94.7K"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        for (i in sampleReels.indices step 3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (j in 0 until 3) {
                    val index = i + j
                    if (index < sampleReels.size) {
                        val (cover, views) = sampleReels[index]
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(9f / 16f)
                                .background(Color(0xFF14202E))
                                .clickable { onReelClick(reels.firstOrNull() ?: return@clickable) }
                        ) {
                            AsyncImage(
                                model = cover,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Views overlay at bottom
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = views,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ServicesListSection(
    services: List<FreelanceService>,
    onHire: (FreelanceService) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        services.forEach { service ->
            FrostedGlassBox(
                modifier = Modifier.fillMaxWidth(),
                borderColor = TelegramGlassBorderSubtle
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = service.title,
                            color = TelegramTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${service.price.toInt()}",
                            color = TelegramEmerald,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = service.description,
                        color = TelegramTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

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
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = TelegramTextMuted,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "${service.turnaround} • Escrow Safe",
                                color = TelegramTextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = { onHire(service) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(text = "Hire", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// --- Stealth Modal 1: Creator Escrow & Payment Wallet Sheet (Lock Icon) ---
@Composable
fun CreatorEscrowWalletDialog(
    profile: com.example.model.CreatorProfile,
    onDismiss: () -> Unit,
    onConfigureGateways: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .testTag("escrow_wallet_dialog"),
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
                                .background(TelegramEmerald.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = TelegramEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "Secure Escrow & Wallet",
                            color = TelegramTextPrimary,
                            fontSize = 15.sp,
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

                Spacer(modifier = Modifier.height(16.dp))

                // Balances Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33142232))
                        .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Total Earnings", color = TelegramTextSecondary, fontSize = 11.sp)
                            Text(
                                text = "$${profile.totalEarnings}",
                                color = TelegramEmerald,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Pending Escrow", color = TelegramTextSecondary, fontSize = 11.sp)
                            Text(
                                text = "$${profile.pendingPayout}",
                                color = TelegramAmber,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Gateway Status
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Connected Gateways",
                        color = TelegramTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Stripe Connect", color = TelegramTextPrimary, fontSize = 12.sp)
                        Text(
                            text = if (profile.stripeConnected) "Connected ✓" else "Not Linked",
                            color = if (profile.stripeConnected) TelegramEmerald else TelegramTextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Razorpay Payouts", color = TelegramTextPrimary, fontSize = 12.sp)
                        Text(
                            text = if (profile.razorpayConnected) "Connected ✓" else "Not Linked",
                            color = if (profile.razorpayConnected) TelegramEmerald else TelegramTextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Instant UPI ID", color = TelegramTextPrimary, fontSize = 12.sp)
                        Text(
                            text = if (profile.upiConnected) profile.upiId else "Not Linked",
                            color = if (profile.upiConnected) TelegramEmerald else TelegramTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Configure Gateways Button
                Button(
                    onClick = onConfigureGateways,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Configure Payment Gateways", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- Stealth Modal 2: Settings, Google Auth & Rules Sheet (Menu Icon) ---
@Composable
fun ProfileSettingsMenuDialog(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .testTag("settings_menu_dialog"),
            borderColor = TelegramBlueBright
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
                    Text(
                        text = "Settings & Privacy",
                        color = TelegramTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TelegramTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Item 1: Google Account & Firebase Cloud Sync
                SettingsMenuRow(
                    icon = Icons.Default.CloudSync,
                    title = "Google Account & Cloud Sync",
                    subtitle = if (uiState.authState.isAuthenticated) "Signed in as ${uiState.authState.displayName}" else "Sign in with Google to sync Firestore",
                    badge = if (uiState.authState.isAuthenticated) "Active ✓" else null,
                    badgeColor = TelegramEmerald,
                    onClick = {
                        onDismiss()
                        viewModel.openAuthDialog(true)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Item 2: Telegram Rules & Community Regulations
                SettingsMenuRow(
                    icon = Icons.Default.Gavel,
                    title = "Rules & Regulations",
                    subtitle = "Telegram standard zero-spam, content safety & DMCA",
                    badge = if (uiState.hasAcceptedTerms) "Accepted ✓" else "Review",
                    badgeColor = if (uiState.hasAcceptedTerms) TelegramEmerald else TelegramAmber,
                    onClick = {
                        onDismiss()
                        viewModel.openTermsDialog(true)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Item 3: End-to-End Privacy & Secret Chats
                SettingsMenuRow(
                    icon = Icons.Default.Security,
                    title = "Privacy & Encryption",
                    subtitle = "Zero behavioral tracking, end-to-end secret keys",
                    badge = "Encrypted",
                    badgeColor = TelegramCyanAccent,
                    onClick = { }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Item 4: Self-Destruct / Purge Account
                SettingsMenuRow(
                    icon = Icons.Default.DeleteForever,
                    title = "Account Self-Destruct & Data Purge",
                    subtitle = "Permanently purge account records from Firestore",
                    badge = null,
                    badgeColor = TelegramCoral,
                    onClick = {
                        viewModel.signOut()
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsMenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    badge: String?,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x33162231))
            .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x332AABEE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TelegramCyanAccent,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        color = TelegramTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (badge != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(badgeColor.copy(alpha = 0.2f))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badge,
                                color = badgeColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
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
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

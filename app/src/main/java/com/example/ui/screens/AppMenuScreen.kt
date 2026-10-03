package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.model.NetworkBandwidthState
import com.example.model.VideoQualityPreset
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel

/**
 * Facebook & Instagram style "Menu" drawer / page.
 * Shifts secondary, rarely used administrative tools (Careers portal,
 * Payment escrow config, Rules & regulations, Google Cloud sync, Help & support,
 * and Settings) into a single organized, uncluttered hub.
 */
@Composable
fun AppMenuScreen(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    onDismiss: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onOpenSearch: () -> Unit
) {
    var isHelpExpanded by remember { mutableStateOf(false) }
    var isSettingsExpanded by remember { mutableStateOf(false) }
    var showTranscribeDialog by remember { mutableStateOf(false) }
    var showThumbnailDialog by remember { mutableStateOf(false) }

    val profile = uiState.profile
    val authState = uiState.authState

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(TelegramDarkBg)
                .statusBarsPadding()
                .navigationBarsPadding()
                .testTag("app_menu_screen")
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // --- Top Header: "Menu" with Search & Close ---
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Menu",
                            color = TelegramTextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    onDismiss()
                                    onOpenSearch()
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x331C2836))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = TelegramTextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x331C2836))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = TelegramTextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // --- User Account Profile Card ---
                item {
                    FrostedGlassBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("menu_profile_shortcut"),
                        borderColor = TelegramGlassBorderSubtle
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onDismiss()
                                    onNavigateToProfile()
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AsyncImage(
                                model = authState.photoUrl ?: profile.avatar,
                                contentDescription = profile.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, TelegramBlueBright, CircleShape)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (authState.isAuthenticated) authState.displayName else profile.name,
                                        color = TelegramTextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (profile.isVerified || authState.isAuthenticated) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Verified",
                                            tint = TelegramBlueBright,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = if (authState.isAuthenticated) (authState.email ?: profile.handle) else "View your profile • Tap to switch",
                                    color = TelegramTextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = TelegramTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // --- Shortcuts Title ---
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "All shortcuts",
                        color = TelegramTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // --- 2-Column Shortcuts Grid (All shifted secondary tools) ---
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Row 1: Careers Portal & Escrow Payment Wallet
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MenuShortcutCard(
                                title = "Careers & Talent",
                                subtitle = "Escrow hiring & jobs",
                                icon = Icons.Default.BusinessCenter,
                                iconTint = TelegramCyanAccent,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onDismiss()
                                    viewModel.toggleCareersPortal(true)
                                },
                                tag = "menu_shortcut_careers"
                            )

                            MenuShortcutCard(
                                title = "Payment & Escrow",
                                subtitle = "Stripe, Razorpay, UPI",
                                icon = Icons.Default.Lock,
                                iconTint = TelegramEmerald,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onDismiss()
                                    viewModel.openPaymentConfig(true)
                                },
                                tag = "menu_shortcut_payment"
                            )
                        }

                        // Row 2: Rules & Regulations & Google Cloud Sync
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MenuShortcutCard(
                                title = "Rules & Terms",
                                subtitle = "Anti-spam & standards",
                                icon = Icons.Default.Gavel,
                                iconTint = TelegramAmber,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onDismiss()
                                    viewModel.openTermsDialog(true)
                                },
                                tag = "menu_shortcut_rules"
                            )

                            MenuShortcutCard(
                                title = "Google Cloud Sync",
                                subtitle = if (authState.isAuthenticated) "Connected ✓" else "Sign in with Google",
                                icon = Icons.Default.CloudSync,
                                iconTint = TelegramBlueBright,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onDismiss()
                                    viewModel.openAuthDialog(true)
                                },
                                tag = "menu_shortcut_auth"
                            )
                        }

                        // Row 3: Live CameraX Recorder & AI Hook Studio
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MenuShortcutCard(
                                title = "Camera Recorder",
                                subtitle = "9:16 Reels & Stories",
                                icon = Icons.Default.Videocam,
                                iconTint = TelegramCoral,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onDismiss()
                                    viewModel.openCameraDialog(true)
                                },
                                tag = "menu_shortcut_camera"
                            )

                            MenuShortcutCard(
                                title = "AI Hook Studio",
                                subtitle = "Extract viral 3s hooks",
                                icon = Icons.Default.AutoAwesome,
                                iconTint = TelegramCyanAccent,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onDismiss()
                                    viewModel.selectTab(2)
                                },
                                tag = "menu_shortcut_studio"
                            )
                        }

                        // Row 4: Voice Transcriber (gemini-3.5-transcribe) & AI Thumbnail Studio (gemini-3.1-flash-image-preview)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MenuShortcutCard(
                                title = "Voice Transcriber",
                                subtitle = "gemini-3.5-transcribe",
                                icon = Icons.Default.Mic,
                                iconTint = TelegramAmber,
                                modifier = Modifier.weight(1f),
                                onClick = { showTranscribeDialog = true },
                                tag = "menu_shortcut_transcribe"
                            )

                            MenuShortcutCard(
                                title = "AI Thumbnail Studio",
                                subtitle = "gemini-3.1-flash-image",
                                icon = Icons.Default.Image,
                                iconTint = TelegramEmerald,
                                modifier = Modifier.weight(1f),
                                onClick = { showThumbnailDialog = true },
                                tag = "menu_shortcut_thumbnail"
                            )
                        }

                        // Row 5: Creator Studio Dashboard & Live Analytics
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MenuShortcutCard(
                                title = "Creator Studio Dashboard",
                                subtitle = "Live 48h Views & Audience Analytics",
                                icon = Icons.Default.Dashboard,
                                iconTint = TelegramCyanAccent,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onDismiss()
                                    viewModel.selectTab(2)
                                    viewModel.setStudioSubTab(com.example.viewmodel.StudioSubTab.CREATOR_DASHBOARD)
                                },
                                tag = "menu_shortcut_dashboard"
                            )
                        }
                    }
                }

                // --- Creator Monetization & Partner Earnings Card ---
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    FrostedGlassBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onNavigateToProfile()
                            }
                            .testTag("menu_monetization_card"),
                        borderColor = TelegramEmerald
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(TelegramEmerald.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = null,
                                        tint = TelegramEmerald,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Creator Earnings & Partner Hub",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "$${String.format(java.util.Locale.US, "%,.2f", uiState.monetization.availableBalance)} available • 70% Ad Split",
                                        color = TelegramEmerald,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = TelegramTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // --- Shifted: AI Feed Personalization & Intent Algorithm Status ---
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    FrostedGlassBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("menu_feed_algorithm_card"),
                        borderColor = TelegramBlueBright
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(TelegramCyanAccent.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = TelegramCyanAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Feed Personalization Algorithm",
                                        color = TelegramTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = uiState.recommendationNotice ?: "⚡ Intent Algorithm: Feed tailored for Tech & Viral Hooks",
                                        color = TelegramCyanAccent,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // --- Shifted & Advanced: Strict Ultra-HD Video Quality Policy ---
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    FrostedGlassBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("menu_video_quality_card"),
                        borderColor = TelegramCyanAccent
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(TelegramCyanAccent.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.HighQuality,
                                        contentDescription = null,
                                        tint = TelegramCyanAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Strict Ultra-HD Quality Policy",
                                        color = TelegramTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Zero Quality Drop • Displays in Native Resolution",
                                        color = TelegramTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Strict Mode Switch Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x331C2938))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Never Degrade Video Quality",
                                        color = TelegramTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "If internet is slow, pause & buffer in Full HD instead of dropping to blurry 240p/360p",
                                        color = TelegramTextSecondary,
                                        fontSize = 10.sp,
                                        lineHeight = 13.sp
                                    )
                                }
                                Switch(
                                    checked = uiState.isStrictQualityMode,
                                    onCheckedChange = { viewModel.setStrictQualityMode(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = TelegramCyanAccent
                                    ),
                                    modifier = Modifier.scale(0.85f)
                                )
                            }

                            // Quality Presets
                            Text(
                                text = "Selected Stream Resolution:",
                                color = TelegramTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                VideoQualityPreset.entries.forEach { preset ->
                                    val isSelected = uiState.selectedQualityPreset == preset
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) TelegramBlueBright.copy(alpha = 0.25f) else Color(0x221C2938))
                                            .border(1.dp, if (isSelected) TelegramCyanAccent else TelegramGlassBorderSubtle, RoundedCornerShape(8.dp))
                                            .clickable { viewModel.setQualityPreset(preset) }
                                            .padding(vertical = 8.dp, horizontal = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = preset.badge,
                                            color = if (isSelected) TelegramCyanAccent else TelegramTextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }

                            // Network Status & Simulation Test Button
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
                                            .background(
                                                if (uiState.networkBandwidth == NetworkBandwidthState.SLOW_NETWORK)
                                                    TelegramCoral
                                                else
                                                    TelegramEmerald
                                            )
                                    )
                                    Text(
                                        text = "${uiState.networkBandwidth.title} (${uiState.simulatedSpeedMbps} Mbps)",
                                        color = TelegramTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }

                                TextButton(
                                    onClick = { viewModel.simulateSlowNetworkToggle() },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (uiState.networkBandwidth == NetworkBandwidthState.SLOW_NETWORK) "Restore 5G" else "Test Slow Network",
                                        color = TelegramCyanAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // --- Expandable Section 1: Help & support ---
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    MenuAccordionItem(
                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                        title = "Help and support",
                        isExpanded = isHelpExpanded,
                        onToggle = { isHelpExpanded = !isHelpExpanded }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MenuSubItem(title = "Plutogram Support Center", icon = Icons.Default.SupportAgent) { }
                            MenuSubItem(title = "Community Standards & Guidelines", icon = Icons.Default.Policy) {
                                viewModel.openTermsDialog(true)
                            }
                            MenuSubItem(title = "Report a Problem", icon = Icons.Default.ReportProblem) { }
                        }
                    }
                }

                // --- Expandable Section 2: Settings and privacy ---
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    MenuAccordionItem(
                        icon = Icons.Default.Settings,
                        title = "Settings and privacy",
                        isExpanded = isSettingsExpanded,
                        onToggle = { isSettingsExpanded = !isSettingsExpanded }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MenuSubItem(
                                title = "Account Center & Cloud Sync",
                                icon = Icons.Default.ManageAccounts
                            ) {
                                viewModel.openAuthDialog(true)
                            }
                            MenuSubItem(
                                title = "Payment & Payout Gateways",
                                icon = Icons.Default.Payment
                            ) {
                                viewModel.openPaymentConfig(true)
                            }
                            MenuSubItem(title = "Privacy Checkup & Encryption", icon = Icons.Default.Security) { }
                            MenuSubItem(title = "Dark Mode (Always Active)", icon = Icons.Default.DarkMode) { }
                            MenuSubItem(title = "Language (English • Hindi • Global)", icon = Icons.Default.Language) { }
                        }
                    }
                }

                // --- Bottom Log Out / Sign In Button ---
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            if (authState.isAuthenticated) {
                                viewModel.signOut()
                            } else {
                                onDismiss()
                                viewModel.openAuthDialog(true)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (authState.isAuthenticated) Color(0x33263548) else TelegramBlue
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("menu_logout_button")
                    ) {
                        Icon(
                            imageVector = if (authState.isAuthenticated) Icons.AutoMirrored.Filled.ExitToApp else Icons.AutoMirrored.Filled.Login,
                            contentDescription = null,
                            tint = if (authState.isAuthenticated) TelegramCoral else Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (authState.isAuthenticated) "Log Out (${authState.displayName})" else "Sign In with Google",
                            color = if (authState.isAuthenticated) TelegramCoral else Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (showTranscribeDialog) {
        AudioTranscribeDialog(onDismiss = { showTranscribeDialog = false })
    }

    if (showThumbnailDialog) {
        ThumbnailGeneratorDialog(
            currentTitle = "Plutogram Creator Masterclass",
            onDismiss = { showThumbnailDialog = false }
        )
    }
}

@Composable
private fun MenuShortcutCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0x33172433))
            .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag(tag)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color = TelegramTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TelegramTextSecondary,
                fontSize = 10.sp,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
private fun MenuAccordionItem(
    icon: ImageVector,
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    FrostedGlassBox(
        modifier = Modifier.fillMaxWidth(),
        borderColor = TelegramGlassBorderSubtle
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = TelegramCyanAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = title,
                        color = TelegramTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = TelegramTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = TelegramGlassBorderSubtle, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    content()
                }
            }
        }
    }
}

@Composable
private fun MenuSubItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TelegramTextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = title,
                color = TelegramTextPrimary,
                fontSize = 12.sp
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

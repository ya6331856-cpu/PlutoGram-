package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.SubscriptionTier
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun SubscriptionTiersDialog(
    isOpen: Boolean,
    currentTierId: String,
    tiers: List<SubscriptionTier>,
    onSelectTier: (SubscriptionTier, isYearly: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val coroutineScope = rememberCoroutineScope()
    var isYearly by remember { mutableStateOf(false) }
    var selectedTier by remember { mutableStateOf(tiers.firstOrNull { it.isPopular } ?: tiers[1]) }
    var isProcessing by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {
            if (!isProcessing) onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xDD080E17))
                .clickable { if (!isProcessing) onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            FrostedGlassBox(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.88f)
                    .clickable(enabled = false) {}
                    .testTag("subscription_tiers_dialog"),
                borderColor = Color(0xFFFFD700)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Creator VIP Memberships",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Icon(Icons.Default.Stars, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(18.dp))
                            }
                            Text(
                                text = "Unlock AI Video Enhancements, 4K Upscale & Collab Perks",
                                color = TelegramTextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TelegramTextSecondary)
                        }
                    }

                    // Billing Period Toggle (Monthly vs Yearly -20%)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x331C2938))
                            .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (!isYearly) TelegramBlue.copy(alpha = 0.5f) else Color.Transparent)
                                .clickable { isYearly = false }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Monthly Billing",
                                color = if (!isYearly) Color.White else TelegramTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isYearly) TelegramEmerald.copy(alpha = 0.35f) else Color.Transparent)
                                .clickable { isYearly = true }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Yearly Billing",
                                    color = if (isYearly) TelegramEmerald else TelegramTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(TelegramEmerald)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("SAVE 20%", color = TelegramDarkBg, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }

                    // Tiers List
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(tiers) { tier ->
                            val isSelected = selectedTier.id == tier.id
                            val isCurrent = tier.id == currentTierId
                            val price = if (isYearly) tier.priceYearly else tier.priceMonthly

                            FrostedGlassBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedTier = tier },
                                borderColor = if (isSelected) Color(tier.badgeColorHex) else TelegramGlassBorderSubtle
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
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
                                            Text(tier.badgeEmoji, fontSize = 20.sp)
                                            Column {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Text(
                                                        text = tier.name,
                                                        color = Color.White,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    if (tier.isPopular) {
                                                        Box(
                                                            modifier = Modifier
                                                                .clip(RoundedCornerShape(4.dp))
                                                                .background(Color(0xFFFFD700))
                                                                .padding(horizontal = 5.dp, vertical = 1.dp)
                                                        ) {
                                                            Text("POPULAR", color = TelegramDarkBg, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold)
                                                        }
                                                    }
                                                    if (isCurrent) {
                                                        Box(
                                                            modifier = Modifier
                                                                .clip(RoundedCornerShape(4.dp))
                                                                .background(TelegramEmerald.copy(alpha = 0.25f))
                                                                .border(1.dp, TelegramEmerald, RoundedCornerShape(4.dp))
                                                                .padding(horizontal = 5.dp, vertical = 1.dp)
                                                        ) {
                                                            Text("ACTIVE", color = TelegramEmerald, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                                        }
                                                    }
                                                }
                                                Text(tier.tagLine, color = TelegramTextSecondary, fontSize = 10.sp)
                                            }
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = if (price == 0.0) "Free" else "$${String.format(Locale.US, "%.2f", price)}",
                                                color = Color(tier.badgeColorHex),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontFamily = FontFamily.Monospace
                                            )
                                            if (price > 0.0) {
                                                Text(
                                                    text = if (isYearly) "/year" else "/month",
                                                    color = TelegramTextSecondary,
                                                    fontSize = 9.sp
                                                )
                                            }
                                        }
                                    }

                                    // Features checklist
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        tier.features.forEach { feat ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color(tier.badgeColorHex),
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Text(feat, color = TelegramTextPrimary, fontSize = 10.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Action Button or Processing State
                    AnimatedContent(
                        targetState = when {
                            isSuccess -> "SUCCESS"
                            isProcessing -> "PROCESSING"
                            else -> "READY"
                        },
                        label = "sub_state"
                    ) { state ->
                        when (state) {
                            "PROCESSING" -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(TelegramBlue.copy(alpha = 0.3f)),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(color = TelegramCyanAccent, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Activating ${selectedTier.name} Subscription...", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            "SUCCESS" -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(TelegramEmerald.copy(alpha = 0.3f))
                                        .border(1.dp, TelegramEmerald, RoundedCornerShape(12.dp)),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TelegramEmerald, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Tier Upgraded! Welcome to ${selectedTier.name} 🎉", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            else -> {
                                Button(
                                    onClick = {
                                        isProcessing = true
                                        coroutineScope.launch {
                                            delay(1300)
                                            isProcessing = false
                                            isSuccess = true
                                            onSelectTier(selectedTier, isYearly)
                                            delay(1200)
                                            onDismiss()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("subscribe_tier_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(selectedTier.badgeColorHex)
                                    )
                                ) {
                                    val price = if (isYearly) selectedTier.priceYearly else selectedTier.priceMonthly
                                    Text(
                                        text = if (selectedTier.id == currentTierId) "Keep Current Tier"
                                        else if (price == 0.0) "Downgrade to Free"
                                        else "Join ${selectedTier.name} • $${String.format(Locale.US, "%.2f", price)}${if (isYearly) "/yr" else "/mo"}",
                                        color = TelegramDarkBg,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

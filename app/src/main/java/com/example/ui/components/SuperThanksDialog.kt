package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SampleData
import com.example.model.SuperThanksGiftTier
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun SuperThanksDialog(
    isOpen: Boolean,
    videoTitle: String,
    creatorName: String,
    selectedTier: SuperThanksGiftTier,
    onSelectTier: (SuperThanksGiftTier) -> Unit,
    onSendTip: (SuperThanksGiftTier, String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    var customMessage by remember { mutableStateOf("Awesome hook! Keep creating 🔥") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xBB080E17))
                .clickable { onDismiss() }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            FrostedGlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp)
                    .clickable(enabled = false) { }
                    .testTag("super_thanks_dialog"),
                borderColor = TelegramCyanAccent
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Row
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFFFFD700), Color(0xFFFF9100))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stars,
                                    contentDescription = null,
                                    tint = TelegramDarkBg,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Send Super Thanks",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Support $creatorName directly",
                                    color = TelegramCyanAccent,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TelegramTextSecondary
                            )
                        }
                    }

                    // Explanatory note
                    Text(
                        text = "100% of Stars and tips go directly to the creator's wallet. Your comment will be pinned and highlighted with a golden badge!",
                        color = TelegramTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        textAlign = TextAlign.Center
                    )

                    // Gift Tiers Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SampleData.superThanksGiftTiers.forEach { tier ->
                            val isSelected = tier.id == selectedTier.id
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected)
                                            TelegramBlueBright.copy(alpha = 0.25f)
                                        else
                                            Color(0x331C2938)
                                    )
                                    .border(
                                        1.5.dp,
                                        if (isSelected) TelegramCyanAccent else TelegramGlassBorderSubtle,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onSelectTier(tier) }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = tier.iconEmoji,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = "${tier.stars} Stars",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "$${String.format(Locale.US, "%.2f", tier.usdAmount)}",
                                        color = TelegramCyanAccent,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    // Pinned Highlighted Message Preview
                    OutlinedTextField(
                        value = customMessage,
                        onValueChange = { customMessage = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Write a highlighted comment...", color = TelegramTextSecondary, fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TelegramCyanAccent,
                            unfocusedBorderColor = TelegramGlassBorderSubtle,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = TelegramTextPrimary,
                            focusedContainerColor = Color(0x221C2938),
                            unfocusedContainerColor = Color(0x221C2938)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = false,
                        maxLines = 3
                    )

                    // Send Action Button
                    Button(
                        onClick = { onSendTip(selectedTier, customMessage) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("send_super_thanks_confirm_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TelegramBlueBright)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = TelegramDarkBg,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Send ${selectedTier.stars} Stars ($${String.format(Locale.US, "%.2f", selectedTier.usdAmount)})",
                                color = TelegramDarkBg,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

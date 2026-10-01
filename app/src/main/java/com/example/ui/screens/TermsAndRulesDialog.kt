package com.example.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*

data class RuleSection(
    val title: String,
    val icon: ImageVector,
    val points: List<String>
)

@Composable
fun TermsAndRulesDialog(
    isAccepted: Boolean,
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    var hasAgreedToTerms by remember { mutableStateOf(isAccepted) }

    val rules = remember {
        listOf(
            RuleSection(
                title = "1. Zero-Tolerance Anti-Spam & Anti-Fraud",
                icon = Icons.Default.Security,
                points = listOf(
                    "You must not use Plutogram to send unsolicited ads, bulk promotional spam, or automated bot messages to users.",
                    "Scams, pyramid schemes, fake giveaways, and phishing links will result in immediate permanent account termination without appeal.",
                    "Impersonating another creator, agency, or Plutogram staff member is strictly prohibited."
                )
            ),
            RuleSection(
                title = "2. Creator Safety & Prohibited Content",
                icon = Icons.Default.Warning,
                points = listOf(
                    "Strictly no content promoting violence, terror groups, hate speech, harassment, or illegal substances.",
                    "Explicit, non-consensual media or pornography is completely banned.",
                    "Harmful misinformation or coordinated manipulation of creator metrics is prohibited."
                )
            ),
            RuleSection(
                title = "3. Intellectual Property & Copyright",
                icon = Icons.Default.Copyright,
                points = listOf(
                    "You retain 100% intellectual property ownership of the videos, audio, and reels you publish.",
                    "Re-uploading copyrighted videos or audio without creator permission is subject to DMCA takedown.",
                    "AI Studio hook extractions are licensed strictly for the author's commercial or creative use."
                )
            ),
            RuleSection(
                title = "4. Telegram-Grade Privacy & Data Protection",
                icon = Icons.Default.Lock,
                points = listOf(
                    "No ad trackers: We never profile you for behavioral targeted advertisements or sell personal data.",
                    "Messages in secret chats use end-to-end encryption. Only sender and recipient hold the decryption keys.",
                    "Cloud chats are synchronized via Google Cloud Firestore with multi-datacenter security redundancy."
                )
            ),
            RuleSection(
                title = "5. Freelance Marketplace & Escrow Safety",
                icon = Icons.Default.MonetizationOn,
                points = listOf(
                    "All client payments are held in an automated 2-stage escrow deposit until deliverables are verified.",
                    "50% upfront deposit is released upon milestone review; the remaining 50% on final 4K delivery.",
                    "Disputed orders are arbitrated by Plutogram Talent Reviewers within 48 hours."
                )
            ),
            RuleSection(
                title = "6. Self-Destruct & Complete Account Deletion",
                icon = Icons.Default.DeleteForever,
                points = listOf(
                    "You have the right to permanently delete your account and all associated Firestore cloud records at any time.",
                    "Once deleted, all reels, messages, and wallet logs are permanently purged from Plutogram servers."
                )
            )
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(vertical = 12.dp)
                .testTag("terms_and_rules_dialog"),
            borderColor = TelegramCyanAccent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(TelegramBlue.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = null,
                                tint = TelegramCyanAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Rules & Terms of Service",
                                color = TelegramTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Telegram Standard Community Policy",
                                color = TelegramCyanAccent,
                                fontSize = 11.sp
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

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Welcome to Plutogram. Our principles are modeled directly on privacy, creator freedom, and zero-spam safety.",
                    color = TelegramTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Rules List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(rules) { section ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x33162231))
                                .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = section.icon,
                                        contentDescription = null,
                                        tint = TelegramCyanAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = section.title,
                                        color = TelegramTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                section.points.forEach { point ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = "• ",
                                            color = TelegramCyanAccent,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = point,
                                            color = TelegramTextSecondary,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Agreement checkbox row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { hasAgreedToTerms = !hasAgreedToTerms }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = hasAgreedToTerms,
                        onCheckedChange = { hasAgreedToTerms = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = TelegramCyanAccent,
                            checkmarkColor = Color(0xFF0D1520)
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "I agree to Plutogram Rules & Privacy Regulations",
                        color = TelegramTextPrimary,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Confirm button
                Button(
                    onClick = {
                        onAccept()
                        onDismiss()
                    },
                    enabled = hasAgreedToTerms,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TelegramBlue,
                        disabledContainerColor = Color(0x332AABEE)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("accept_terms_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (hasAgreedToTerms) "Accept & Continue" else "Review & Agree to Rules",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.JobOpening
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel

@Composable
fun CareersPortalDialog(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    onDismiss: () -> Unit
) {
    var selectedJobForApply by remember { mutableStateOf<JobOpening?>(null) }
    var applicantName by remember { mutableStateOf("Alex Vance") }
    var applicantEmail by remember { mutableStateOf("alexvance.creator@gmail.com") }
    var portfolioUrl by remember { mutableStateOf("https://telepulse.me/@alexvance_fx") }
    var pitch by remember { mutableStateOf("Generated 40M+ views across reels using custom Gemini hook prompts and sound design cadence.") }
    var expectedRate by remember { mutableStateOf("$95K / yr or $65/hr") }
    var showSuccessBanner by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        FrostedGlassBox(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .testTag("careers_portal_dialog"),
            borderColor = TelegramCyanAccent
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BusinessCenter,
                                contentDescription = null,
                                tint = TelegramCyanAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Work With Us",
                                color = TelegramTextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Plutogram Creator Talent Scout Portal",
                            color = TelegramTextSecondary,
                            fontSize = 11.sp
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

                Spacer(modifier = Modifier.height(10.dp))

                if (showSuccessBanner) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(TelegramEmerald.copy(alpha = 0.2f))
                            .border(1.dp, TelegramEmerald, RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = TelegramEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Application submitted! Our lead scout will review your portfolio within 24h.",
                                color = TelegramTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // If applying for a specific job:
                if (selectedJobForApply != null) {
                    val job = selectedJobForApply!!
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        item {
                            Text(
                                text = "Applying for: ${job.title}",
                                color = TelegramCyanAccent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${job.department} • ${job.compensation}",
                                color = TelegramTextSecondary,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(text = "Your Full Name:", color = TelegramTextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = applicantName,
                                onValueChange = { applicantName = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Contact Email:", color = TelegramTextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = applicantEmail,
                                onValueChange = { applicantEmail = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Portfolio / Reel Link:", color = TelegramTextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = portfolioUrl,
                                onValueChange = { portfolioUrl = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Why You? (Hook / Video Experience):", color = TelegramTextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = pitch,
                                onValueChange = { pitch = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Expected Compensation / Rate:", color = TelegramTextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = expectedRate,
                                onValueChange = { expectedRate = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { selectedJobForApply = null },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Back", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = {
                                        viewModel.submitCareerApplication(
                                            job.title,
                                            applicantName,
                                            applicantEmail,
                                            portfolioUrl,
                                            pitch,
                                            expectedRate
                                        )
                                        showSuccessBanner = true
                                        selectedJobForApply = null
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .testTag("submit_job_application_btn")
                                ) {
                                    Text("Submit Application", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    // Open Roles List
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(uiState.jobOpenings, key = { it.id }) { job ->
                            JobOpeningCard(
                                job = job,
                                onApply = { selectedJobForApply = job }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JobOpeningCard(
    job: JobOpening,
    onApply: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x33142232))
            .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.title,
                    color = TelegramTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (job.isUrgent) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(TelegramAmber.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "HOT SCOUT",
                            color = TelegramAmber,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${job.department} • ${job.location} • ${job.compensation}",
                color = TelegramCyanAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = job.description,
                color = TelegramTextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onApply,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TelegramBlue),
                    modifier = Modifier.testTag("apply_job_${job.id}")
                ) {
                    Text(
                        text = "Apply / Scout Me",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

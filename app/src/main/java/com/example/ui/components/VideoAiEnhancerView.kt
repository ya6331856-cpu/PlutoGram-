package com.example.ui.components

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VideoAiEnhancerView(
    jobs: List<VideoEnhancementJob>,
    geminiOptimization: GeminiScriptOptimization?,
    isGeminiOptimizing: Boolean,
    onRunGeminiScriptDoctor: (String) -> Unit,
    onStartEnhancementJob: (VideoAiToolType, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedTool by remember { mutableStateOf(VideoAiToolType.UPSCALE_4K) }

    // 4K Upscaler Settings
    var targetResolution by remember { mutableStateOf("4K HDR 60fps") }
    var selectedLut by remember { mutableStateOf("Cyberpunk Teal & Orange") }
    var denoiseStrength by remember { mutableFloatStateOf(0.85f) }
    var showBeforeAfterSplit by remember { mutableStateOf(false) }

    // Subtitle Settings
    var subtitleStyle by remember { mutableStateOf("Hormozi Electric Cyan") }
    var soundFxSync by remember { mutableStateOf(true) }

    // Audio Cleaner Settings
    var audioPreset by remember { mutableStateOf("Studio Condenser 96kHz") }
    var bgDuckingPct by remember { mutableFloatStateOf(0.75f) }

    // Gemini Script Doctor
    var scriptInputText by remember { mutableStateOf("How to scale video production with AI without losing human storytelling touch") }

    // Job Execution State
    var isProcessingJob by remember { mutableStateOf(false) }
    var jobProgress by remember { mutableFloatStateOf(0f) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("video_ai_enhancer_view"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. AI Studio Banner ---
        item {
            FrostedGlassBox(
                modifier = Modifier.fillMaxWidth(),
                borderColor = TelegramCyanAccent
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(listOf(TelegramBlueBright, TelegramCyanAccent))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TelegramDarkBg, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text("Video AI Neural Lab", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                                Text("Powered by Gemini 3.5 Flash & Neural Processing", color = TelegramTextSecondary, fontSize = 10.sp)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TelegramEmerald.copy(alpha = 0.2f))
                                .border(1.dp, TelegramEmerald, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("GPU Acceleration ON", color = TelegramEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- 2. Tool Switcher Tabs ---
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(VideoAiToolType.values()) { tool ->
                    val isSelected = selectedTool == tool
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(TelegramBlue.copy(alpha = 0.5f), TelegramCyanAccent.copy(alpha = 0.4f)))
                                else Brush.linearGradient(listOf(Color(0x331C2938), Color(0x221C2938)))
                            )
                            .border(1.dp, if (isSelected) TelegramCyanAccent else TelegramGlassBorderSubtle, RoundedCornerShape(10.dp))
                            .clickable { selectedTool = tool }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(tool.iconEmoji, fontSize = 14.sp)
                            Text(
                                text = tool.title,
                                color = if (isSelected) Color.White else TelegramTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // --- 3. Dynamic Tool Config & Preview Panel ---
        item {
            FrostedGlassBox(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Color(0x3300E5FF)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    when (selectedTool) {
                        VideoAiToolType.UPSCALE_4K -> {
                            Text("4K Neural Upscaler & HDR Engine", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                            // Preview Card with Before/After Toggle
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0F1722))
                                    .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp))
                            ) {
                                AsyncImage(
                                    model = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800",
                                    contentDescription = "Preview",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Before / After Badge
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xCC000000))
                                        .clickable { showBeforeAfterSplit = !showBeforeAfterSplit }
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Compare, contentDescription = null, tint = TelegramCyanAccent, modifier = Modifier.size(12.dp))
                                    Text(if (showBeforeAfterSplit) "Mode: 4K HDR Neural Active" else "Tap: Toggle Before/After", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xAA0E1724))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("Resolution: $targetResolution • LUT: $selectedLut", color = TelegramCyanAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Resolution Pills
                            Text("Output Quality", color = TelegramTextSecondary, fontSize = 11.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("4K HDR 60fps", "4K Ultra-Sharp", "2K 120fps").forEach { res ->
                                    val isSel = targetResolution == res
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) TelegramBlue.copy(alpha = 0.4f) else Color(0x221C2938))
                                            .border(1.dp, if (isSel) TelegramCyanAccent else TelegramGlassBorderSubtle, RoundedCornerShape(8.dp))
                                        .clickable { targetResolution = res }
                                        .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(res, color = if (isSel) Color.White else TelegramTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Denoise Slider
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Neural Denoise & Texture Detail", color = TelegramTextSecondary, fontSize = 11.sp)
                                    Text("${(denoiseStrength * 100).toInt()}%", color = TelegramCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Slider(
                                    value = denoiseStrength,
                                    onValueChange = { denoiseStrength = it },
                                    colors = SliderDefaults.colors(
                                        thumbColor = TelegramCyanAccent,
                                        activeTrackColor = TelegramCyanAccent
                                    )
                                )
                            }
                        }

                        VideoAiToolType.KINETIC_SUBTITLES -> {
                            Text("Kinetic Dynamic Subtitle Engine", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                            // Subtitle Mock Animation Card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0F1722))
                                    .border(1.dp, TelegramGlassBorderSubtle, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("ALMOST EVERYONE", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.SansSerif)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFF00E5FF))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text("GETS THIS WRONG!", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Black)
                                    }
                                    Text("Sound FX: Bass Drop on Emphasis", color = TelegramEmerald, fontSize = 9.sp)
                                }
                            }

                            // Subtitle Styles
                            Text("Style Presets", color = TelegramTextSecondary, fontSize = 11.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("Hormozi Electric Cyan", "MrBeast Bold Gold", "Minimalist Clean").forEach { st ->
                                    val isSel = subtitleStyle == st
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) TelegramBlue.copy(alpha = 0.4f) else Color(0x221C2938))
                                            .border(1.dp, if (isSel) TelegramCyanAccent else TelegramGlassBorderSubtle, RoundedCornerShape(8.dp))
                                            .clickable { subtitleStyle = st }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(st, color = if (isSel) Color.White else TelegramTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                    }
                                }
                            }
                        }

                        VideoAiToolType.VOCAL_ISOLATION -> {
                            Text("Studio Voice Cleaner & Acoustic Isolation", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Audio Preset", color = TelegramTextSecondary, fontSize = 11.sp)
                                    Text(audioPreset, color = TelegramEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf("Studio Condenser 96kHz", "Podcast Broadcast", "Outdoor Wind Filter").forEach { p ->
                                        val isSel = audioPreset == p
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSel) TelegramEmerald.copy(alpha = 0.3f) else Color(0x221C2938))
                                                .border(1.dp, if (isSel) TelegramEmerald else TelegramGlassBorderSubtle, RoundedCornerShape(8.dp))
                                                .clickable { audioPreset = p }
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(p, color = if (isSel) Color.White else TelegramTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                        }
                                    }
                                }

                                Text("Background Score Auto-Ducking: ${(bgDuckingPct * 100).toInt()}%", color = TelegramTextSecondary, fontSize = 11.sp)
                                Slider(
                                    value = bgDuckingPct,
                                    onValueChange = { bgDuckingPct = it },
                                    colors = SliderDefaults.colors(thumbColor = TelegramEmerald, activeTrackColor = TelegramEmerald)
                                )
                            }
                        }

                        VideoAiToolType.SCRIPT_DOCTOR -> {
                            Text("Gemini 3.5 Flash Retention Script Doctor", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                            OutlinedTextField(
                                value = scriptInputText,
                                onValueChange = { scriptInputText = it },
                                label = { Text("Premise / Rough Script", fontSize = 11.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TelegramCyanAccent,
                                    unfocusedBorderColor = TelegramGlassBorderSubtle,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = TelegramTextPrimary
                                )
                            )

                            Button(
                                onClick = { onRunGeminiScriptDoctor(scriptInputText) },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isGeminiOptimizing,
                                colors = ButtonDefaults.buttonColors(containerColor = TelegramBlueBright)
                            ) {
                                if (isGeminiOptimizing) {
                                    CircularProgressIndicator(color = TelegramDarkBg, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Analyzing with Gemini 3.5 Flash...", color = TelegramDarkBg, fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(Icons.Default.Bolt, contentDescription = null, tint = TelegramDarkBg, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Analyze with Gemini ⚡", color = TelegramDarkBg, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Gemini Optimization Output
                            geminiOptimization?.let { opt ->
                                FrostedGlassBox(
                                    modifier = Modifier.fillMaxWidth(),
                                    borderColor = TelegramEmerald
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Viral Score: ${opt.viralScore}/100 🔥", color = TelegramEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(opt.retentionDropRisk, color = Color(0xFFFFD700), fontSize = 10.sp)
                                        }

                                        Text("Generated 3-Second Viral Hooks:", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        opt.hookVariations.forEach { hook ->
                                            Text("• $hook", color = TelegramCyanAccent, fontSize = 10.sp)
                                        }

                                        HorizontalDivider(color = TelegramGlassBorderSubtle)

                                        Text("Pacing Pointers:", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        opt.pacingPointers.forEach { pt ->
                                            Text("✓ $pt", color = TelegramTextPrimary, fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Process Enhancement Action Button
                    if (selectedTool != VideoAiToolType.SCRIPT_DOCTOR) {
                        if (isProcessingJob) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Enhancing video neural tensors...", color = Color.White, fontSize = 11.sp)
                                    Text("${(jobProgress * 100).toInt()}%", color = TelegramCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                LinearProgressIndicator(
                                    progress = { jobProgress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = TelegramCyanAccent,
                                    trackColor = Color(0x331C2938)
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    isProcessingJob = true
                                    jobProgress = 0.1f
                                    coroutineScope.launch {
                                        delay(400)
                                        jobProgress = 0.45f
                                        delay(500)
                                        jobProgress = 0.85f
                                        delay(400)
                                        jobProgress = 1.0f
                                        isProcessingJob = false
                                        onStartEnhancementJob(selectedTool, "Project Cut #04", "$targetResolution ($selectedLut)")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .testTag("run_ai_enhancer_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TelegramCyanAccent)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TelegramDarkBg, modifier = Modifier.size(16.dp))
                                    Text("Process ${selectedTool.title} ⚡", color = TelegramDarkBg, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 4. Completed Enhancement Jobs History ---
        item {
            Text(
                text = "Recent Enhancement Queue & Render Cache",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(jobs) { job ->
            FrostedGlassBox(
                modifier = Modifier.fillMaxWidth(),
                borderColor = TelegramGlassBorderSubtle
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(job.toolType.iconEmoji, fontSize = 22.sp)
                        Column {
                            Text(job.videoTitle, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("${job.toolType.title} • ${job.metadataSummary}", color = TelegramTextSecondary, fontSize = 10.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(TelegramEmerald.copy(alpha = 0.2f))
                            .border(1.dp, TelegramEmerald, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(job.status, color = TelegramEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

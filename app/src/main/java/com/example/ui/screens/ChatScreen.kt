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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.model.ChatConversation
import com.example.model.ChatMessage
import com.example.ui.components.AudioWaveformVisualizer
import com.example.ui.components.FrostedGlassBox
import com.example.ui.theme.*
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel

@Composable
fun ChatScreen(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    onDismiss: () -> Unit
) {
    val activeConv = uiState.conversations.find { it.id == uiState.activeConversationId }

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
                .testTag("chat_overlay_screen")
        ) {
            AnimatedContent(
                targetState = activeConv,
                label = "chat_screen_nav"
            ) { conv ->
                if (conv != null) {
                    ChatConversationDetailView(
                        conv = conv,
                        inputText = uiState.chatMessageInput,
                        onInputChange = { viewModel.updateChatMessageInput(it) },
                        onSendMessage = { viewModel.sendChatMessage() },
                        onVoiceCall = { viewModel.startCall(conv.peerName, conv.peerAvatar, isVideo = false) },
                        onVideoCall = { viewModel.startCall(conv.peerName, conv.peerAvatar, isVideo = true) },
                        onBack = { viewModel.closeConversationDetail() }
                    )
                } else {
                    ChatConversationsListView(
                        conversations = uiState.conversations,
                        onSelectConv = { viewModel.openConversation(it.id) },
                        onClose = onDismiss
                    )
                }
            }
        }
    }
}

@Composable
fun ChatConversationsListView(
    conversations: List<ChatConversation>,
    onSelectConv: (ChatConversation) -> Unit,
    onClose: () -> Unit
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TelegramTextPrimary
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Direct Messages",
                            color = TelegramTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Encrypted",
                            tint = TelegramCyanAccent,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Text(
                        text = "End-to-End Encrypted Protocol",
                        color = TelegramCyanAccent,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "New Message",
                    tint = TelegramBlueBright
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Conversations List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(conversations, key = { it.id }) { conv ->
                FrostedGlassBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectConv(conv) }
                        .testTag("chat_conversation_${conv.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar with online dot
                        Box(modifier = Modifier.size(48.dp)) {
                            AsyncImage(
                                model = conv.peerAvatar,
                                contentDescription = conv.peerName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .border(1.dp, TelegramGlassBorder, CircleShape)
                            )
                            if (conv.isOnline) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(TelegramEmerald)
                                        .border(2.dp, TelegramDarkBg, CircleShape)
                                )
                            }
                        }

                        // Info & Last Message
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = conv.peerName,
                                    color = TelegramTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = conv.lastTimestamp,
                                    color = TelegramTextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = conv.lastMessage,
                                    color = TelegramTextSecondary,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )

                                if (conv.unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(TelegramBlueBright),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = conv.unreadCount.toString(),
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
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
}

@Composable
fun ChatConversationDetailView(
    conv: ChatConversation,
    inputText: String,
    onInputChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onVoiceCall: () -> Unit,
    onVideoCall: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        // Chat Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TelegramTextPrimary
                    )
                }

                AsyncImage(
                    model = conv.peerAvatar,
                    contentDescription = conv.peerName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                )

                Column {
                    Text(
                        text = conv.peerName,
                        color = TelegramTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (conv.isOnline) "online • e2e encrypted" else "offline",
                        color = if (conv.isOnline) TelegramEmerald else TelegramTextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // Voice Call Button
                IconButton(onClick = onVoiceCall, modifier = Modifier.testTag("chat_voice_call_btn")) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Voice Call",
                        tint = TelegramCyanAccent
                    )
                }
                // Video Call Button
                IconButton(onClick = onVideoCall, modifier = Modifier.testTag("chat_video_call_btn")) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Video Call",
                        tint = TelegramBlueBright
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Messages Bubble Stream
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(conv.messages, key = { it.id }) { msg ->
                MessageBubble(message = msg)
            }
        }

        // Bottom Message Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChange,
                placeholder = {
                    Text(text = "Message...", color = TelegramTextMuted, fontSize = 13.sp)
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_message_input"),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TelegramBlueBright,
                    unfocusedBorderColor = TelegramGlassBorder,
                    focusedContainerColor = Color(0x33142232),
                    unfocusedContainerColor = Color(0x33142232),
                    focusedTextColor = TelegramTextPrimary,
                    unfocusedTextColor = TelegramTextPrimary
                )
            )

            IconButton(
                onClick = onSendMessage,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(TelegramBlueBright)
                    .testTag("send_chat_message_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isMe = message.isMe
    var isVoicePlaying by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isMe) Brush.linearGradient(listOf(TelegramBlue, TelegramBlueDark))
                    else Brush.verticalGradient(listOf(Color(0xD91B2939), Color(0xCC15212E)))
                )
                .border(
                    1.dp,
                    if (isMe) TelegramBlueBright.copy(alpha = 0.5f) else TelegramGlassBorderSubtle,
                    RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column {
                if (message.isVoiceNote) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { isVoicePlaying = !isVoicePlaying },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Icon(
                                imageVector = if (isVoicePlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = TelegramDarkBg,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        AudioWaveformVisualizer(
                            isPlaying = isVoicePlaying,
                            barCount = 18,
                            activeColor = Color.White,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = message.voiceDuration,
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                } else {
                    Text(
                        text = message.text,
                        color = Color.White,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = message.timestamp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 9.sp
                    )
                    if (isMe) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Read",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

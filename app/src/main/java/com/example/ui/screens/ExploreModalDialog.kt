package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*
import com.example.viewmodel.TelePulseUiState
import com.example.viewmodel.TelePulseViewModel

@Composable
fun ExploreModalDialog(
    uiState: TelePulseUiState,
    viewModel: TelePulseViewModel,
    onDismiss: () -> Unit
) {
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
                .testTag("explore_modal_dialog")
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
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
                                .background(Color(0x332AABEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = null,
                                tint = TelegramCyanAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "Explore & Search",
                            color = TelegramTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TelegramTextSecondary
                        )
                    }
                }

                // Embedded SearchExploreScreen with Instagram Explore Grid & Profile Search
                SearchExploreScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onNavigateToProfile = {
                        onDismiss()
                        viewModel.selectTab(4)
                    },
                    onNavigateToReels = {
                        onDismiss()
                        viewModel.selectTab(1)
                    },
                    onNavigateToHome = {
                        onDismiss()
                        viewModel.selectTab(0)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

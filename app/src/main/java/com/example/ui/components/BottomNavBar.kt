package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.ui.theme.*

data class NavTabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val isCenterAction: Boolean = false,
    val tag: String
)

@Composable
fun BottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onCenterPlusClick: () -> Unit = { onTabSelected(2) },
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavTabItem("Home", Icons.Filled.Home, Icons.Outlined.Home, tag = "tab_home"),
        NavTabItem("Reels", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary, tag = "tab_reels"),
        NavTabItem("Create", Icons.Filled.Videocam, Icons.Filled.Videocam, isCenterAction = true, tag = "tab_create"),
        NavTabItem("Videos", Icons.Filled.PlayCircle, Icons.Outlined.PlayCircleOutline, tag = "tab_long_videos"),
        NavTabItem("Profile", Icons.Filled.Person, Icons.Outlined.Person, tag = "tab_profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        // Frosted Glass Bar Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xEE162230),
                            Color(0xFA0E1722)
                        )
                    )
                )
                .border(1.dp, TelegramGlassBorder, RoundedCornerShape(32.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = selectedTab == index

                    if (item.isCenterAction) {
                        // Center Core Tab: Video Creation Studio Button (Unmistakably communicates Video Creation)
                        Box(
                            modifier = Modifier
                                .offset(y = (-5).dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(TelegramBlueBright, TelegramCyanAccent)
                                    )
                                )
                                .border(1.5.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                                .clickable { onCenterPlusClick() }
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                                .testTag(item.tag),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Videocam,
                                    contentDescription = "Create Video",
                                    tint = TelegramDarkBg,
                                    modifier = Modifier.size(20.dp)
                                )
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = TelegramDarkBg,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    } else {
                        // Standard Tab Item
                        val iconColor by animateColorAsState(
                            targetValue = if (isSelected) TelegramBlueBright else TelegramTextSecondary,
                            label = "color"
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .defaultMinSize(minWidth = 54.dp, minHeight = 48.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { onTabSelected(index) }
                                .testTag(item.tag)
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = iconColor,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = item.title,
                                color = iconColor,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

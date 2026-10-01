package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Telegram Dark Frosted Palette
val TelegramDarkBg = Color(0xFF0B141D)
val TelegramSurface = Color(0xFF131D2A)
val TelegramSurfaceVariant = Color(0xFF1C2736)
val TelegramCardBg = Color(0xDD17222E)
val TelegramGlassBorder = Color(0x2E6FA4D8)
val TelegramGlassBorderSubtle = Color(0x1FFFFFFF)

// Accent Colors
val TelegramBlue = Color(0xFF2AABEE)
val TelegramBlueBright = Color(0xFF4EA4F6)
val TelegramBlueDark = Color(0xFF1C75B8)
val TelegramCyanAccent = Color(0xFF00E5FF)
val TelegramEmerald = Color(0xFF34C759)
val TelegramAmber = Color(0xFFFF9500)
val TelegramCoral = Color(0xFFFF3B30)
val TelegramPurple = Color(0xFFA259FF)

// Text Colors
val TelegramTextPrimary = Color(0xFFF5F7FA)
val TelegramTextSecondary = Color(0xFF8696A4)
val TelegramTextMuted = Color(0xFF5D6F80)
val TelegramTextLink = Color(0xFF53A8F5)

// Frosted Glass Brushes
val GlassGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xCC1A2634),
        Color(0xAA131E2A)
    )
)

val FrostedCardBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xD91E2A38),
        Color(0xCC15202C)
    )
)

val GlowBlueBrush = Brush.radialGradient(
    colors = listOf(
        Color(0x4D2AABEE),
        Color(0x002AABEE)
    )
)

package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val TelegramDarkColorScheme = darkColorScheme(
    primary = TelegramBlue,
    onPrimary = TelegramTextPrimary,
    primaryContainer = TelegramSurfaceVariant,
    onPrimaryContainer = TelegramTextPrimary,
    secondary = TelegramCyanAccent,
    onSecondary = TelegramDarkBg,
    secondaryContainer = TelegramSurfaceVariant,
    onSecondaryContainer = TelegramTextPrimary,
    tertiary = TelegramPurple,
    background = TelegramDarkBg,
    onBackground = TelegramTextPrimary,
    surface = TelegramSurface,
    onSurface = TelegramTextPrimary,
    surfaceVariant = TelegramSurfaceVariant,
    onSurfaceVariant = TelegramTextSecondary,
    outline = TelegramGlassBorder,
    outlineVariant = TelegramGlassBorderSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                val insetsController = WindowCompat.getInsetsController(it, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = TelegramDarkColorScheme,
        typography = Typography,
        content = content
    )
}

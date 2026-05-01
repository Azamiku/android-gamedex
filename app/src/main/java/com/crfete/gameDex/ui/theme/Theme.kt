package com.crfete.gameDex.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Tema personalizado de mi app GameDex
 * Requisito: Cambiar tema por defecto
 */

private val DarkColorScheme = darkColorScheme(
    primary = GamePrimaryDark,
    secondary = GameSecondaryDark,
    tertiary = GameTertiaryDark,
    background = GameBackgroundDark,
    surface = GameSurfaceDark,
    onPrimary = Color(0xFFFFFFFF),           // Blanco sobre primario
    onSecondary = Color(0xFF000000),         // Negro sobre secundario
    onTertiary = Color(0xFF000000),          // Negro sobre terciario
    onBackground = Color(0xFFC4B5FD),
    onSurface = Color(0xFFDDD6FE)
)

private val LightColorScheme = lightColorScheme(
    primary = GamePrimary,
    secondary = GameSecondary,
    tertiary = GameTertiary,
    background = GameBackground,
    surface = GameSurface,
    onPrimary = Color(0xFFFFFFFF),           // Blanco sobre primario
    onSecondary = Color(0xFFFFFFFF),         // Blanco sobre secundario
    onTertiary = Color(0xFF000000),          // Negro sobre terciario
    onBackground = Color(0xFFC4B5FD),
    onSurface = Color(0xFFDDD6FE)
)

@Composable
fun GameDexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
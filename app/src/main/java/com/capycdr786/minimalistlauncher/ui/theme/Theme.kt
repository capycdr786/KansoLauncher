package com.capycdr786.minimalistlauncher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    background = Black,
    onBackground = White,
    surface = DarkSurface,
    onSurface = White,
    surfaceVariant = DarkGray,
    onSurfaceVariant = White
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    background = White,
    onBackground = Black,
    surface = LightSurface,
    onSurface = Black,
    surfaceVariant = LightGray,
    onSurfaceVariant = Black
)

@Composable
fun BoringLauncherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

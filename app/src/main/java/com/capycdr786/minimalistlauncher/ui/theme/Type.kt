package com.capycdr786.minimalistlauncher.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.capycdr786.minimalistlauncher.R

val Exo2FontFamily = FontFamily(
    Font(R.font.exo2_regular, FontWeight.Normal),
    Font(R.font.exo2_regular, FontWeight.Medium),
    Font(R.font.exo2_regular, FontWeight.Bold),
    Font(R.font.exo2_regular, FontWeight.Light),
)

// Primary launcher font family
val LauncherFontFamily = Exo2FontFamily
val InterFontFamily = Exo2FontFamily

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = LauncherFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = LauncherFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = LauncherFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.2).sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = LauncherFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
)

package com.example.minicafe.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ---------- Couleurs (Styleguide Figma) ----------
val Primary = Color(0xFFC67C4E)
val PrimaryLight = Color(0xFFEDD6C8)
val Dark = Color(0xFF313131)
val Grey = Color(0xFFE3E3E3)
val Cream = Color(0xFFF9F2ED)

// Valeurs lues sur les screenshots
val DarkHeader = Color(0xFF1C1C1C)
val ScreenBackground = Color(0xFFF9F9F9)
val InputBackground = Color(0xFFF3F6FB)
val TextSecondary = Color(0xFF9B9B9B)
val Danger = Color(0xFFD64545)

/**
 * Police de l'app. Pour utiliser Sora : mets les .ttf dans res/font/
 * (sora_regular, sora_medium, sora_semibold, sora_bold) et remplace par :
 *
 * val AppFont = FontFamily(
 *     Font(R.font.sora_regular, FontWeight.Normal),
 *     Font(R.font.sora_medium, FontWeight.Medium),
 *     Font(R.font.sora_semibold, FontWeight.SemiBold),
 *     Font(R.font.sora_bold, FontWeight.Bold),
 * )
 */
val AppFont: FontFamily = FontFamily.Default

private val AppTypography = Typography(
    bodyLarge = TextStyle(fontFamily = AppFont, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 21.sp),
    bodyMedium = TextStyle(fontFamily = AppFont, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 18.sp),
    bodySmall = TextStyle(fontFamily = AppFont, fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontFamily = AppFont, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    titleMedium = TextStyle(fontFamily = AppFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
)

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    secondary = PrimaryLight,
    background = ScreenBackground,
    surface = Color.White,
    onSurface = Dark,
    error = Danger,
)

@Composable
fun MiniCafeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content,
    )
}

package com.example.katalogproduk.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = Color(0xFF1B6B5A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBFEBDD),
    onPrimaryContainer = Color(0xFF00201A),
    secondaryContainer = Color(0xFFD7EFE8),
    onSecondaryContainer = Color(0xFF0B2B24),
    background = Color(0xFFF4F9F7),
    onBackground = Color(0xFF161D1B),
    surface = Color.White,
    onSurface = Color(0xFF161D1B),
    onSurfaceVariant = Color(0xFF55615D)
)

private val AppTypography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)

@Composable
fun KatalogProdukTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}

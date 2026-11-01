/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.ui.theme

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme
import com.materialkolor.score.Score

val DefaultThemeColor = Color(0xFFED5564)

// ---- Midnight Green palette -------------------------------------------------
val AccentGreen = Color(0xFF1ED760)
val AccentGreenDark = Color(0xFF1AA34A)
val MidnightBackground = Color(0xFF121212)
val MidnightSurface = Color(0xFF181818)
val MidnightSurfaceHigh = Color(0xFF242424)
val MidnightSurfaceHighest = Color(0xFF2A2A2A)
val MidnightSubtext = Color(0xFFB3B3B3)

val MidnightGreenColorScheme = darkColorScheme(
    primary = AccentGreen,
    onPrimary = Color.Black,
    primaryContainer = AccentGreenDark,
    onPrimaryContainer = Color.Black,
    inversePrimary = AccentGreenDark,
    secondary = Color.White,
    onSecondary = Color.Black,
    secondaryContainer = MidnightSurfaceHighest,
    onSecondaryContainer = Color.White,
    tertiary = AccentGreen,
    onTertiary = Color.Black,
    tertiaryContainer = MidnightSurfaceHigh,
    onTertiaryContainer = Color.White,
    background = MidnightBackground,
    onBackground = Color.White,
    surface = MidnightBackground,
    onSurface = Color.White,
    surfaceVariant = MidnightSurfaceHigh,
    onSurfaceVariant = MidnightSubtext,
    surfaceTint = Color.Transparent,
    inverseSurface = Color.White,
    inverseOnSurface = Color.Black,
    error = Color(0xFFF15E6C),
    onError = Color.Black,
    errorContainer = Color(0xFF5C1A22),
    onErrorContainer = Color.White,
    outline = Color(0xFF727272),
    outlineVariant = Color(0xFF3E3E3E),
    scrim = Color.Black,
    surfaceBright = MidnightSurfaceHighest,
    surfaceDim = MidnightBackground,
    surfaceContainerLowest = Color(0xFF0A0A0A),
    surfaceContainerLow = MidnightSurface,
    surfaceContainer = MidnightSurface,
    surfaceContainerHigh = MidnightSurfaceHigh,
    surfaceContainerHighest = MidnightSurfaceHighest,
)

private val BaseTypography = Typography()

// Bold, tight headings with plain body text
val MidnightTypography = Typography(
    displayLarge = BaseTypography.displayLarge.copy(fontWeight = FontWeight.Bold),
    displayMedium = BaseTypography.displayMedium.copy(fontWeight = FontWeight.Bold),
    displaySmall = BaseTypography.displaySmall.copy(fontWeight = FontWeight.Bold),
    headlineLarge = BaseTypography.headlineLarge.copy(fontWeight = FontWeight.Bold),
    headlineMedium = BaseTypography.headlineMedium.copy(fontWeight = FontWeight.Bold),
    headlineSmall = BaseTypography.headlineSmall.copy(fontWeight = FontWeight.Bold),
    titleLarge = BaseTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
    titleMedium = BaseTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
    titleSmall = BaseTypography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
    bodyLarge = BaseTypography.bodyLarge,
    bodyMedium = BaseTypography.bodyMedium,
    bodySmall = BaseTypography.bodySmall,
    labelLarge = BaseTypography.labelLarge.copy(fontWeight = FontWeight.Bold),
    labelMedium = BaseTypography.labelMedium,
    labelSmall = BaseTypography.labelSmall,
)

// Small corner radii: square-ish album art and cards
val MidnightShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(6.dp),
    large = RoundedCornerShape(8.dp),
    extraLarge = RoundedCornerShape(12.dp),
)

@Composable
fun MetrolistTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    pureBlack: Boolean = false,
    themeColor: Color = DefaultThemeColor,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    // Midnight Green: when the default theme colour is selected, use a fixed
    // dark scheme (near-black surfaces, green accent) instead of wallpaper colours.
    val useMidnightGreen = themeColor == DefaultThemeColor
    val useSystemDynamicColor = false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    // Select the appropriate color scheme generation method
    val baseColorScheme = if (useMidnightGreen) {
        MidnightGreenColorScheme
    } else if (useSystemDynamicColor) {
        // Use standard Material 3 dynamic color functions for system wallpaper colors
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        // Use materialKolor only when a specific seed color is provided
        rememberDynamicColorScheme(
            seedColor = themeColor, // themeColor is guaranteed non-default here
            isDark = darkTheme,
            specVersion = ColorSpec.SpecVersion.SPEC_2025,
            style = PaletteStyle.TonalSpot // Keep existing style
        )
    }

    // Apply pureBlack modification if needed, similar to original logic
    val colorScheme = remember(baseColorScheme, pureBlack, darkTheme) {
        if (darkTheme && pureBlack) {
            baseColorScheme.pureBlack(true)
        } else {
            baseColorScheme
        }
    }

    // Use standard MaterialTheme instead of MaterialExpressiveTheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = MidnightTypography,
        shapes = MidnightShapes,
        content = content,
    )
}

fun Bitmap.extractThemeColor(): Color = Color(
    Palette.from(this)
        .maximumColorCount(8)
        .generate()
        .rankedColors(1, DefaultThemeColor.toArgb())
        .first()
)

internal fun Palette.rankedColors(
    desiredColorCount: Int,
    fallbackColor: Int,
): List<Int> = Score.score(
    swatches.associate { it.rgb to it.population },
    desiredColorCount,
    fallbackColor,
    true,
)

fun ColorScheme.pureBlack(apply: Boolean) =
    if (apply) copy(
        surface = Color.Black,
        background = Color.Black
    ) else this

val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color = Color(value)
    override fun SaverScope.save(value: Color): Int = value.toArgb()
}

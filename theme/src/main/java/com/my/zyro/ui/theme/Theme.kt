/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.ui.theme

import android.os.Build
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.unit.dp
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.PaletteStyle
import com.kyant.monet.TonalPalettes.Companion.toTonalPalettes
import com.kyant.monet.dynamicColorScheme
import com.my.zyro.preference.palettesMap

/**
 * Original Zyro theme implementation
 * Completely reimplemented from scratch with custom Material3 ColorScheme
 */

private tailrec fun Context.findWindow(): Window? =
    when (this) {
        is Activity -> window
        is ContextWrapper -> baseContext.findWindow()
        else -> null
    }

/**
 * Creates a Zyro ColorScheme using custom Zyro colors.
 * Used by SwitchBar and other components that need a standalone scheme.
 */
@Composable
fun getZyroColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrastModeEnabled: Boolean = false,
): ColorScheme {
    return remember(darkTheme, isHighContrastModeEnabled) {
        (if (darkTheme) darkColorScheme(
        primary = Color(0xFF6DE0DB), onPrimary = Color(0xFF003735),
        primaryContainer = Color(0xFF164B50), onPrimaryContainer = Color(0xFFB8F6EF),
        secondary = Color(0xFFB8B9FF), onSecondary = Color(0xFF242457),
        secondaryContainer = Color(0xFF303454), onSecondaryContainer = Color(0xFFE4E3FF),
        tertiary = Color(0xFFFFD28C), onTertiary = Color(0xFF412D08),
        background = if (isHighContrastModeEnabled) Color.Black else Color(0xFF0B1220),
        onBackground = Color(0xFFE8EEF7),
        surface = Color(0xFF111C2D), onSurface = Color(0xFFE8EEF7),
        surfaceVariant = Color(0xFF1D2C40), onSurfaceVariant = Color(0xFFB5C2D4),
        surfaceContainerLowest = Color(0xFF080F1A), surfaceContainerLow = Color(0xFF101A29),
        surfaceContainer = Color(0xFF152235), surfaceContainerHigh = Color(0xFF1B2A3E),
        surfaceContainerHighest = Color(0xFF24344A),
        outline = Color(0xFF8393AA), outlineVariant = Color(0xFF304056),
        error = Color(0xFFFFB4B0), onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A), onErrorContainer = Color(0xFFFFDAD6)
    ) else lightColorScheme(
        primary = Color(0xFF006A68), onPrimary = Color.White,
        primaryContainer = Color(0xFFB7F2EB), onPrimaryContainer = Color(0xFF00201F),
        secondary = Color(0xFF555992), onSecondary = Color.White,
        secondaryContainer = Color(0xFFE1E0FF), onSecondaryContainer = Color(0xFF111442),
        background = Color(0xFFF3F6FC), onBackground = Color(0xFF172234),
        surface = Color.White, onSurface = Color(0xFF172234),
        surfaceVariant = Color(0xFFE6EDF6), onSurfaceVariant = Color(0xFF46566B),
        surfaceContainerLowest = Color.White, surfaceContainerLow = Color(0xFFF4F7FC),
        surfaceContainer = Color(0xFFEBF0F8), surfaceContainerHigh = Color(0xFFE3EAF4),
        surfaceContainerHighest = Color(0xFFDCE4F0),
        outline = Color(0xFF65768C), outlineVariant = Color(0xFFD0DAE8)
    )).withOled(darkTheme && isHighContrastModeEnabled)
    }
}

/**
 * Main Zyro Theme Composable
 * Applies custom color scheme, typography, and system UI styling
 * Supports Material You dynamic colors via isDynamicColorEnabled
 * Uses Monet color science engine for seed-color-based themes
 */
@Composable
fun ZyroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrastModeEnabled: Boolean = false,
    isDynamicColorEnabled: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val window = view.context.findWindow()

    if (isDynamicColorEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Material You dynamic colors (Android 12+)
        val systemScheme = remember(context, darkTheme) {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        val colorScheme = remember(systemScheme, darkTheme, isHighContrastModeEnabled) {
            systemScheme.withOled(darkTheme && isHighContrastModeEnabled)
        }

        val systemUiController = rememberSystemUiController(window)
        LaunchedEffect(window, view, darkTheme, colorScheme.background) {
            window?.let { WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme }
            systemUiController.apply {
            setSystemBarsColor(color = Color.Transparent, darkIcons = !darkTheme)
            setNavigationBarColor(color = colorScheme.background, darkIcons = !darkTheme)
            }
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = ZyroShapes,
            content = content
        )
    } else {
        // Monet color science from user-selected seed color and palette style
        val seedColor = Color(LocalSeedColor.current)
        val paletteStyle = palettesMap[LocalPaletteStyleIndex.current] ?: PaletteStyle.Vibrant
        val tonalPalettes = remember(seedColor, paletteStyle) { seedColor.toTonalPalettes(paletteStyle) }

        CompositionLocalProvider(LocalTonalPalettes provides tonalPalettes) {
            val base = dynamicColorScheme(isLight = !darkTheme)
            val brand = getZyroColorScheme(darkTheme, isHighContrastModeEnabled)
            val selectedScheme = if (LocalSeedColor.current == com.my.zyro.preference.DEFAULT_SEED_COLOR) brand
            else base.copy(
                background = brand.background, onBackground = brand.onBackground,
                surface = brand.surface, onSurface = brand.onSurface,
                surfaceVariant = brand.surfaceVariant, onSurfaceVariant = brand.onSurfaceVariant,
                surfaceContainerLowest = brand.surfaceContainerLowest,
                surfaceContainerLow = brand.surfaceContainerLow,
                surfaceContainer = brand.surfaceContainer,
                surfaceContainerHigh = brand.surfaceContainerHigh,
                surfaceContainerHighest = brand.surfaceContainerHighest,
                outlineVariant = brand.outlineVariant
            )

            val colorScheme = remember(selectedScheme, darkTheme, isHighContrastModeEnabled) {
                selectedScheme.withOled(darkTheme && isHighContrastModeEnabled)
            }
            val systemUiController = rememberSystemUiController(window)
            LaunchedEffect(window, view, darkTheme, colorScheme.background) {
                window?.let { WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme }
                systemUiController.apply {
                setSystemBarsColor(color = Color.Transparent, darkIcons = !darkTheme)
                setNavigationBarColor(color = colorScheme.background, darkIcons = !darkTheme)
                }
            }

            MaterialTheme(
                colorScheme = colorScheme,
                typography = AppTypography,
            shapes = ZyroShapes,
                content = content
            )
        }
    }
}


val ZyroShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp), small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp), large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

internal fun ColorScheme.withOled(enabled: Boolean): ColorScheme = if (!enabled) this else copy(
    background = Color.Black, surface = Color.Black,
    surfaceDim = Color.Black, surfaceBright = Color(0xFF141414),
    surfaceContainerLowest = Color.Black, surfaceContainerLow = Color(0xFF050505),
    surfaceContainer = Color(0xFF0A0A0A), surfaceContainerHigh = Color(0xFF101010),
    surfaceContainerHighest = Color(0xFF181818), surfaceVariant = Color(0xFF181818),
    surfaceTint = Color.Transparent, onSurface = Color(0xFFF2F2F2),
    onBackground = Color(0xFFF2F2F2), onSurfaceVariant = Color(0xFFC7C7C7),
    outline = Color(0xFF929292), outlineVariant = Color(0xFF333333)
)

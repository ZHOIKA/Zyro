/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
 * Creates an original Zyro ColorScheme independent from Kizzy
 * Uses custom Zyro color palette for dark theme
 */
@Composable
fun getZyroColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrastModeEnabled: Boolean = false,
): ColorScheme {
    val primary = ZyroViolet
    val primaryContainer = ZyroVioletDark
    val secondary = ZyroCyan
    val tertiary = ZyroAmber
    val background = if (isHighContrastModeEnabled) ZyroBlack else ZyroGray900
    val surface = ZyroGray800
    val surfaceVariant = ZyroGray700
    
    return ColorScheme(
        primary = primary,
        onPrimary = ZyroWhite,
        primaryContainer = primaryContainer,
        onPrimaryContainer = ZyroVioletLight,
        secondary = secondary,
        onSecondary = ZyroBlack,
        secondaryContainer = ZyroCyan.copy(alpha = 0.2f),
        onSecondaryContainer = ZyroCyan,
        tertiary = tertiary,
        onTertiary = ZyroBlack,
        tertiaryContainer = ZyroAmber.copy(alpha = 0.2f),
        onTertiaryContainer = ZyroAmber,
        error = ZyroRose,
        onError = ZyroWhite,
        errorContainer = ZyroRose.copy(alpha = 0.2f),
        onErrorContainer = ZyroRose,
        background = background,
        onBackground = ZyroGray100,
        surface = surface,
        onSurface = ZyroGray100,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = ZyroGray400,
        outline = ZyroGray500,
        outlineVariant = ZyroOutlineVariant,
        scrim = ZyroScrim,
        inverseSurface = ZyroGray100,
        inverseOnSurface = ZyroBlack,
        inversePrimary = ZyroVioletLight,
        surfaceTint = surface,
    )
}

/**
 * Main Zyro Theme Composable
 * Applies custom color scheme, typography, and system UI styling
 */
@Composable
fun ZyroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrastModeEnabled: Boolean = false,
    isDynamicColorEnabled: Boolean = false,  // Legacy parameter kept for compatibility
    content: @Composable () -> Unit
) {
    val colorScheme = getZyroColorScheme(
        darkTheme,
        isHighContrastModeEnabled,
    )
    
    val window = LocalView.current.context.findWindow()
    val view = LocalView.current

    // Configure system UI appearance
    window?.let {
        WindowCompat.getInsetsController(it, view)?.isAppearanceLightStatusBars = !darkTheme
    }

    // Set system bar colors to match theme
    rememberSystemUiController(window).apply {
        setSystemBarsColor(color = Color.Transparent, darkIcons = !darkTheme)
        setNavigationBarColor(color = colorScheme.background, darkIcons = !darkTheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

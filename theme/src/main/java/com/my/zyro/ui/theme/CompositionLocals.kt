/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */
package com.my.zyro.ui.theme

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.my.zyro.preference.AppSettingsStateFlow
import com.my.zyro.preference.DEFAULT_SEED_COLOR
import com.my.zyro.preference.DarkThemePreference

/**
 * Zyro Theme Composition Locals
 * Provides theme and app settings through Compose composition tree
 * Reimplemented independently from Kizzy with own preference handling
 */

// Theme and appearance settings
val LocalDarkTheme = compositionLocalOf { DarkThemePreference() }
val LocalSeedColor = compositionLocalOf { DEFAULT_SEED_COLOR }
val LocalDynamicColorSwitch = compositionLocalOf { false }

// UI layout and sizing
val LocalWindowWidthState = staticCompositionLocalOf { WindowWidthSizeClass.Compact }
val LocalPaletteStyleIndex = compositionLocalOf { 0 }

// App-specific view models and dependencies (kept as placeholders for compatibility)
val LocalRpcConnection = compositionLocalOf<Any?> { null }
val LocalHomeViewModel = compositionLocalOf<Any?> { null }
val LocalLogger = compositionLocalOf<Any?> { null }

/**
 * Settings Provider Composable
 * Injects app settings into the composition tree via Composition Locals
 * Observes preference changes and updates tree automatically
 */
@Composable
fun SettingsProvider(
    windowWidthSizeClass: WindowWidthSizeClass,
    content: @Composable () -> Unit
) {
    val appSettingsState = AppSettingsStateFlow.collectAsState().value
    
    CompositionLocalProvider(
        LocalDarkTheme provides appSettingsState.darkTheme,
        LocalSeedColor provides appSettingsState.seedColor,
        LocalPaletteStyleIndex provides appSettingsState.paletteStyleIndex,
        LocalWindowWidthState provides windowWidthSizeClass,
        LocalDynamicColorSwitch provides appSettingsState.isDynamicColorEnabled,
        content = content
    )
}
/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.ui.theme

import androidx.compose.ui.graphics.Color
import com.my.zyro.domain.model.logs.LogLevel

/**
 * Original Zyro color palette - redesigned independently from Kizzy
 * Implements a modern dark theme with custom accent colors for Discord RPC integration
 */

// Primary Colors - Vibrant purple/indigo spectrum
val ZyroViolet = Color(0xFF7C3AED)      // Primary brand color
val ZyroVioletLight = Color(0xFF9F7AEA) // Lighter variant
val ZyroVioletDark = Color(0xFF6D28D9)  // Darker variant

// Secondary Colors - Accent palette
val ZyroCyan = Color(0xFF06B6D4)        // Secondary accent
val ZyroAmber = Color(0xFFFBBF24)       // Warning/Alert color
val ZyroRose = Color(0xFFFB7185)        // Error/Destructive action

// Neutral Grayscale - Dark theme foundation
val ZyroBlack = Color(0xFF0F0F0F)       // True black background
val ZyroGray900 = Color(0xFF1A1A1A)     // Very dark gray
val ZyroGray800 = Color(0xFF262626)     // Dark gray - surfaces
val ZyroGray700 = Color(0xFF404040)     // Medium-dark gray - dividers
val ZyroGray600 = Color(0xFF525252)     // Medium gray
val ZyroGray500 = Color(0xFF737373)     // Mid gray
val ZyroGray400 = Color(0xFF9CA3AF)     // Light gray
val ZyroGray300 = Color(0xFFD1D5DB)     // Lighter gray
val ZyroGray100 = Color(0xFFF3F4F6)     // Very light gray
val ZyroWhite = Color(0xFFFFFFFF)       // Pure white

// Success & Status Colors
val ZyroGreen = Color(0xFF10B981)       // Success indicator
val ZyroBlue = Color(0xFF3B82F6)        // Info indicator
val ZyroOrange = Color(0xFFF97316)      // Warning indicator

// Semantic Colors for UI elements
val ZyroSurfaceLight = Color(0xFF1F1F1F)      // Elevated surfaces
val ZyroSurfaceDim = Color(0xFF121212)        // Dimmed backgrounds
val ZyroOutlineVariant = Color(0xFF3A3A3A)    // Subtle dividers
val ZyroScrim = Color(0xFF000000)             // Overlay scrim (80% opacity)

// Log Colors for Console Display
object LogColors {
    val ERROR = Color(0xFFF44336)       // Red
    val WARNING = Color(0xFFFFC107)     // Amber
    val INFO = Color(0xFF2196F3)        // Blue
    val DEBUG = Color(0xFF4CAF50)       // Green
    val VERBOSE = Color(0xFF9C27B0)     // Purple
    val DEFAULT = ZyroGray400           // Default gray
    
    fun LogLevel.color() = Color(when (this) {
        LogLevel.INFO -> INFO
        LogLevel.DEBUG -> DEBUG
        LogLevel.WARN -> WARNING
        LogLevel.ERROR -> ERROR
    })
}

// Legacy compatibility - maps old Discord colors to new Zyro palette
// Used for gradual migration of existing code
val DISCORD_LIGHT_DARK = ZyroGray800         // was Color(0xFF282b30)
val DISCORD_GREY = ZyroGray600               // was Color(0xFF36393e)
val DISCORD_BLURPLE = ZyroViolet             // was Color(0xFF5663f1)


/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Original Zyro Typography System
 * Reimplemented with Material Design 3 standards and system font families
 * Uses default system font stack instead of proprietary Google Sans fonts
 */

// System default font families for cross-platform compatibility
val systemSans = FontFamily.Default
val systemMonospace = FontFamily.Monospace

/**
 * Zyro Material 3 Typography Scale
 * Defines type styles for all text elements in the application
 * Based on Material Design 3 specs but implemented independently
 */
val AppTypography = Typography(
    // Display styles - Large, prominent text
    displayLarge = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W400,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W400,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W400,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    
    // Headline styles - Large titles and headers
    headlineLarge = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W500,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W500,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    
    // Title styles - Medium importance text
    titleLarge = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W500,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (0.15).sp,
    ),
    titleSmall = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.1).sp,
    ),
    
    // Body styles - Standard paragraph text
    bodyLarge = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (0.15).sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.25).sp,
    ),
    bodySmall = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.4).sp,
    ),
    
    // Label styles - Small UI elements, buttons, chips
    labelLarge = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (0.1).sp,
    ),
    labelMedium = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.5).sp,
    ),
    labelSmall = TextStyle(
        fontFamily = systemSans,
        fontWeight = FontWeight.W500,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = (0.5).sp,
    ),
)
                fontFamily = googleSansDisplay,
                fontWeight = FontWeight.W400,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.sp,
        ),
        headlineSmall = TextStyle(
                fontFamily = googleSansDisplay,
                fontWeight = FontWeight.W400,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
        ),
        titleLarge = TextStyle(
                fontFamily = googleSansDisplay,
                fontWeight = FontWeight.W400,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
        ),
        titleMedium = TextStyle(
                fontFamily = googleSansText,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.1.sp,
        ),
        titleSmall = TextStyle(
                fontFamily = googleSansText,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
        ),
        labelLarge = TextStyle(
                fontFamily = googleSansText,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
        ),
        bodyLarge = TextStyle(
                fontFamily = googleSansText,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
        ),
        bodyMedium = TextStyle(
                fontFamily = googleSansText,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp,
        ),
        bodySmall = TextStyle(
                fontFamily = googleSansText,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
        ),
        labelMedium = TextStyle(
                fontFamily = googleSansText,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
        ),
        labelSmall = TextStyle(
                fontFamily = googleSansText,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
        ),
)

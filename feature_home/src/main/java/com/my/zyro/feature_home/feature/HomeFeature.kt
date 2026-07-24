/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_home.feature

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
data class HomeFeature(
    val title: String,
    @DrawableRes val icon: Int,
    val route: String? = null,
    val isChecked: Boolean = false,
    val onClick: (String) -> Unit = {},
    val onCheckedChange: (Boolean) -> Unit = {},
    val showSwitch: Boolean = true,
    val shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    val tooltipText: String = "",
    val featureDocsLink: String = ""
)
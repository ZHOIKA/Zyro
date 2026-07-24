/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_media_rpc

import androidx.compose.runtime.Immutable
import com.my.zyro.data.utils.AppsInfo

@Immutable
data class MediaAppsState(
    val apps: List<AppsInfo> = emptyList(),
    val enabledApps: Map<String, Boolean> = emptyMap(),
    val isLoading: Boolean = true,
)
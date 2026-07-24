/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package xyz.zyro.feature_experimental_rpc

import androidx.compose.runtime.Immutable
import com.my.zyro.data.utils.AppsInfo

@Immutable
data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAppsRpcPartEnabled: Boolean = true,
    val isMediaRpcPartEnabled: Boolean = true,
    val templateName: String = "",
    val templateDetails: String = "",
    val templateState: String = "",
    val installedApps: List<AppsInfo> = emptyList(),
    val enabledApps: Map<String, Boolean> = emptyMap(), // pkg -> enabled
    val appActivityTypes: Map<String, Int> = emptyMap(), // pkg -> activity type
    val isAppsLoading: Boolean = false,
    val showCoverArt: Boolean = true,
    val showAppIcon: Boolean = false,
    val showPlaybackState: Boolean = true,
    val enableTimestamps: Boolean = true,
    val hideOnPause: Boolean = false
)

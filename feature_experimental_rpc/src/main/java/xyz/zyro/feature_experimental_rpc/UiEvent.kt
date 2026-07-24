/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package xyz.zyro.feature_experimental_rpc

sealed interface UiEvent {
    data class ToggleAppsRpcPart(val enabled: Boolean) : UiEvent
    data class ToggleMediaRpcPart(val enabled: Boolean) : UiEvent
    data class SetTemplateName(val value: String) : UiEvent
    data class SetTemplateDetails(val value: String) : UiEvent
    data class SetTemplateState(val value: String) : UiEvent
    data class ToggleAppEnabled(val packageName: String) : UiEvent
    data class SetAppActivityType(val packageName: String, val activityType: Int) : UiEvent
    data class ToggleShowCoverArt(val enabled: Boolean) : UiEvent
    data class ToggleShowAppIcon(val enabled: Boolean) : UiEvent
    data class ToggleShowPlaybackState(val enabled: Boolean) : UiEvent
    data class ToggleEnableTimestamps(val enabled: Boolean) : UiEvent
    data class ToggleHideOnPause(val enabled: Boolean) : UiEvent
}

/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * MediaAppsState.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of yzziK(Vaibhav)
 *  *  *****************************************************************
 *
 *
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
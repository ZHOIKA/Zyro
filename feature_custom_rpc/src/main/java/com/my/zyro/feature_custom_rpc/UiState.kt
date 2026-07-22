/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * UiState.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.feature_custom_rpc

import androidx.compose.runtime.Immutable
import com.my.zyro.domain.model.rpc.RpcConfig

@Immutable
data class UiState(
    val activityTypeIsExpanded: Boolean = false,
    val showBottomSheet: Boolean = false,
    val showLoadDialog: Boolean = false,
    val showShareDialog: Boolean = false,
    val showSaveDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showPreviewDialog: Boolean = false,
    val showStoragePermissionRequestDialog: Boolean = false,
    val showStartTimeStampsPickerDialog: Boolean = false,
    val showStopTimeStampsPickerDialog: Boolean = false,
    val statusIsExpanded: Boolean = false,
    val platformIsExpanded: Boolean = false,
    val rpcConfig: RpcConfig = RpcConfig()
)
/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_custom_rpc.components.sheet

import android.Manifest
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.my.zyro.resources.R

@Composable
@OptIn(ExperimentalPermissionsApi::class)
internal fun RequestStoragePermissionDialog(
    onDismiss: () -> Unit
) {
    val storagePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    when (storagePermissionState.status) {
        PermissionStatus.Granted -> {}
        is PermissionStatus.Denied -> {
            val reqText =
                if ((storagePermissionState.status as PermissionStatus.Denied).shouldShowRationale) {
                    stringResource(id = R.string.text_after_permission_denied)
                } else {
                    stringResource(id = R.string.request_for_storage_access)
                }
            AlertDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    Button(onClick = { storagePermissionState.launchPermissionRequest() }) {
                        Text(text = stringResource(id = R.string.grant_permission))
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.permission_required))
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = "storage"
                    )
                },
                text = {
                    Text(text = reqText)
                }
            )
        }
    }
}
/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_custom_rpc.components.sheet

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.my.zyro.data.utils.shareFile
import com.my.zyro.resources.R
import com.my.zyro.ui.components.dialog.SingleChoiceItem
import java.io.File

@Composable
fun ShareConfig(
    onDismiss: () -> Unit
) {
    val ctx = LocalContext.current
    val dir = ctx.dir()
    dir.mkdirs()
    AlertDialog(onDismissRequest = { onDismiss() },
        confirmButton = {},
        title = { Text(text = stringResource(id = R.string.select_a_config)) }, text = {
            LazyColumn {
                val files = dir.list(FILE_FILTER)?.asList()
                files?.forEach { file ->
                    item {
                        SingleChoiceItem(
                            text = file.dropLast(5),
                            selected = false
                        ) {
                            onDismiss()
                            ctx.shareFile(File(dir,file))
                        }
                    }
                }
            }
        })
}
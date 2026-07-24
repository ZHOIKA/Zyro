/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_custom_rpc.components.sheet

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.blankj.utilcode.util.FileIOUtils
import com.my.zyro.data.rpc.Constants
import com.my.zyro.data.utils.getFileName
import com.my.zyro.domain.model.rpc.RpcConfig
import com.my.zyro.preference.Prefs
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FilenameFilter

internal val FILE_FILTER = FilenameFilter { _: File?, f: String ->
    f.endsWith(".json")
}

internal fun Context.dir() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        File(this.filesDir, "Configs")
    else {
        val selected = Prefs[Prefs.CONFIGS_DIRECTORY, Constants.DOWNLOADS_DIRECTORY]
        if (selected == Constants.DOWNLOADS_DIRECTORY)
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "Zyro")
        else
            File(this.filesDir, "Configs")
    }

internal fun Context.handleUriResult(uri: Uri?, onSuccess: (json: String) -> Unit) {
    if (uri == null)
        return
    val fileName = this.getFileName(uri)
    if (!fileName.endsWith(".json"))
        return

    val file = File(this.cacheDir, "tmp.json")
    val inputStream = this.contentResolver.openInputStream(uri)
    inputStream?.use { input ->
        file.outputStream().use { out ->
            input.copyTo(out)
        }
    }
    FileIOUtils.readFile2String(file).also { json ->
        onSuccess(json)
    }
}
internal val json = Json {
    ignoreUnknownKeys = true
}

internal fun RpcConfig.dataToString(): String {
    return json.encodeToString(this)
}


internal fun String.stringToData(): RpcConfig {
    return try {
        return json.decodeFromString(this)
    } catch (ex: Exception) {
        RpcConfig()
    }
}
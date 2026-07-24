/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.data.utils

import android.content.Context
import android.content.pm.PackageManager

data class AppsInfo(
    val name: String,
    val pkg: String,
    val isChecked: Boolean = false,
)

fun getInstalledApps(
    context: Context,
    isEnabled: (String) -> Boolean,
): List<AppsInfo> {
    val pm = context.packageManager
    val installedApps = pm.getInstalledApplications(PackageManager.GET_GIDS)
    val appDetailsList = installedApps
        .asSequence()
        .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
        .map { app ->
            AppsInfo(
                name = app.loadLabel(pm).toString(),
                pkg = app.packageName,
                isChecked = isEnabled(app.packageName)
            )
        }
        .sortedBy { it.name }
        .toList()
    return appDetailsList
}
/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

@file:Suppress("DEPRECATION")

package com.my.zyro.feature_rpc_base

import android.app.ActivityManager
import android.content.Context
import com.my.zyro.feature_rpc_base.services.AppDetectionService
import com.my.zyro.feature_rpc_base.services.CustomRpcService
import com.my.zyro.feature_rpc_base.services.ExperimentalRpc
import com.my.zyro.feature_rpc_base.services.MediaRpcService
import javax.inject.Singleton

@Singleton
object AppUtils {
    private lateinit var activityManager: ActivityManager
    fun init(context: Context) {
        activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }
    fun appDetectionRunning(): Boolean {
        return checkForRunningService<AppDetectionService>()
    }

    fun mediaRpcRunning(): Boolean {
        return checkForRunningService<MediaRpcService>()
    }

    fun customRpcRunning(): Boolean {
        return checkForRunningService<CustomRpcService>()
    }

    fun experimentalRpcRunning(): Boolean {
        return checkForRunningService<ExperimentalRpc>()
    }
    private inline fun <reified T : Any> checkForRunningService(): Boolean {
        for (runningServiceInfo in activityManager.getRunningServices(
            Int.MAX_VALUE
        )) {
            if (T::class.java.name == runningServiceInfo.service.className)
                return true
        }
        return false
    }
}
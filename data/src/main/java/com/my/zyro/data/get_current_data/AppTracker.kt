/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.data.get_current_data

import android.util.Log
import com.my.zyro.data.get_current_data.app.GetCurrentlyRunningApp
import com.my.zyro.data.get_current_data.media.GetCurrentPlayingMedia
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject

class AppTracker @Inject constructor(
    private val getCurrentlyRunningApp: GetCurrentlyRunningApp,
    private val getCurrentPlayingMedia: GetCurrentPlayingMedia
) {
    fun getCurrentAppData() = flow {
        while (currentCoroutineContext().isActive) {
            val getCurrentMedia = getCurrentPlayingMedia()
            if (getCurrentMedia.name.isNotEmpty()) {
                emit(getCurrentMedia)
            } else {
                val getCurrentApp = getCurrentlyRunningApp()
                if (getCurrentApp.name.isNotEmpty()) {
                    emit(getCurrentApp)
                }
            }
            delay(5000)
        }
    }.catch { exception ->
        Log.e("Error", exception.message.toString())
    }
}


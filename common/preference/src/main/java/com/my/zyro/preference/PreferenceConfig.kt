/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.preference

import android.content.Context
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

object PreferenceConfig {
    lateinit var applicationScope: CoroutineScope

    fun apply(context: Context) {
        applicationScope = CoroutineScope(SupervisorJob())
        MMKV.initialize(context)
        Prefs.checkAndAutoDeleteSavedImages()
    }
}
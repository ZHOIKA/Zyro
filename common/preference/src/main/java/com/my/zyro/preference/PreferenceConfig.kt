/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * PreferenceApp.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
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
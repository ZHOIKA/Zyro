/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * Config.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.feature_crash_handler

import com.developer.crashx.config.CrashConfig

object CrashHandlerConfig {
    fun apply() {
        CrashConfig.Builder.create()
            .errorActivity(CrashHandler::class.java)
            .apply()
    }
}
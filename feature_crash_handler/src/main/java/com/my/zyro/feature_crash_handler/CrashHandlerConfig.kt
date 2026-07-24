/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
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
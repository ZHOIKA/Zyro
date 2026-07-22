/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * CrashHandler.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.feature_crash_handler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.developer.crashx.CrashActivity
import com.my.zyro.ui.theme.ZyroTheme
import com.my.zyro.ui.theme.LocalDarkTheme
import com.my.zyro.ui.theme.LocalDynamicColorSwitch

class CrashHandler : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val report = CrashActivity.getStackTraceFromIntent(intent)
        setContent {
            ZyroTheme(
                darkTheme = LocalDarkTheme.current.isDarkTheme(),
                isHighContrastModeEnabled = LocalDarkTheme.current.isHighContrastModeEnabled,
                isDynamicColorEnabled = LocalDynamicColorSwitch.current,
            ){
                CrashScreen(trace = report.buildCrashLog())
            }
        }
    }
    private fun String?.buildCrashLog(): String {
        return """Zyro crash report
Manufacturer: ${DeviceUtils.getManufacturer()}
Device: ${DeviceUtils.getModel()}
Android version: ${DeviceUtils.getSDKVersionName()}
App version: ${AppUtils.getAppVersionName()} (${AppUtils.getAppVersionCode()})
Stacktrace: 
$this"""
    }
}
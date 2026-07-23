package com.my.zyro

import android.app.Application
import android.util.Log
import com.google.android.material.color.DynamicColors
import com.my.zyro.feature_crash_handler.CrashHandlerConfig
import com.my.zyro.feature_logs.LoggerProvider
import com.my.zyro.preference.PreferenceConfig
import com.my.zyro.feature_rpc_base.AppUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            DynamicColors.applyToActivitiesIfAvailable(this)
            CrashHandlerConfig.apply()
            PreferenceConfig.apply(this)
            LoggerProvider.init()
            AppUtils.init(this)
            Log.d("App", "Application initialized successfully")
        } catch (e: Exception) {
            Log.e("App", "Error during application initialization", e)
            // Continue execution to prevent crash
        }
    }
}

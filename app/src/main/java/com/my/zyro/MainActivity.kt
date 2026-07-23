package com.my.zyro

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.my.zyro.BuildConfig
import com.my.zyro.domain.model.toVersion
import com.my.zyro.domain.use_case.check_for_update.CheckForUpdateUseCase
import com.my.zyro.preference.getLanguageConfig
import com.my.zyro.ui.theme.LocalDarkTheme
import com.my.zyro.ui.theme.LocalDynamicColorSwitch
import com.my.zyro.ui.theme.SettingsProvider
import com.my.zyro.ui.theme.ZyroTheme
import com.my.zyro.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var checkForUpdateUseCase: CheckForUpdateUseCase

    private lateinit var usageAccessStatus: MutableState<Boolean>
    private lateinit var notificationListenerAccess: MutableState<Boolean>

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usageAccessStatus = mutableStateOf(this.hasUsageAccess())
        notificationListenerAccess = mutableStateOf(this.hasNotificationAccess())
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            v.setPadding(0, 0, 0, 0)
            insets
        }
        
        // Set locale on main thread (non-blocking operation)
        if (Build.VERSION.SDK_INT < 33) {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(getLanguageConfig())
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            checkForUpdateUseCase().collect { result ->
                if (result is com.my.zyro.domain.model.Resource.Success) {
                    val release = result.data
                    if (release != null && release.toVersion() > BuildConfig.VERSION_NAME.toVersion()) {
                        NotificationHelper.showUpdateNotification(this@MainActivity)
                    }
                }
            }
        }

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            SettingsProvider(windowSizeClass.widthSizeClass) {
                ZyroTheme(
                    darkTheme = LocalDarkTheme.current.isDarkTheme(),
                    isHighContrastModeEnabled = LocalDarkTheme.current.isHighContrastModeEnabled,
                    isDynamicColorEnabled = LocalDynamicColorSwitch.current,
                ) {
                    Zyro(
                        usageAccessStatus = usageAccessStatus,
                        notificationListenerAccess = notificationListenerAccess,
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        notificationListenerAccess.value = hasNotificationAccess()
        usageAccessStatus.value = hasUsageAccess()
    }

    @Suppress("DEPRECATION")
    private fun Context.hasUsageAccess(): Boolean {
        return try {
            val packageManager: PackageManager = this.packageManager
            val applicationInfo = packageManager.getApplicationInfo(this.packageName, 0)
            val appOpsManager = this.getSystemService(APP_OPS_SERVICE) as AppOpsManager
            val mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun Context.hasNotificationAccess(): Boolean {
        val enabledNotificationListeners = Settings.Secure.getString(
            this.contentResolver, "enabled_notification_listeners"
        )
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(this.packageName)
    }

    companion object {
        fun setLanguage(locale: String) {
            val localeListCompat =
                if (locale.isEmpty()) LocaleListCompat.getEmptyLocaleList()
                else LocaleListCompat.forLanguageTags(locale)
            AppCompatDelegate.setApplicationLocales(localeListCompat)
        }
    }
}

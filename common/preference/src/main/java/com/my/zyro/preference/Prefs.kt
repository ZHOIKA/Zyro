/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.preference

import com.my.zyro.domain.model.release.Release
import com.my.zyro.domain.model.user.User
import com.tencent.mmkv.MMKV
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.hours

/**
 * Zyro Preferences Manager
 * Manages application settings and user data persistence via MMKV
 * Reimplemented from scratch with independent key naming and logic
 */
object Prefs {
    @PublishedApi
    internal val kv = MMKV.defaultMMKV()
    
    // ============== Generic Key-Value Operations ==============
    
    /**
     * Store any supported type in preferences
     * Supports: String, Int, Boolean, Float, Long
     */
    operator fun set(key: String, value: Any?) {
        when (value) {
            is String? -> kv.encode(key, value)
            is Int -> kv.encode(key, value)
            is Boolean -> kv.encode(key, value)
            is Float -> kv.encode(key, value)
            is Long -> kv.encode(key, value)
            else -> throw UnsupportedOperationException("Type not supported: ${value?.javaClass?.simpleName}")
        }
    }

    /**
     * Retrieve any supported type from preferences with optional default value
     */
    inline operator fun <reified T : Any> get(
        key: String,
        defaultValue: T? = null,
    ): T {
        return when (T::class) {
            String::class -> kv.decodeString(key, defaultValue as String? ?: "") as T
            Int::class -> kv.decodeInt(key, defaultValue as? Int ?: -1) as T
            Boolean::class -> kv.decodeBool(key, defaultValue as? Boolean ?: false) as T
            Float::class -> kv.decodeFloat(key, defaultValue as? Float ?: -1f) as T
            Long::class -> kv.decodeLong(key, defaultValue as? Long ?: -1L) as T
            else -> throw UnsupportedOperationException("Type not supported: ${T::class.simpleName}")
        }
    }

    /**
     * Remove a specific key from preferences
     */
    fun remove(key: String) {
        kv.removeValueForKey(key)
    }

    // ============== Application Settings ==============
    
    fun getUser(): User? {
        val userJson = get(KEY_APP_USER, "")
        return if (userJson.isNotEmpty()) {
            try {
                Json.decodeFromString(userJson)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    fun getSavedLatestRelease(): Release? {
        val json = get(KEY_APP_LATEST_RELEASE, "")
        return if (json.isNotEmpty()) {
            try {
                Json.decodeFromString(json)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    fun saveLatestRelease(release: Release) {
        set(KEY_APP_LATEST_RELEASE, Json.encodeToString(release))
    }

    fun checkAndAutoDeleteSavedImages() {
        val lastDeleted = get(
            key = KEY_CACHE_LAST_IMAGE_CLEANUP,
            defaultValue = System.currentTimeMillis() - 24.hours.inWholeMilliseconds
        )
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastDeleted > 24.hours.inWholeMilliseconds) {
            remove(KEY_CACHE_SAVED_IMAGES)
            remove(KEY_CACHE_ARTWORK)
            set(KEY_CACHE_LAST_IMAGE_CLEANUP, currentTime)
        }
    }

    // ============== Enabled Apps Management ==============
    
    fun isAppEnabled(packageName: String?): Boolean {
        val apps = get(KEY_RPC_ENABLED_APPS, "[]")
        val enabledPackages: ArrayList<String> = try {
            Json.decodeFromString(apps)
        } catch (e: Exception) {
            ArrayList()
        }
        return enabledPackages.contains(packageName)
    }

    fun toggleAppEnabled(pkg: String) {
        val apps = get(KEY_RPC_ENABLED_APPS, "[]")
        val enabledPackages: ArrayList<String> = try {
            Json.decodeFromString(apps)
        } catch (e: Exception) {
            ArrayList()
        }
        
        if (enabledPackages.contains(pkg)) {
            enabledPackages.remove(pkg)
        } else {
            enabledPackages.add(pkg)
        }
        set(KEY_RPC_ENABLED_APPS, Json.encodeToString(enabledPackages))
    }

    // ============== Media RPC Apps ==============
    
    fun isMediaAppEnabled(packageName: String?): Boolean {
        val apps = get(KEY_MEDIA_ENABLED_APPS, Json.encodeToString(DEFAULT_MEDIA_APPS))
        val enabledPackages: ArrayList<String> = try {
            Json.decodeFromString(apps)
        } catch (e: Exception) {
            DEFAULT_MEDIA_APPS.toMutableList() as ArrayList<String>
        }
        return enabledPackages.contains(packageName)
    }

    fun toggleMediaApp(pkg: String) {
        val apps = get(KEY_MEDIA_ENABLED_APPS, Json.encodeToString(DEFAULT_MEDIA_APPS))
        val enabledPackages: ArrayList<String> = try {
            Json.decodeFromString(apps)
        } catch (e: Exception) {
            DEFAULT_MEDIA_APPS.toMutableList() as ArrayList<String>
        }
        
        if (enabledPackages.contains(pkg)) {
            enabledPackages.remove(pkg)
        } else {
            enabledPackages.add(pkg)
        }
        set(KEY_MEDIA_ENABLED_APPS, Json.encodeToString(enabledPackages))
    }

    // ============== Experimental RPC Apps ==============
    
    fun isExperimentalAppEnabled(packageName: String?): Boolean {
        val apps = get(KEY_EXP_ENABLED_APPS, "[]")
        val enabledPackages: ArrayList<String> = try {
            Json.decodeFromString(apps)
        } catch (e: Exception) {
            ArrayList()
        }
        return enabledPackages.contains(packageName)
    }

    fun toggleExperimentalApp(pkg: String) {
        val apps = get(KEY_EXP_ENABLED_APPS, "[]")
        val enabledPackages: ArrayList<String> = try {
            Json.decodeFromString(apps)
        } catch (e: Exception) {
            ArrayList()
        }
        
        if (enabledPackages.contains(pkg)) {
            enabledPackages.remove(pkg)
        } else {
            enabledPackages.add(pkg)
        }
        set(KEY_EXP_ENABLED_APPS, Json.encodeToString(enabledPackages))
    }

    // ============== Activity Type Management ==============
    
    fun saveAppActivityType(packageName: String, activityType: Int) {
        val json = get(KEY_EXP_APP_ACTIVITY_TYPES, "{}")
        val map: MutableMap<String, Int> = try {
            Json.decodeFromString(json)
        } catch (_: Exception) {
            mutableMapOf()
        }
        map[packageName] = activityType
        set(KEY_EXP_APP_ACTIVITY_TYPES, Json.encodeToString(map))
    }

    fun getAppActivityTypes(): Map<String, Int> {
        val json = get(KEY_EXP_APP_ACTIVITY_TYPES, "{}")
        return try {
            Json.decodeFromString(json)
        } catch (_: Exception) {
            emptyMap()
        }
    }

    // ============== Preference Keys ==============
    
    // Application/User Data
    private const val KEY_APP_USER = "zyro_user_data"
    private const val KEY_APP_LATEST_RELEASE = "zyro_latest_release"
    
    // Authentication
    const val TOKEN = "zyro_auth_token"
    const val USER_ID = "zyro_user_id"
    const val USER_BIO = "zyro_user_bio"
    const val USER_NITRO = "zyro_user_nitro"
    
    // RPC Configuration Keys
    const val LAST_RUN_CONSOLE_RPC = "zyro_last_console_rpc"
    const val LAST_RUN_CUSTOM_RPC = "zyro_last_custom_rpc"
    const val CUSTOM_ACTIVITY_TYPE = "zyro_custom_activity_type"
    const val CUSTOM_ACTIVITY_STATUS = "zyro_custom_activity_status"
    const val CUSTOM_ACTIVITY_APPLICATION_ID = "zyro_custom_activity_app_id_"
    
    // App Selection
    private const val KEY_RPC_ENABLED_APPS = "zyro_rpc_enabled_apps"
    private const val KEY_MEDIA_ENABLED_APPS = "zyro_media_enabled_apps"
    private const val KEY_EXP_ENABLED_APPS = "zyro_exp_enabled_apps"
    
    // Media RPC Settings
    const val MEDIA_RPC_ARTIST_NAME = "zyro_media_artist"
    const val MEDIA_RPC_ALBUM_NAME = "zyro_media_album"
    const val MEDIA_RPC_APP_ICON = "zyro_media_app_icon"
    const val MEDIA_RPC_ENABLE_TIMESTAMPS = "zyro_media_timestamps"
    const val MEDIA_RPC_HIDE_ON_PAUSE = "zyro_media_hide_pause"
    const val MEDIA_RPC_SHOW_PLAYBACK_STATE = "zyro_media_playback_state"
    const val MEDIA_RPC_SHOW_SONG_AS_TITLE = "zyro_media_song_title"
    
    // Custom RPC Settings
    const val USE_RPC_BUTTONS = "zyro_use_rpc_buttons"
    const val RPC_BUTTONS_DATA = "zyro_rpc_buttons"
    const val RPC_USE_LOW_RES_ICON = "zyro_low_res_icons"
    const val CONFIGS_DIRECTORY = "zyro_configs_dir"
    const val USE_IMGUR = "zyro_use_imgur"
    const val IMGUR_CLIENT_ID = "zyro_imgur_id"
    
    // Appearance & UI
    const val DARK_THEME = "zyro_dark_theme"
    const val HIGH_CONTRAST = "zyro_high_contrast"
    const val DYNAMIC_COLOR = "zyro_dynamic_color"
    const val THEME_COLOR = "zyro_theme_color"
    const val CUSTOM_THEME_COLOR = "zyro_custom_color"
    const val PALETTE_STYLE = "zyro_palette_style"
    const val LANGUAGE = "zyro_language"
    
    // UI Behavior
    const val IS_FIRST_LAUNCHED = "zyro_first_launch"
    const val SHOW_LOGS_IN_COMPACT_MODE = "zyro_compact_logs"
    const val LOGS_AUTO_SCROLL = "zyro_logs_autoscroll"
    const val APPLY_FIELDS_FROM_LAST_RUN_RPC = "zyro_load_last_config"
    
    // Experimental RPC Settings
    private const val KEY_EXP_APP_ACTIVITY_TYPES = "zyro_exp_activity_types"
    const val EXPERIMENTAL_RPC_USE_APPS_RPC = "zyro_exp_use_apps"
    const val EXPERIMENTAL_RPC_USE_MEDIA_RPC = "zyro_exp_use_media"
    const val EXPERIMENTAL_RPC_TEMPLATE_NAME = "zyro_exp_template_name"
    const val EXPERIMENTAL_RPC_TEMPLATE_DETAILS = "zyro_exp_template_details"
    const val EXPERIMENTAL_RPC_TEMPLATE_STATE = "zyro_exp_template_state"
    const val EXPERIMENTAL_RPC_SHOW_COVER_ART = "zyro_exp_cover_art"
    const val EXPERIMENTAL_RPC_SHOW_APP_ICON = "zyro_exp_app_icon"
    const val EXPERIMENTAL_RPC_SHOW_PLAYBACK_STATE = "zyro_exp_playback_state"
    const val EXPERIMENTAL_RPC_ENABLE_TIMESTAMPS = "zyro_exp_timestamps"
    const val EXPERIMENTAL_RPC_HIDE_ON_PAUSE = "zyro_exp_hide_pause"
    
    // Cache & Cleanup
    private const val KEY_CACHE_SAVED_IMAGES = "zyro_cache_images"
    private const val KEY_CACHE_ARTWORK = "zyro_cache_artwork"
    private const val KEY_CACHE_LAST_IMAGE_CLEANUP = "zyro_cache_cleanup_time"

    // ============== Default Media Apps List ==============
    
    val DEFAULT_MEDIA_APPS: List<String> = listOf(
        // Music streaming services
        "com.google.android.apps.youtube.music", "com.spotify.music", "com.google.android.music",
        "com.amazon.mp3", "com.apple.android.music", "com.soundcloud.android", "deezer.android.app",
        "com.jrtstudio.AnotherMusicPlayer", "com.pandora.android", "com.rhapsody", "com.sonyericsson.music",
        "com.aspiro.tidal", "com.sec.android.app.music", "com.tbig.playerpro",
        
        // Video streaming platforms
        "com.google.android.apps.youtube.kids", "com.google.android.apps.youtube.unplugged",
        "com.google.android.youtube.googletv", "com.google.android.youtube.tv", "com.google.android.youtube",
        "com.netflix.mediaclient", "com.kick.mobile", "tv.twitch.android.app",
        
        // Video players
        "com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.pro", "com.google.android.apps.mediashell",
        "com.google.android.videos", "org.videolan.vlc",
    )
}

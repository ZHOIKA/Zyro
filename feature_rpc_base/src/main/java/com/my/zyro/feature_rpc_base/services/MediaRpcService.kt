/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_rpc_base.services

import android.annotation.SuppressLint
import android.app.*
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import com.my.zyro.data.get_current_data.media.GetCurrentPlayingMedia
import com.my.zyro.data.rpc.ZyroRPC
import com.my.zyro.domain.interfaces.Logger
import com.my.zyro.domain.model.rpc.RpcButtons
import com.my.zyro.feature_rpc_base.Constants
import com.my.zyro.feature_rpc_base.setLargeIcon
import com.my.zyro.preference.Prefs
import com.my.zyro.preference.Prefs.MEDIA_RPC_ENABLE_TIMESTAMPS
import com.my.zyro.preference.Prefs.TOKEN
import com.my.zyro.resources.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Zyro Media RPC Service
 * Monitors active media sessions and broadcasts Discord Rich Presence
 * Completely reimplemented media detection and presence update logic
 */
@AndroidEntryPoint
class MediaRpcService : Service() {

    // ============== Dependencies ==============

    @Inject
    lateinit var zyroRPC: ZyroRPC

    @Inject
    lateinit var serviceScope: CoroutineScope

    @Inject
    lateinit var mediaDataProvider: GetCurrentPlayingMedia

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: Notification.Builder

    // ============== Internal State ==============

    private var deviceWakeLock: WakeLock? = null
    private lateinit var mediaSession: MediaSessionManager
    private var activeMediaController: MediaController? = null

    /**
     * Service initialization - setup prerequisites and start monitoring
     */
    @SuppressLint("WakelockTimeout")
    override fun onCreate() {
        super.onCreate()
        
        // Verify authentication token exists
        val authToken = Prefs[TOKEN, ""]
        if (authToken.isEmpty()) {
            logger.i("MediaRPC", "No authentication token, stopping service")
            stopSelf()
            return
        }
        
        // ============== Wakelock Setup ==============
        
        initializeWakeLock()

        // ============== Notification Setup ==============

        val stopAction = Intent(this, MediaRpcService::class.java).apply {
            action = Constants.ACTION_STOP_SERVICE
        }
        val stopPending = PendingIntent.getService(this, 0, stopAction, PendingIntent.FLAG_IMMUTABLE)

        val restartAction = Intent(this, MediaRpcService::class.java).apply {
            action = Constants.ACTION_RESTART_SERVICE
        }
        val restartPending = PendingIntent.getService(this, 0, restartAction, PendingIntent.FLAG_IMMUTABLE)

        val serviceNotification = notificationBuilder
            .setSmallIcon(R.drawable.ic_media_rpc)
            .setContentText(getString(R.string.idling_notification))
            .addAction(R.drawable.ic_media_rpc, getString(R.string.restart), restartPending)
            .addAction(R.drawable.ic_media_rpc, getString(R.string.exit), stopPending)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startForeground(Constants.NOTIFICATION_ID, serviceNotification, FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            @Suppress("DEPRECATION")
            startForeground(Constants.NOTIFICATION_ID, serviceNotification)
        }

        // ============== Media Session Monitoring Setup ==============

        mediaSession = getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager
        
        val notificationListenerComponent = ComponentName(this, NotificationListener::class.java)
        mediaSession.addOnActiveSessionsChangedListener(
            ::onMediaSessionsChanged,
            notificationListenerComponent
        )

        // Initial session registration
        val initialSessions = mediaSession.getActiveSessions(notificationListenerComponent)
        onMediaSessionsChanged(initialSessions, isEvent = false)
    }

    /**
     * Initializes device wakelock to prevent service suspension during media monitoring
     */
    @SuppressLint("WakelockTimeout")
    private fun initializeWakeLock() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        deviceWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "zyro:media_rpc_service")
        deviceWakeLock?.acquire()
    }

    /**
     * Fetches current media metadata and updates Discord presence payload
     */
    private suspend fun updateMediaPresence() {
        try {
            val shouldIncludeTime = Prefs[MEDIA_RPC_ENABLE_TIMESTAMPS, false]
            val currentMedia = mediaDataProvider()

            // Update service notification with media info
            notificationManager.notify(
                Constants.NOTIFICATION_ID,
                notificationBuilder
                    .setContentTitle(currentMedia.name.ifEmpty { getString(R.string.app_name) })
                    .setContentText(
                        (currentMedia.details ?: "").ifEmpty { getString(R.string.idling_notification) }
                    )
                    .setLargeIcon(
                        rpcImage = currentMedia.largeImage,
                        context = this@MediaRpcService
                    )
                    .build()
            )

            // Retrieve saved button configuration
            val buttonConfigJson = Prefs[Prefs.RPC_BUTTONS_DATA, "{}"]
            val buttonConfig = try {
                Json.decodeFromString<RpcButtons>(buttonConfigJson)
            } catch (e: Exception) {
                RpcButtons()
            }

            // Update or start RPC based on current state
            if (zyroRPC.isRpcRunning()) {
                if (currentMedia.name.isBlank()) {
                    logger.d("MediaRPC", "Media empty, closing RPC")
                    zyroRPC.closeRPC()
                } else {
                    zyroRPC.updateRPC(currentMedia, shouldIncludeTime)
                }
            } else {
                if (currentMedia.name.isBlank()) {
                    logger.d("MediaRPC", "Skipping RPC start with empty media")
                    return
                }
                
                // Build new presence payload
                zyroRPC.apply {
                    resetState()
                    
                    setName(currentMedia.name)
                    setType(Prefs[Prefs.CUSTOM_ACTIVITY_TYPE, 0])
                    setDetails(currentMedia.details)
                    setState(currentMedia.state)
                    
                    // Include timestamps if enabled
                    if (shouldIncludeTime) {
                        setStartTimestamps(currentMedia.time?.start)
                        setStopTimestamps(currentMedia.time?.end)
                    }
                    
                    setStatus(Prefs[Prefs.CUSTOM_ACTIVITY_STATUS, "dnd"])
                    setLargeImage(currentMedia.largeImage, currentMedia.largeText)
                    setSmallImage(currentMedia.smallImage, currentMedia.smallText)
                    
                    // Attach buttons if configured
                    if (Prefs[Prefs.USE_RPC_BUTTONS, false]) {
                        setButton1(buttonConfig.button1.takeIf { it.isNotEmpty() })
                        setButton1URL(buttonConfig.button1Url.takeIf { it.isNotEmpty() })
                        setButton2(buttonConfig.button2.takeIf { it.isNotEmpty() })
                        setButton2URL(buttonConfig.button2Url.takeIf { it.isNotEmpty() })
                    }
                    
                    build()
                }
            }
        } catch (e: Exception) {
            logger.e("MediaRPC", "Presence update failed: ${e.message}")
        }
    }

    /**
     * Handles media session list changes
     * Registers callback for new active session or unregisters when none available
     */
    private fun onMediaSessionsChanged(
        sessions: List<MediaController>?,
        isEvent: Boolean = true
    ) {
        logger.d("MediaRPC", "Media sessions updated. Count: ${sessions?.size ?: 0}")

        // Debounce: event may fire before system updates session list
        if (isEvent) {
            runBlocking { delay(1500) }
        }

        // Unregister old callback and register new one
        activeMediaController?.unregisterCallback(mediaCallback)
        
        if (sessions?.isNotEmpty() == true) {
            val notificationListenerComponent = ComponentName(this, NotificationListener::class.java)
            activeMediaController = mediaSession.getActiveSessions(notificationListenerComponent).firstOrNull()
            activeMediaController?.registerCallback(mediaCallback)
        } else {
            activeMediaController = null
        }

        // Trigger presence update
        serviceScope.coroutineContext.cancelChildren()
        serviceScope.launch { updateMediaPresence() }
    }

    /**
     * Callback for media controller state changes
     */
    private inner class MediaPlaybackCallback : MediaController.Callback() {
        
        override fun onPlaybackStateChanged(state: PlaybackState?) {
            super.onPlaybackStateChanged(state)
            logger.d("MediaRPC", "Playback state changed")
            
            serviceScope.coroutineContext.cancelChildren()
            serviceScope.launch {
                delay(1000)
                updateMediaPresence()
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadata?) {
            super.onMetadataChanged(metadata)
            logger.d("MediaRPC", "Media metadata changed")
            
            serviceScope.coroutineContext.cancelChildren()
            serviceScope.launch {
                delay(1000)
                updateMediaPresence()
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            logger.d("MediaRPC", "Media session destroyed")
            
            serviceScope.coroutineContext.cancelChildren()
            serviceScope.launch { updateMediaPresence() }
        }
    }

    private val mediaCallback = MediaPlaybackCallback()

    /**
     * Handles service start commands (stop/restart requests)
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (action) {
                Constants.ACTION_STOP_SERVICE -> {
                    stopSelf()
                    return START_NOT_STICKY
                }
                Constants.ACTION_RESTART_SERVICE -> {
                    stopSelf()
                    startService(Intent(this, MediaRpcService::class.java))
                    return START_NOT_STICKY
                }
            }
        }
        return START_STICKY
    }

    /**
     * Service cleanup on destruction
     */
    override fun onDestroy() {
        try {
            mediaSession.removeOnActiveSessionsChangedListener(::onMediaSessionsChanged)
            activeMediaController?.unregisterCallback(mediaCallback)
        } catch (e: Exception) {
            logger.e("MediaRPC", "Cleanup error: ${e.message}")
        }
        
        // Cleanup RPC and resources
        zyroRPC.closeRPC()
        serviceScope.cancel()
        
        deviceWakeLock?.let {
            if (it.isHeld) it.release()
        }
        
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

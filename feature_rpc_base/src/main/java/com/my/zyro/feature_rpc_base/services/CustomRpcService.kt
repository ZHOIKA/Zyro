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
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import com.my.zyro.data.utils.toRpcImage
import com.my.zyro.domain.model.rpc.RpcConfig
import com.my.zyro.feature_rpc_base.Constants
import com.my.zyro.feature_rpc_base.setLargeIcon
import com.my.zyro.resources.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Zyro Custom RPC Service
 * Manages custom Discord Rich Presence via foreground service
 * Completely reimplemented with original lifecycle management
 */
@AndroidEntryPoint
class CustomRpcService : Service() {
    
    private var customRpcConfig: RpcConfig? = null
    private var deviceWakeLock: WakeLock? = null

    @Inject
    lateinit var zyroRPC: com.my.zyro.data.rpc.ZyroRPC

    @Inject
    lateinit var serviceScope: CoroutineScope

    @Inject
    lateinit var notificationBuilder: Notification.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    companion object {
        private const val WAKELOCK_TAG = "zyro:custom_rpc_service"
    }

    /**
     * Processes incoming service commands and starts RPC transmission
     * Manages notification, wakelock, and RPC payload serialization
     */
    @SuppressLint("WakelockTimeout")
    @Suppress("DEPRECATION")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Check if service stop requested
        if (intent?.action.equals(Constants.ACTION_STOP_SERVICE)) {
            stopSelf()
            return START_NOT_STICKY
        }

        // Deserialize RPC configuration from intent
        intent?.getStringExtra("RPC")?.let { rpcJson ->
            try {
                customRpcConfig = Json.decodeFromString(rpcJson)
            } catch (e: Exception) {
                return START_NOT_STICKY
            }
        }

        // ============== Notification Setup ==============
        
        val stopServiceIntent = Intent(this, CustomRpcService::class.java).apply {
            action = Constants.ACTION_STOP_SERVICE
        }
        
        val stopPendingIntent: PendingIntent = PendingIntent.getService(
            this, 0, stopServiceIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationContent = notificationBuilder
            .setContentTitle(getString(R.string.custom_rpc_running))
            .setContentText(customRpcConfig?.name ?: "Custom RPC")
            .setSmallIcon(R.drawable.ic_rpc_placeholder)
            .addAction(R.drawable.ic_rpc_placeholder, getString(R.string.exit), stopPendingIntent)
            .build()

        // ============== Foreground Service Setup ==============
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startForeground(
                Constants.NOTIFICATION_ID,
                notificationContent,
                FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            @Suppress("DEPRECATION")
            startForeground(Constants.NOTIFICATION_ID, notificationContent)
        }

        // ============== Wakelock Acquisition ==============
        
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        deviceWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG)
        deviceWakeLock?.acquire()

        // ============== RPC Configuration & Transmission ==============
        
        serviceScope.launch {
            // Update notification with large icon
            notificationManager.notify(
                Constants.NOTIFICATION_ID,
                notificationBuilder
                    .setLargeIcon(
                        rpcImage = customRpcConfig?.largeImg?.toRpcImage(),
                        context = this@CustomRpcService
                    )
                    .build()
            )

            // Build and transmit presence payload
            customRpcConfig?.let { config ->
                zyroRPC.apply {
                    // Reset all state from previous RPC instances
                    resetState()
                    
                    // Configure presence fields
                    setName(config.name.takeIf { it.isNotEmpty() } ?: "")
                    setDetails(config.details.takeIf { it.isNotEmpty() })
                    setState(config.state.takeIf { it.isNotEmpty() })
                    
                    // Party configuration
                    val currentParty = config.partyCurrentSize.toIntOrNull()
                    val maxParty = config.partyMaxSize.toIntOrNull()
                    setPartySize(currentParty, maxParty)
                    
                    // Status and type
                    setStatus(config.status.takeIf { it.isNotEmpty() } ?: "online")
                    setType(config.type.toIntOrNull() ?: 0)
                    setPlatform(config.platform.takeIf { it.isNotEmpty() })
                    
                    // Timestamps
                    setStartTimestamps(config.timestampsStart.toLongOrNull())
                    setStopTimestamps(config.timestampsStop.toLongOrNull())
                    
                    // Action buttons
                    setButton1(config.button1.takeIf { it.isNotEmpty() })
                    setButton1URL(config.button1link.takeIf { it.isNotEmpty() })
                    setButton2(config.button2.takeIf { it.isNotEmpty() })
                    setButton2URL(config.button2link.takeIf { it.isNotEmpty() })
                    
                    // Images and stream
                    setLargeImage(config.largeImg.toRpcImage(), config.largeText)
                    setSmallImage(config.smallImg.toRpcImage(), config.smallText)
                    setStreamUrl(config.url.takeIf { it.isNotEmpty() })
                    
                    // Transmit to Discord
                    build()
                }
            }
        }

        return START_NOT_STICKY
    }

    /**
     * Cleanup when service is destroyed
     */
    override fun onDestroy() {
        // Stop RPC transmission
        zyroRPC.closeRPC()
        
        // Release wakelock
        deviceWakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
        
        // Cancel all coroutines
        serviceScope.cancel()
        
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

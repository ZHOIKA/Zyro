/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_rpc_base.di

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import com.my.zyro.feature_rpc_base.Constants
import com.my.zyro.feature_rpc_base.services.NotificationListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ServiceComponent::class)
object RpcBaseModule {

    @Provides
    fun providesComponentName(
        @ApplicationContext context: Context
    ) = ComponentName(context, NotificationListener::class.java)

    @Provides
    fun providesNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(NotificationManager::class.java)
    }

    @Provides
    fun providesNotificationBuilder(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager
    ): Notification.Builder {
        val channel = NotificationChannel(
            Constants.CHANNEL_ID,
            Constants.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = Constants.CHANNEL_DESCRIPTION
            setShowBadge(false)
        }
        notificationManager.createNotificationChannel(channel)
        return Notification.Builder(context, Constants.CHANNEL_ID)
    }
}

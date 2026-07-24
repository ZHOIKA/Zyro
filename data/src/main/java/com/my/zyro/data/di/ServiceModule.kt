/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.data.di

import com.my.zyro.data.rpc.ZyroRPC
import com.my.zyro.domain.interfaces.Logger
import com.my.zyro.domain.repository.ZyroRepository
import com.my.zyro.preference.Prefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import zyro.gateway.DiscordWebSocket
import zyro.gateway.DiscordWebSocketImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {
    @Provides
    fun providesDiscordWebsocket(
        logger: Logger
    ): DiscordWebSocket =
        DiscordWebSocketImpl(Prefs[Prefs.TOKEN, ""], logger)

    @Provides
    fun provideZyroRpc(
        zyroRepository: ZyroRepository,
        discordWebSocket: DiscordWebSocket,
        logger: Logger
    ) = ZyroRPC(Prefs[Prefs.TOKEN, ""], zyroRepository, discordWebSocket, logger)

    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
}
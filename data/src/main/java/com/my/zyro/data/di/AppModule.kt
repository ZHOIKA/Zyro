/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.data.di

import com.my.zyro.data.BuildConfig
import com.my.zyro.data.remote.ApiService
import com.my.zyro.data.remote.Base
import com.my.zyro.data.remote.Discord
import com.my.zyro.data.remote.Github
import com.my.zyro.data.remote.Imgur
import com.my.zyro.data.remote.ImgurApiService
import com.my.zyro.data.repository.ZyroRepositoryImpl
import com.my.zyro.domain.repository.ZyroRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @Base
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    @Discord
    fun provideDiscordBaseUrl() = BuildConfig.DISCORD_API_BASE_URL

    @Provides
    @Singleton
    @Github
    fun provideGithubBaseUrl() = BuildConfig.GITHUB_API_BASE_URL

    @Provides
    @Singleton
    @Imgur
    fun provideImgurBaseUrl() = BuildConfig.IMGUR_API_BASE_URL

    @Provides
    fun provideJson() = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    fun provideHttpClient(
        json: Json,
        kLogger: com.my.zyro.domain.interfaces.Logger
    ): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json)
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 30_000
                requestTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
            install(Logging) {
                level = LogLevel.HEADERS
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
                logger = object : Logger {
                    override fun log(message: String) {
                        kLogger.d("Ktor", message)
                    }
                }
            }
        }
    }

    @Provides
    fun providesZyroRepository(
        apiService: ApiService,
        imgurApiService: ImgurApiService
    ): ZyroRepository {
        return ZyroRepositoryImpl(apiService, imgurApiService)
    }
}
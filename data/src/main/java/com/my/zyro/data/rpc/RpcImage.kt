/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.data.rpc

import android.content.Context
import android.graphics.Bitmap
import com.my.zyro.domain.repository.ZyroRepository
import com.my.zyro.preference.Prefs
import com.my.zyro.data.utils.getAppInfo
import com.my.zyro.data.utils.toBitmap
import com.my.zyro.data.utils.toFile
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class RpcImage {
    abstract suspend fun resolveImage(repository: ZyroRepository): String?

    class DiscordImage(val image: String) : RpcImage() {
        override suspend fun resolveImage(repository: ZyroRepository): String {
            return "mp:${image}"
        }
    }

    class ExternalImage(val image: String) : RpcImage() {
        override suspend fun resolveImage(repository: ZyroRepository): String? {
            return repository.getImage(image)
        }
    }

    class ApplicationIcon(val packageName: String, private val context: Context) : RpcImage() {
        val data = Prefs[Prefs.SAVED_IMAGES, "{}"]
        private val savedImages: HashMap<String, String> = Json.decodeFromString(data)

        override suspend fun resolveImage(repository: ZyroRepository): String? {
            return if (savedImages.containsKey(packageName))
                savedImages[packageName]
            else
                retrieveImageFromApi(packageName, context, repository)
        }

        private suspend fun retrieveImageFromApi(
            packageName: String,
            context: Context,
            repository: ZyroRepository,
        ): String? {
            val applicationInfo = context.getAppInfo(packageName)
            val bitmap = applicationInfo.toBitmap(context)
            val response = repository.uploadImage(bitmap.toFile(context, "image"))
            response?.let {
                savedImages[packageName] = it
                Prefs[Prefs.SAVED_IMAGES] = Json.encodeToString(savedImages)
            }
            return response
        }
    }

    class BitmapImage(
        private val context: Context,
        val bitmap: Bitmap?,
        private val packageName: String,
        val title: String,
    ) : RpcImage() {
        override suspend fun resolveImage(repository: ZyroRepository): String? {
            val data = Prefs[Prefs.SAVED_ARTWORK, "{}"]
            val schema = "${this.packageName}:${this.title}"
            val savedImages = Json.decodeFromString<HashMap<String, String>>(data)
            return if (savedImages.containsKey(schema))
                savedImages[schema]
            else {
                val result = repository.uploadImage(bitmap.toFile(this.context, "art"))
                result?.let {
                    savedImages[schema] = it
                    Prefs[Prefs.SAVED_ARTWORK] = Json.encodeToString(savedImages)
                }
                result
            }
        }
    }
}

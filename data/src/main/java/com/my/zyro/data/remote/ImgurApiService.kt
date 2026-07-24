/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.data.remote

import com.my.zyro.data.rpc.Constants.APPLICATION_ID
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import java.io.File
import javax.inject.Inject

class ImgurApiService @Inject constructor(
    private val client: HttpClient,
    @Discord private val discordBaseUrl: String,
    @Imgur private val imgurBaseUrl: String,
) {
    suspend fun getImage(url: String, token: String) = runCatching {
        client.post {
            url("$discordBaseUrl/applications/$APPLICATION_ID/external-assets")
            headers {
                append(HttpHeaders.Authorization, token)
                append(HttpHeaders.ContentType, "application/json")
            }
            setBody(mapOf("urls" to arrayOf(url)))
        }
    }

    suspend fun uploadImage(file: File, clientId: String) = runCatching {
        client.post {
            url("$imgurBaseUrl/image")
            headers {
                append(HttpHeaders.Authorization, "Client-ID $clientId")
            }
            contentType(ContentType.MultiPart.FormData)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("image", file.readBytes())
                        append("type", "raw")
                    }
                )
            )
        }
    }
}
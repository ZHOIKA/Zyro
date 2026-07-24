/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.domain.model.release


import kotlinx.serialization.SerialName
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Asset(
    @SerialName("url")
    val url: String? = null,
    @SerialName("browser_download_url")
    val browserDownloadUrl: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("node_id")
    val nodeId: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("label")
    val label: String? = null,
    @SerialName("state")
    val state: String? = null,
    @SerialName("content_type")
    val contentType: String? = null,
    @SerialName("size")
    val size: Int? = null,
    @SerialName("download_count")
    val downloadCount: Int? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("uploader")
    val uploader: Uploader? = null
)
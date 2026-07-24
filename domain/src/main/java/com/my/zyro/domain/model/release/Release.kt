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
data class Release(
    @SerialName("url")
    val url: String? = null,
    @SerialName("html_url")
    val htmlUrl: String? = null,
    @SerialName("assets_url")
    val assetsUrl: String? = null,
    @SerialName("upload_url")
    val uploadUrl: String? = null,
    @SerialName("tarball_url")
    val tarballUrl: String? = null,
    @SerialName("zipball_url")
    val zipballUrl: String? = null,
    @SerialName("discussion_url")
    val discussionUrl: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("node_id")
    val nodeId: String? = null,
    @SerialName("tag_name")
    val tagName: String? = null,
    @SerialName("target_commitish")
    val targetCommitish: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("body")
    val body: String? = null,
    @SerialName("draft")
    val draft: Boolean? = null,
    @SerialName("prerelease")
    val prerelease: Boolean? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("published_at")
    val publishedAt: String? = null,
    @SerialName("author")
    val author: Author? = null,
    @SerialName("assets")
    val assets: List<Asset?>? = null
)
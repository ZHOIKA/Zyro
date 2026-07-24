/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.domain.model.rpc

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// data class used for saving/loading/previewing rpc in the app
@Serializable
@Immutable
data class RpcConfig(
    @SerialName("button1")
    val button1: String = "",
    @SerialName("button1link")
    val button1link: String = "",
    @SerialName("button2")
    val button2: String = "",
    @SerialName("button2link")
    val button2link: String = "",
    @SerialName("details")
    val details: String = "",
    @SerialName("largeImg")
    val largeImg: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("smallImg")
    val smallImg: String = "",
    @SerialName("state")
    val state: String = "",
    @SerialName("status")
    val status: String = "",
    @SerialName("timestampsStart")
    val timestampsStart: String = "",
    @SerialName("timestampsStop")
    val timestampsStop: String = "",
    @SerialName("type")
    val type: String = "",
    @SerialName("large_text")
    val largeText: String = "",
    @SerialName("small_text")
    val smallText: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("partyCurrentSize")
    val partyCurrentSize: String = "",
    @SerialName("partyMaxSize")
    val partyMaxSize: String = "",
    @SerialName("platform")
    val platform: String = "",
)
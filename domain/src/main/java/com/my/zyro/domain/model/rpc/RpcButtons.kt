/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.domain.model.rpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RpcButtons(
    @SerialName("button1")
    val button1: String = "",
    @SerialName("button2")
    val button2: String = "",
    @SerialName("button1Url")
    val button1Url: String = "",
    @SerialName("button2Url")
    val button2Url: String = ""
)
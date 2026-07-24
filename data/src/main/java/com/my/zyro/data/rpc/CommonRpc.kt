/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.data.rpc

data class CommonRpc(
    val name: String = "",
    val type: Int? = null,
    val details: String? = "",
    val state: String? = "",
    val partyCurrentSize: Int? = null,
    val partyMaxSize: Int? = null,
    val largeImage: RpcImage? = null,
    val smallImage: RpcImage? = null,
    var largeText: String? = null,
    var smallText: String? = null,
    val time: Timestamps? = null,
    val packageName: String = "",
    val platform: String? = null
)

/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package zyro.gateway.entities


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ready(
    @SerialName("resume_gateway_url")
    val resumeGatewayUrl: String? = null,
    @SerialName("session_id")
    val sessionId: String? = null,
    @SerialName("user")
    val user: ReadyUser? = null,
)

@Serializable
data class ReadyUser(
    @SerialName("id")
    val id: String? = null,
    @SerialName("bio")
    val bio: String? = null,
    @SerialName("premium_type")
    val premiumType: Int? = null,
)

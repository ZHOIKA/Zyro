/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.data.remote


import com.my.zyro.data.rpc.Constants
import com.my.zyro.domain.model.Game
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GamesResponse(
    @SerialName("label")
    val label: String,
    @SerialName("link")
    val link: String? = null,
    @SerialName("title")
    val title: String
)

fun GamesResponse.toGame() : Game {
    return Game(
        platform = when (label) {
            "nintendo" -> Constants.NINTENDO
            "wii" -> Constants.WII_U
            "nintendo-3ds" -> Constants.NINTENDO_3DS
            else -> Constants.XBOX
        },
        small_image = when (label) {
            "nintendo" -> Constants.NINTENDO_LINK
            "wii" -> Constants.WII_U_LINK
            "nintendo-3ds" -> Constants.N3DS_LINK
            else -> Constants.XBOX_LINK
        },
        large_image = link,
        game_title = title
    )
}
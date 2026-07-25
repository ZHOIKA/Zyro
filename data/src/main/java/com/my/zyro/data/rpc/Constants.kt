/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.data.rpc

import com.my.zyro.resources.R

object Constants {
    const val NINTENDO_LINK =
        "https://img.icons8.com/color/96/nintendo-switch.png"
    const val WII_U_LINK =
        "https://img.icons8.com/color/96/wii.png"
    const val XBOX_LINK =
        "https://img.icons8.com/color/96/xbox.png"
    const val N3DS_LINK =
        "https://img.icons8.com/color/96/3ds-console.png"
    const val NINTENDO = "Nintendo Switch"
    const val NINTENDO_3DS = "Nintendo-3DS"
    const val WII_U = "Wii-U"
    const val XBOX = "Xbox"
    const val APPLICATION_ID = "962990036020756480"
    const val IMGUR_CLIENT_ID = "d70305e7c3ac5c6"
    const val APP_DIRECTORY = "App Directory"
    const val DOWNLOADS_DIRECTORY = "Downloads Directory"
    const val MAX_ALLOWED_CHARACTER_LENGTH = 32
    /*
    See https://discord.com/developers/docs/reference#snowflakes
    */
    val MAX_APPLICATION_ID_LENGTH_RANGE = 18..19

    val ACTIVITY_TYPE = mapOf(
        "Playing" to 0,
        "Streaming" to 1,
        "Listening" to 2,
        "Watching" to 3,
        "Competing" to 5
    )
    val ACTIVITY_STATUS = mapOf(
        R.string.status_online to "online",
        R.string.status_idle to "idle",
        R.string.status_dnd to "dnd",
        R.string.status_offline to "offline",
        R.string.status_invisible_offline to "invisible"
    )

    val ACTIVITY_PLATFORMS = mapOf(
        "Android" to "android",
        "Desktop" to "desktop",
        "Embedded" to "embedded",
        "IOS" to "ios",
        "PlayStation 4" to "ps4",
        "PlayStation 5" to "ps5",
        "Samsung" to "samsung",
        "Xbox" to "xbox",
    )
}

data class PlatformOption(
    val displayName: String,
    val iconUrl: String,
)

val CONSOLE_PLATFORMS: List<PlatformOption> = listOf(
    PlatformOption(displayName = Constants.XBOX, iconUrl = Constants.XBOX_LINK),
    PlatformOption(displayName = Constants.NINTENDO, iconUrl = Constants.NINTENDO_LINK),
    PlatformOption(displayName = Constants.WII_U, iconUrl = Constants.WII_U_LINK),
    PlatformOption(displayName = Constants.NINTENDO_3DS, iconUrl = Constants.N3DS_LINK),
)


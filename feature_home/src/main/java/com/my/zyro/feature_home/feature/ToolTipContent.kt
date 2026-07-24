/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_home.feature

object ToolTipContent {
    private const val BASE_DOCS_URL = "https://zyrodocs.vercel.app/rpc"
    const val APP_DETECTION_DOCS = "Zyro allows you to share your current activity on Discord by detecting what app you're using on your device."
    const val MEDIA_RPC_DOCS = "Zyro allows you to share your current media activity on Discord, such as the song or video you're currently listening to or watching."
    const val CONSOLE_RPC_DOCS = "Zyro allows you to use some predefined presets to set your presence on Discord."
    const val CUSTOM_RPC_DOCS = "Zyro allows you to create your own fully customisable rich presence for Discord."
    const val EXPERIMENTAL_RPC_DOCS = "Experimental RPC is a feature in Zyro that constantly switches between Apps RPC and Media RPC."
    const val APP_DETECTION_DOCS_LINK = "$BASE_DOCS_URL/app"
    const val MEDIA_RPC_DOCS_LINK = "$BASE_DOCS_URL/media"
    const val CONSOLE_RPC_DOCS_LINK = "$BASE_DOCS_URL/console"
    const val CUSTOM_RPC_DOCS_LINK = "$BASE_DOCS_URL/custom"
    const val EXPERIMENTAL_RPC_DOCS_LINK = "$BASE_DOCS_URL/experimental"
}
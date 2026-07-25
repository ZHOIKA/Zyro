/*
 *  ******************************************************************
 *  * Copyright (C) 2024 - Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_rpc_base

object Constants {
    const val CHANNEL_ID = "zyro.notification"
    const val CHANNEL_NAME = "Zyro RPC"
    const val CHANNEL_DESCRIPTION = "Background Service notification which runs rpc"
    const val NOTIFICATION_ID = 2022_03_04
    const val ACTION_STOP_SERVICE = "Stop Service"
    const val ACTION_RESTART_SERVICE = "Restart Service"

    data class PlatformOption(
        val displayName: String,
        val iconUrl: String,
    )

    val CONSOLE_PLATFORMS = listOf(
        PlatformOption(displayName = "PlayStation 5", iconUrl = "https://i.imgur.com/ps5_icon.png"),
        PlatformOption(displayName = "PlayStation 4", iconUrl = "https://i.imgur.com/ps4_icon.png"),
        PlatformOption(displayName = "Xbox Series X", iconUrl = "https://i.imgur.com/xbox_series_x_icon.png"),
        PlatformOption(displayName = "Xbox One", iconUrl = "https://i.imgur.com/xbox_one_icon.png"),
        PlatformOption(displayName = "Nintendo Switch", iconUrl = "https://i.imgur.com/switch_icon.png"),
        PlatformOption(displayName = "PC", iconUrl = "https://i.imgur.com/pc_icon.png"),
        PlatformOption(displayName = "Mobile", iconUrl = "https://i.imgur.com/mobile_icon.png"),
    )
}


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
        val iconUrl: String = "",
        val activityPlatform: String? = null,
    )

    val CONSOLE_PLATFORMS = listOf(
        PlatformOption(displayName = "PlayStation 5", activityPlatform = "ps5"),
        PlatformOption(displayName = "PlayStation 4", activityPlatform = "ps4"),
        PlatformOption(displayName = "Xbox Series X", activityPlatform = "xbox"),
        PlatformOption(displayName = "Xbox One", activityPlatform = "xbox"),
        PlatformOption(displayName = "Nintendo Switch"),
        PlatformOption(displayName = "PC", activityPlatform = "desktop"),
        PlatformOption(displayName = "Mobile", activityPlatform = "android"),
    )
}

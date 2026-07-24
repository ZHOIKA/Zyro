/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.navigation

class Routes {
    companion object {
        /* Home Screen */
        const val HOME = "home"

        /* Splash Screen */
        const val SPLASH = "splash"

        /* Onboarding/Setup Screen */
        const val SETUP = "setup"

        /* Profile Screen*/
        const val PROFILE = "profile"

        /* Rpc Screens */
        const val APPS_DETECTION = "apps_rpc"
        const val MEDIA_RPC = "media_rpc"
        const val CUSTOM_RPC = "custom_rpc"
        const val CONSOLE_RPC = "console_rpc"
        const val EXPERIMENTAL_RPC = "experimental_rpc"
        const val EXPERIMENTAL_RPC_APPS = "experimental_rpc_apps"

        /* Settings Screens */
        const val LANGUAGES = "languages"
        const val STYLE_AND_APPEARANCE = "style_and_appearance"
        const val RPC_SETTINGS = "rpc_settings"

        /* Display Screen */
        const val DARK_THEME ="dark_theme_screen"

        /* Logs Screen */
        const val LOGS_SCREEN= "logs_screen"

        /* Admin Screen */
        const val ADMIN = "admin"

        /* About Screen */
        const val ABOUT = "about"
        const val CREDITS = "credits"
    }
}

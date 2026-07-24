/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_console_rpc

sealed interface UiEvent {
    object TryAgain: UiEvent
    object CloseSearchBar: UiEvent
    object OpenSearchBar: UiEvent
    class Search(val query: String): UiEvent
}
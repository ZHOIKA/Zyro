/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_logs

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.my.zyro.domain.model.logs.LogEvent
import com.my.zyro.feature_logs.LoggerProvider.logger
import com.my.zyro.preference.Prefs

@Stable
class LogsViewModel: ViewModel() {
    val filterStrings = mutableStateOf("")
    var logs = logger.getLogs()
    var showCompat = mutableStateOf(Prefs[Prefs.SHOW_LOGS_IN_COMPACT_MODE,false])
    var autoScroll = mutableStateOf(Prefs[Prefs.LOGS_AUTO_SCROLL,false])
    val isSearchBarVisible = mutableStateOf(false)


    fun filter(): List<LogEvent> {
        return try {
            val currentLogsSnapshot = synchronized(logs) { logs.toList() }
            currentLogsSnapshot.matches(filterStrings.value)
        } catch (_: ConcurrentModificationException) {
            emptyList()
        }
    }

    private fun List<LogEvent>.matches(filter: String) = filter {
        it.matches(filter)
    }

    private fun LogEvent.matches(filter: String): Boolean {
        return this.text.contains(filter,ignoreCase = true) || this.tag.contains(filter,ignoreCase = true)
    }
    fun clearLogs() = logger.clear()
}
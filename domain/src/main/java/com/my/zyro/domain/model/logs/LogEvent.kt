/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.domain.model.logs

import androidx.compose.runtime.Immutable

@Immutable
data class LogEvent(
    val level: LogLevel,
    val tag: String,
    val text: String,
    val createdAt: Long
)
/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * LogEvent.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
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
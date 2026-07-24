/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.domain.interfaces

interface Logger {
    fun clear()
    fun i(tag: String, event: String)
    fun e(tag: String, event: String)
    fun d(tag: String, event: String)
    fun w(tag: String, event: String)
}

object NoOpLogger: Logger {
    override fun clear() {}
    override fun i(tag: String, event: String) {}
    override fun e(tag: String, event: String) {}
    override fun d(tag: String, event: String) {}
    override fun w(tag: String, event: String) {}
}
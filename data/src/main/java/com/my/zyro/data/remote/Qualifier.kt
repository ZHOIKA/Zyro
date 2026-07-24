/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.data.remote

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Base

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Discord

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Github

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Imgur

/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */
package com.my.zyro.domain.repository

import com.my.zyro.domain.model.Contributor
import com.my.zyro.domain.model.Game
import com.my.zyro.domain.model.release.Release
import com.my.zyro.domain.model.user.User
import java.io.File

interface ZyroRepository {
    suspend fun getImage(url: String): String?
    suspend fun uploadImage(file: File): String?
    suspend fun getGames(): List<Game>
    suspend fun getUser(userid: String): User
    suspend fun getContributors(): List<Contributor>
    suspend fun checkForUpdate(): Release
}

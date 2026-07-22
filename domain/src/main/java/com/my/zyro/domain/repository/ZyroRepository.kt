/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * ZyroRepository.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
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

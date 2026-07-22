/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * ZyroRepositoryImpl.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of yzziK(Vaibhav)
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.data.repository

import com.my.zyro.data.remote.ApiService
import com.my.zyro.data.remote.GamesResponse
import com.my.zyro.data.remote.ImgurApiService
import com.my.zyro.data.remote.toGame
import com.my.zyro.data.rpc.Constants
import com.my.zyro.data.utils.toAttachmentAsset
import com.my.zyro.data.utils.toExternalAsset
import com.my.zyro.data.utils.toImageURL
import com.my.zyro.domain.model.Contributor
import com.my.zyro.domain.model.Game
import com.my.zyro.domain.model.release.Release
import com.my.zyro.domain.model.user.User
import com.my.zyro.domain.repository.ZyroRepository
import com.my.zyro.preference.Prefs
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import java.io.File
import javax.inject.Inject

class ZyroRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val imgurApi: ImgurApiService,
) : ZyroRepository {

    override suspend fun getImage(url: String): String? {
        return if (Prefs[Prefs.USE_IMGUR, false]) {
            imgurApi.getImage(url, Prefs[Prefs.TOKEN]).getOrNull()?.toExternalAsset()
        } else {
            api.getImage(url).getOrNull()?.toAttachmentAsset()
        }
    }

    override suspend fun uploadImage(file: File): String? {
        return if (Prefs[Prefs.USE_IMGUR, false]) {
            imgurApi.uploadImage(file, Prefs[Prefs.IMGUR_CLIENT_ID, Constants.IMGUR_CLIENT_ID])
                .getOrNull()?.toImageURL()?.let { this.getImage(it) }
        } else {
            api.uploadImage(file).getOrNull()?.toAttachmentAsset()
        }
    }

    override suspend fun getGames(): List<Game> {
        return api.getGames().getOrNull()?.body<List<GamesResponse>>()?.map { it.toGame() }
            ?: emptyList()
    }

    override suspend fun getUser(userid: String): User {
        return api.getUser(userid).getOrNull()?.body() ?: User()
    }

    override suspend fun getContributors(): List<Contributor> {
        return api.getContributors().getOrNull()?.body() ?: emptyList()
    }

    override suspend fun checkForUpdate(): Release {
        return api.checkForUpdate().getOrNull()?.releaseBody() ?: Release()
    }
}

suspend fun HttpResponse.releaseBody(): Release {
    return if (this.status.value == 200) {
        Prefs.saveLatestRelease(this.body())
        this.body()
    } else {
        Prefs.getSavedLatestRelease() ?: Release()
    }
}
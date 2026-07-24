/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.domain.use_case.get_games

import com.my.zyro.domain.model.Resource
import com.my.zyro.domain.model.Game
import com.my.zyro.domain.repository.ZyroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(
    private val zyroRepository: ZyroRepository
) {
    operator fun invoke(): Flow<Resource<List<Game>>> = flow {
        try {
            emit(Resource.Loading())
            val games = zyroRepository.getGames()
            emit(Resource.Success(games))
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
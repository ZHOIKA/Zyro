/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.domain.use_case.get_user

import com.my.zyro.domain.model.Resource
import com.my.zyro.domain.model.user.User
import com.my.zyro.domain.repository.ZyroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val zyroRepository: ZyroRepository
) {
    operator fun invoke(userid: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val user = zyroRepository.getUser(userid)
            emit(Resource.Success(user))
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
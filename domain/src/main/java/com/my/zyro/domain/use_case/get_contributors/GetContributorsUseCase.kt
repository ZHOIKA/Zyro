/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * GetContributorsUseCase.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.domain.use_case.get_contributors

import com.my.zyro.domain.model.Contributor
import com.my.zyro.domain.model.Resource
import com.my.zyro.domain.repository.ZyroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetContributorsUseCase @Inject constructor(
    private val zyroRepository: ZyroRepository
) {
    operator fun invoke(): Flow<Resource<List<Contributor>>> = flow {
        try {
            emit(Resource.Loading())
            val contributors = zyroRepository.getContributors()
            emit(Resource.Success(contributors))
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
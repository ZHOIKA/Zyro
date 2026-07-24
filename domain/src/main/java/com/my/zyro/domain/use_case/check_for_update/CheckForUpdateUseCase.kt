/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.domain.use_case.check_for_update

import com.my.zyro.domain.model.Resource
import com.my.zyro.domain.model.release.Release
import com.my.zyro.domain.repository.ZyroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckForUpdateUseCase @Inject constructor(
    private val repository: ZyroRepository
) {
    operator fun invoke(): Flow<Resource<Release>> = flow {
        try {
            emit(Resource.Loading())
            val release = repository.checkForUpdate()
            emit(Resource.Success(release))
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
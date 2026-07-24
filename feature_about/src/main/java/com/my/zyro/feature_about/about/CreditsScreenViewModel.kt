/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_about.about

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.zyro.domain.model.Resource
import com.my.zyro.domain.use_case.get_contributors.GetContributorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreditsScreenViewModel @Inject constructor(
    private val getContributorsUseCase: GetContributorsUseCase
): ViewModel() {
    private val _creditScreenState: MutableStateFlow<CreditScreenState> = MutableStateFlow(CreditScreenState.Loading)
    val creditScreenState: StateFlow<CreditScreenState> = _creditScreenState

    init {
        getContributors()
    }
    
    private fun getContributors() {
        getContributorsUseCase().onEach { result ->
            when(result){
                is Resource.Success -> {
                    Log.d("CreditsScreenViewModel", "Contributors loaded successfully: ${result.data?.size ?: 0} items")
                    _creditScreenState.value = CreditScreenState.LoadingCompleted(result.data?: emptyList())
                }
                is Resource.Error -> {
                    val errorMsg = result.message?: "An unexpected error occurred"
                    Log.e("CreditsScreenViewModel", "Error loading contributors: $errorMsg")
                    _creditScreenState.value = CreditScreenState.Error(errorMsg)
                }
                is Resource.Loading -> {
                    Log.d("CreditsScreenViewModel", "Loading contributors...")
                    _creditScreenState.value = CreditScreenState.Loading
                }
            }
        }.launchIn(viewModelScope)
    }
}

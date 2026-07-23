/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * CreditsScreenViewModel.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
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

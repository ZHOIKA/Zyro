package com.my.zyro.feature_home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.zyro.domain.model.Resource
import com.my.zyro.domain.model.release.Release
import com.my.zyro.domain.use_case.check_for_update.CheckForUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val checkForUpdateUseCase: CheckForUpdateUseCase
) : ViewModel() {


    private val _homeState =
        MutableStateFlow<HomeScreenState>(
            HomeScreenState.Loading
        )


    val homeState: StateFlow<HomeScreenState> =
        _homeState



    fun getLatestUpdate() {


        checkForUpdateUseCase()
            .onEach { result ->


                when(result) {


                    is Resource.Loading -> {

                        _homeState.value =
                            HomeScreenState.Loading

                    }



                    is Resource.Success -> {


                        _homeState.value =
                            HomeScreenState.LoadingCompleted(
                                result.data ?: Release()
                            )

                    }



                    is Resource.Error -> {


                        _homeState.value =
                            HomeScreenState.Error(
                                result.message
                                    ?: "Erro ao verificar atualização"
                            )

                    }

                }


            }
            .launchIn(viewModelScope)

    }



    fun setReleaseFromPrefs(
        release: Release
    ) {

        _homeState.value =
            HomeScreenState.LoadingCompleted(
                release
            )

    }

}



@Stable
sealed interface HomeScreenState {


    @Stable
    data object Loading : HomeScreenState



    @Stable
    data class Error(
        val error: String?
    ) : HomeScreenState



    @Stable
    data class LoadingCompleted(
        val release: Release
    ) : HomeScreenState

}
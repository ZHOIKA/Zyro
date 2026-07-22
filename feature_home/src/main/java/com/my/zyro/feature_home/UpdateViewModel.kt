package com.my.zyro.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.zyro.data.update.UpdateManager
import com.my.zyro.domain.model.release.Release
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class UpdateState {

    object Idle : UpdateState()

    data class Downloading(
        val progress: Int
    ) : UpdateState()

    data class Ready(
        val file: File
    ) : UpdateState()

    data class Error(
        val message: String
    ) : UpdateState()
}


@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val updateManager: UpdateManager
) : ViewModel() {


    private val _state =
        MutableStateFlow<UpdateState>(
            UpdateState.Idle
        )


    val state =
        _state.asStateFlow()



    fun downloadAndInstall(
        release: Release
    ) {

        if (_state.value is UpdateState.Downloading) {
            return
        }


        val apkUrl =
            release.assets
                ?.firstOrNull { asset ->

                    asset?.name
                        ?.lowercase()
                        ?.endsWith(".apk") == true

                }
                ?.browserDownloadUrl



        if (apkUrl.isNullOrBlank()) {

            _state.value =
                UpdateState.Error(
                    "Nenhum APK encontrado"
                )

            return

        }



        viewModelScope.launch {

            try {

                _state.value =
                    UpdateState.Downloading(
                        0
                    )



                val apkFile =
                    updateManager.downloadApk(
                        apkUrl
                    ) { progress ->

                        _state.value =
                            UpdateState.Downloading(
                                progress
                            )

                    }



                _state.value =
                    UpdateState.Ready(
                        apkFile
                    )



                updateManager.installApk(
                    apkFile
                )



            } catch (e: Exception) {


                _state.value =
                    UpdateState.Error(
                        e.localizedMessage
                            ?: "Falha ao atualizar"
                    )

            }

        }

    }

}
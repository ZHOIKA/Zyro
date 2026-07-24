/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_apps_rpc

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.zyro.data.utils.getInstalledApps
import com.my.zyro.preference.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val _state: MutableStateFlow<AppsState> = MutableStateFlow(AppsState())
    val state = _state.asStateFlow()

    init {
        getInstalledApps()
    }

    fun getInstalledApps() {
        viewModelScope.launch(context = Dispatchers.Default) {
            val appList = getInstalledApps(
                context = context,
                isEnabled = Prefs::isAppEnabled
            ).sortedBy { !it.isChecked }
            val enabledApps = appList.associate { it.pkg to it.isChecked }
            _state.update {
                AppsState(
                    apps = appList,
                    isLoading = false,
                    enabledApps = enabledApps
                )
            }
        }
    }

    fun updateAppEnabled(pkg: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Prefs.toggleAppEnabled(pkg)
            _state.update { currentState ->
                currentState.copy(
                    enabledApps = currentState.enabledApps.toMutableMap().apply {
                        this[pkg] = !this[pkg]!!
                    },
                )
            }
        }
    }
}
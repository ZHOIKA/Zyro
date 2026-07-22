/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * LoginUiState.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.feature_profile.ui.login

sealed interface LoginUiState {
    object InitialState: LoginUiState
    object OnLoginClicked: LoginUiState
    object OnLoginCompleted: LoginUiState
}
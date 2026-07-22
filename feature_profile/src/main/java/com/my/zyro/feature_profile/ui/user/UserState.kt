/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * UserState.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of yzziK(Vaibhav)
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.feature_profile.ui.user

import androidx.compose.runtime.Stable
import com.my.zyro.domain.model.user.User

@Stable
sealed interface UserState {
    object Loading: UserState
    class Error(val error: String,val user: User?): UserState
    class LoadingCompleted(val user: User?): UserState
}
/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * GamesState.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.feature_console_rpc

import androidx.compose.runtime.Stable
import com.my.zyro.domain.model.Game

@Stable
sealed interface GamesState {
    object Loading: GamesState
    class Success(val games: List<Game>): GamesState
    class Error(val error: String): GamesState
}
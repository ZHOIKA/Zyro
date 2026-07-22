/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * Game.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * Game.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Game(
    val platform: String,
    val small_image: String,
    val large_image: String?,
    val game_title: String
)

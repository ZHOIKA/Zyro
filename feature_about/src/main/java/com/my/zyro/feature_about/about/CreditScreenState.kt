/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * CreditScreenState.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of yzziK(Vaibhav)
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.feature_about.about

import androidx.compose.runtime.Stable
import com.my.zyro.domain.model.Contributor

@Stable
sealed interface CreditScreenState {
    object Loading: CreditScreenState
    class Error(val error: String?): CreditScreenState
    class LoadingCompleted(val contributors: List<Contributor>): CreditScreenState
}
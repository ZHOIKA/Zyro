/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * LogProvider.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of yzziK(Vaibhav)
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.feature_logs

object LoggerProvider {
    var logger = KLogger.getInstance()!!
    fun init() {
        KLogger.init()
    }
}
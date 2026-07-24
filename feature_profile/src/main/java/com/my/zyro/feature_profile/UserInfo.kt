/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * UserInfo.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.feature_profile

import com.my.zyro.preference.Prefs
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import zyro.gateway.DiscordWebSocket
import zyro.gateway.DiscordWebSocketImpl
import zyro.gateway.entities.Payload
import zyro.gateway.entities.PayloadData
import zyro.gateway.entities.Ready

suspend fun getUserInfo(token: String, onInfoSaved: () -> Unit) {
    val discordWebSocket: DiscordWebSocket = object: DiscordWebSocketImpl(token){
        override fun onDispatchEvent(payloadJson: String, payload: Payload) {
            if (payload.t.toString() == "READY"){
                val readyData = Json.decodeFromString<PayloadData<Ready>>(payloadJson).d ?: return
                val user = readyData.user ?: return
                Prefs[Prefs.USER_ID] = user.id ?: ""
                Prefs[Prefs.USER_BIO] = user.bio ?: ""
                Prefs[Prefs.USER_NITRO] = user.premiumType in 1..3
                close()
                onInfoSaved()
            }
        }
    }
    discordWebSocket.connect()
}

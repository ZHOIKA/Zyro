/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * GetAppsUseCase.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.data.get_current_data.app

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import com.blankj.utilcode.util.AppUtils
import com.my.zyro.data.rpc.CommonRpc
import com.my.zyro.data.rpc.RpcImage
import com.my.zyro.preference.Prefs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.Objects
import java.util.SortedMap
import java.util.TreeMap
import javax.inject.Inject

class GetCurrentlyRunningApp @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    operator fun invoke(beginTime: Long = System.currentTimeMillis() - 10000): CommonRpc {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTimeMillis = System.currentTimeMillis()
        val queryUsageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, beginTime, currentTimeMillis
        )
        if (queryUsageStats != null && queryUsageStats.size > 1) {
            val treeMap: SortedMap<Long, UsageStats> = TreeMap()
            for (usageStats in queryUsageStats) {
                treeMap[usageStats.lastTimeUsed] = usageStats
            }
            if (!(treeMap.isEmpty() || treeMap[treeMap.lastKey()]?.packageName == "com.my.zyro" || treeMap[treeMap.lastKey()]?.packageName == "com.discord")) {
                val packageName = treeMap[treeMap.lastKey()]!!.packageName
                Objects.requireNonNull(packageName)
                return CommonRpc(
                    name = AppUtils.getAppName(packageName),
                    details = null,
                    state = null,
                    largeImage = RpcImage.ApplicationIcon(packageName, context),
                    packageName = packageName
                )

            }
        }
        return CommonRpc()
    }
}
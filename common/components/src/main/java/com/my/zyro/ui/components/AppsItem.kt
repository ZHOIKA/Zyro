/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.getValue
import android.graphics.drawable.Drawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.blankj.utilcode.util.AppUtils

@Composable
fun AppsItem(
    name: String,
    pkg: String,
    isChecked: Boolean,
    onClick: () -> Unit = {},
) {
    val appIcon by produceState<Drawable?>(initialValue = null, key1 = pkg) {
        value = withContext(Dispatchers.IO) { AppUtils.getAppIcon(pkg) }
    }
    Surface(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isChecked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = appIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .padding(10.dp),
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    maxLines = 3,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 17.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis
                )
                if (pkg.isNotEmpty())
                    Text(
                        text = pkg,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        style = MaterialTheme.typography.bodyMedium,
                    )
            }
            KSwitch(
                checked = isChecked,
                modifier = Modifier.padding(start = 20.dp, end = 6.dp),
            )
        }
    }
}
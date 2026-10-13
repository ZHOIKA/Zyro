/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_home.feature

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.my.zyro.resources.R
import com.my.zyro.ui.components.KSwitch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Features(homeItems: List<HomeFeature> = emptyList(), onValueUpdate: (Int) -> Unit) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        homeItems.forEachIndexed { index, feature ->
            val card: @Composable () -> Unit = {
                Surface(
                    onClick = { feature.route?.let(feature.onClick) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = if (feature.isChecked) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, if (feature.isChecked) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh) {
                                Icon(painterResource(feature.icon), contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(14.dp).size(26.dp))
                            }
                            Spacer(Modifier.width(16.dp))
                            Text(feature.title, style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f))
                            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                        }
                        if (feature.tooltipText.isNotBlank()) {
                            Text(feature.tooltipText, style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        if (feature.showSwitch) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(stringResource(if (feature.isChecked) R.string.android_on else R.string.android_off),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (feature.isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                                KSwitch(checked = feature.isChecked, onClick = {
                                    feature.onCheckedChange(!feature.isChecked)
                                    onValueUpdate(index)
                                })
                            }
                        }
                    }
                }
            }
            if (feature.tooltipText.isNotBlank() && feature.featureDocsLink.isNotBlank()) {
                TooltipBox(positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    state = rememberTooltipState(), tooltip = {
                        RichTooltip(title = { Text(feature.title) }, text = { Text(feature.tooltipText) },
                            action = { TextButton(onClick = { uriHandler.openUri(feature.featureDocsLink) }) {
                                Text(stringResource(R.string.learn_more))
                            } })
                    }) { card() }
            } else card()
        }
    }
}

/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun KSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    enable: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    androidx.compose.material3.Switch(
        modifier = modifier,
        checked = checked,
        enabled = enable,
        onCheckedChange = onClick?.let { callback -> { _: Boolean -> callback() } }
    )
}

@Preview
@Composable
fun KSwitchPreview() {
    var switchState by remember { mutableStateOf(true) }
    KSwitch(checked = switchState, enable = switchState, onClick = {
        switchState =!switchState
    })
}
/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * LoginScreen.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.feature_profile.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.my.zyro.resources.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.my.zyro.feature_profile.getUserInfo
import com.my.zyro.feature_profile.ui.component.DiscordLoginButton
import com.my.zyro.feature_profile.ui.component.DiscordLoginWebView
import com.my.zyro.preference.Prefs
import com.my.zyro.preference.Prefs.TOKEN
import com.my.zyro.ui.components.BackButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(
    onCompleted: () -> Unit,
    onBackPressed: () -> Unit
) {
    var buttonEnabledState by remember { mutableStateOf(true) }
    var uiState: LoginUiState by remember { mutableStateOf(LoginUiState.InitialState) }
    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = { },
                navigationIcon = { BackButton { onBackPressed() } })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                LoginUiState.InitialState -> {}
                LoginUiState.OnLoginClicked -> {
                    ModalBottomSheet(
                        onDismissRequest = {
                            uiState = LoginUiState.InitialState
                        },
                        sheetState = modalBottomSheetState,
                        dragHandle = {
                            BottomSheetDefaults.DragHandle()
                        },
                    ) {
                        DiscordLoginWebView {
                            Prefs[TOKEN] = it
                            scope.launch {
                                modalBottomSheetState.hide()
                                uiState = LoginUiState.OnLoginCompleted
                                getUserInfo(it, onInfoSaved = {
                                    onCompleted()
                                })
                            }
                        }
                    }
                }
                LoginUiState.OnLoginCompleted -> {
                    buttonEnabledState = false
                    CircularProgressIndicator()
                }
            }
            if (uiState == LoginUiState.InitialState) {
                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                        Text("Z", style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(28.dp))
                    }
                    Text("Zyro", style = MaterialTheme.typography.displaySmall)
                    Text(stringResource(R.string.zyro_dashboard_description),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    DiscordLoginButton(onClick = { uiState = LoginUiState.OnLoginClicked }, enabled = buttonEnabledState)
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onCompleted = {},
        onBackPressed = {}
    )
}
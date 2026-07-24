/*
 *  ******************************************************************
 *  * Copyright (C) 2024 — Zyro Contributors
 *  * Based on code from Kizzy by dead8309 (Vaibhav)
 *  * https://github.com/dead8309/Kizzy
 *  * SPDX-License-Identifier: GPL-3.0-only
 *  ******************************************************************
 */

package com.my.zyro.feature_crash_handler

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.zyro.data.utils.shareAsFile
import com.my.zyro.resources.R
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrashScreen(trace: String?) {
    val ctx = LocalContext.current
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(id = R.string.app_crashed),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold)
            )
        }, actions = {
            IconButton(onClick = {
                exitProcess(0)
            }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = Icons.Default.Close.name
                )
            }
        })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(onClick = {
            ctx.shareAsFile(trace, "Zyro_Log.txt")
        }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share Logs",
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(text = stringResource(id = R.string.share_crash_logs))
        }
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.share_crash_logs_desc),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                ElevatedCard(
                    modifier = Modifier.fillMaxSize(), colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    )
                ) {
                    LazyColumn {
                        item {
                            if (trace != null) {
                                Text(
                                    modifier = Modifier.padding(10.dp),
                                    text = trace,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// This error is thrown when notification listener permission is not granted
@Preview
@Composable
fun PreviewCrashScreen() = CrashScreen(trace = """
    Zyro crash report
    Manufacturer: ******
    Device: *****
    Android version: **
    App version: *** (**)
    Stacktrace: 
    java.lang.SecurityException: Missing permission to control media.
    	at android.os.Parcel.createExceptionOrNull(Parcel.java:3011)
    	at android.os.Parcel.createException(Parcel.java:2995)
    	at android.os.Parcel.readException(Parcel.java:2978)
    	at android.os.Parcel.readException(Parcel.java:2920)
    	at android.media.session.ISessionManager$\Stub$\Proxy.getSessions(ISessionManager.java:672)
    	at android.media.session.MediaSessionManager.getActiveSessionsForUser(MediaSessionManager.java:272)
    	at android.media.session.MediaSessionManager.getActiveSessions(MediaSessionManager.java:194)
    	at com.my.zyro.data.get_current_data.media.GetCurrentPlayingMedia.invoke(GetCurrentlyPlayingMedia.kt:38)
    	at com.my.zyro.services.MediaRpcService$\onCreate${'$'}1.invokeSuspend(MediaRpcService.kt:61)
    	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
    	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
    	at kotlinx.coroutines.internal.LimitedDispatcher.run(LimitedDispatcher.kt:42)
    	at kotlinx.coroutines.scheduling.TaskImpl.run(Tasks.kt:95)
    	at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:570)
    	at kotlinx.coroutines.scheduling.CoroutineScheduler$\Worker.executeTask(CoroutineScheduler.kt:750)
    	at kotlinx.coroutines.scheduling.CoroutineScheduler$\Worker.runWorker(CoroutineScheduler.kt:677)
    	at kotlinx.coroutines.scheduling.CoroutineScheduler$\Worker.run(CoroutineScheduler.kt:664)
    	Suppressed: kotlinx.coroutines.DiagnosticCoroutineContextException: [StandaloneCoroutine{Cancelling}@2fd5814, Dispatchers.IO]
    Caused by: android.os.RemoteException: Remote stack trace:
    	at com.android.server.media.MediaSessionService.enforceMediaPermissions(MediaSessionService.java:616)
    	at com.android.server.media.MediaSessionService.-${'$'}$\Nest$\menforceMediaPermissions(Unknown Source:0)
    	at com.android.server.media.MediaSessionService$\SessionManagerImpl.verifySessionsRequest(MediaSessionService.java:2163)
    	at com.android.server.media.MediaSessionService$\SessionManagerImpl.getSessions(MediaSessionService.java:1269)
    	at android.media.session.ISessionManager$\Stub.onTransact(ISessionManager.java:317)


""".trimIndent())
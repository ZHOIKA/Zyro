package com.my.zyro.feature_home

import android.content.ComponentName
import android.os.Build
import android.widget.Toast

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Update

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


import androidx.hilt.navigation.compose.hiltViewModel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

import coil.compose.AsyncImage

import com.my.zyro.feature_home.BuildConfig
import com.my.zyro.domain.model.toVersion
import com.my.zyro.domain.model.user.User

import com.my.zyro.feature_home.feature.Features
import com.my.zyro.feature_home.feature.HomeFeature

import com.my.zyro.feature_rpc_base.services.ZyroTileService
import com.my.zyro.feature_settings.SettingsDrawer

import com.my.zyro.resources.R

import com.my.zyro.ui.components.ChipSection
import com.my.zyro.ui.components.ParticleEffect
import com.my.zyro.ui.components.UpdateDialog

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    state: HomeScreenState,
    checkForUpdates: () -> Unit,
    showBadge: Boolean,
    features: List<HomeFeature>,
    user: User?,
    navigateToProfile: () -> Unit,
    navigateToStyleAndAppearance: () -> Unit,
    navigateToLanguages: () -> Unit,
    navigateToAbout: () -> Unit,
    navigateToRpcSettings: () -> Unit,
    navigateToLogsScreen: () -> Unit,
    navigateToAdmin: () -> Unit = {},
    updateViewModel: UpdateViewModel = hiltViewModel()
) {

    val ctx = LocalContext.current

    var homeItems by remember {
        mutableStateOf(features)
    }

    var showUpdateDialog by remember {
        mutableStateOf(false)
    }

    val drawerState = rememberDrawerState(
        DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState()
        )

    val isCollapsed =
        scrollBehavior.state.collapsedFraction > 0.55f


    OnLifecycleEvent { _, event ->

        if (event == Lifecycle.Event.ON_RESUME) {
            homeItems = features
        }

    }


    ModalNavigationDrawer(
        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier.width(300.dp)
            ) {

                SettingsDrawer(

                    user = user,

                    showZyroQuickieRequestItem =
                        !ZyroTileService.tileAdded.value,


                    onRequestAddTile = {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                            val component =
                                ComponentName(
                                    ctx,
                                    ZyroTileService::class.java
                                )


                            val manager =
                                ctx.getSystemService(
                                    android.app.StatusBarManager::class.java
                                )


                            manager.requestAddTileService(
                                component,
                                ctx.getString(
                                    R.string.qs_tile_label
                                ),

                                android.graphics.drawable.Icon
                                    .createWithResource(
                                        ctx,
                                        R.drawable.ic_tile_play
                                    ),

                                {}
                            ) {}

                        }

                    },


                    navigateToProfile = navigateToProfile,

                    navigateToStyleAndAppearance =
                        navigateToStyleAndAppearance,

                    navigateToLanguages =
                        navigateToLanguages,

                    navigateToAbout =
                        navigateToAbout,

                    navigateToRpcSettings =
                        navigateToRpcSettings,

                    navigateToLogsScreen =
                        navigateToLogsScreen,
                    navigateToAdmin =
                        navigateToAdmin

                )

            }

        }

    ) {

        ParticleEffect(
            modifier = Modifier
                .fillMaxSize()
        )

        Scaffold(

            modifier =
                Modifier
                    .fillMaxSize()
                    .nestedScroll(
                        scrollBehavior.nestedScrollConnection
                    ),

            topBar = {

                LargeTopAppBar(

                    title = {

                        Text(
                            text =
                                "${stringResource(R.string.welcome)}, ${
                                    user?.globalName
                                        ?: user?.username
                                        ?: ""
                                }",

                            style =
                                if (isCollapsed)
                                    MaterialTheme.typography.headlineSmall
                                else
                                    MaterialTheme.typography.headlineLarge
                        )

                    },

                    navigationIcon = {

                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {

                            Icon(
                                Icons.Outlined.Menu,
                                contentDescription = "Menu"
                            )

                        }

                    },

                    actions = {

                        BadgedBox(

                            badge = {

                                if (showBadge) {

                                    Badge(
                                        modifier =
                                            Modifier
                                                .size(8.dp)
                                                .offset(
                                                    8.dp,
                                                    (-14).dp
                                                )
                                    )

                                }

                            }

                        ) {

                            Icon(
                                imageVector =
                                    Icons.Outlined.Update,

                                contentDescription =
                                    "Update",

                                modifier =
                                    Modifier.clickable {

                                        Toast.makeText(
                                            ctx,
                                            ctx.getString(
                                                R.string.update_check_for_update
                                            ),
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        checkForUpdates()

                                        showUpdateDialog = true

                                    }
                            )

                        }


                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )


                        IconButton(
                            onClick = navigateToProfile
                        ) {

                            if (user != null) {

                                AsyncImage(

                                    model =
                                        user.getAvatarImage(),

                                    modifier =
                                        Modifier
                                            .size(52.dp)
                                            .border(
                                                2.dp,
                                                MaterialTheme.colorScheme.secondaryContainer,
                                                CircleShape
                                            )
                                            .clip(
                                                CircleShape
                                            ),

                                    placeholder =
                                        painterResource(
                                            R.drawable.error_avatar
                                        ),

                                    contentDescription =
                                        user.username

                                )

                            } else {

                                Icon(
                                    Icons.Outlined.Person,
                                    contentDescription = "Profile"
                                )

                            }

                        }

                    },

                    scrollBehavior = scrollBehavior

                )

            }

        ) { padding ->


            LazyColumn(

                modifier =
                    Modifier.padding(padding),

                verticalArrangement =
                    Arrangement.spacedBy(16.dp)

            ) {

                item {

                    ChipSection()

                    Text(
                        text =
                            stringResource(
                                R.string.features
                            ),

                        style =
                            MaterialTheme.typography.headlineMedium,

                        modifier =
                            Modifier.padding(
                                start = 15.dp
                            )
                    )

                }


                item {

                    Features(homeItems) { index ->

                        homeItems =
                            homeItems.mapIndexed { i, item ->

                                if (i == index) {

                                    item.copy(
                                        isChecked =
                                            !item.isChecked
                                    )

                                } else {

                                    if (item.isChecked)

                                        item.copy(
                                            isChecked = false
                                        )

                                    else
                                        item
                                }

                            }

                    }

                }

            }


            if (
                state is HomeScreenState.LoadingCompleted &&
                showUpdateDialog
            ) {

                UpdateDialog(

                    newVersionPublishDate =
                        state.release.publishedAt ?: "",

                    newVersionSize =
                        state.release.assets
                            ?.firstOrNull()
                            ?.size ?: 0,

                    newVersionLog =
                        state.release.body ?: "",

                    isDownloading =
                        updateViewModel.state.value
                            is UpdateState.Downloading,

                    downloadProgress =
                        (
                            updateViewModel.state.value
                                as? UpdateState.Downloading
                        )?.progress,

                    onUpdateNow = {

                        updateViewModel.downloadAndInstall(
                            state.release
                        )

                    },

                    onDismissRequest = {

                        showUpdateDialog = false

                    }

                )

            } else if (
                state is HomeScreenState.AlreadyUpdated &&
                showUpdateDialog
            ) {
                Toast.makeText(ctx, "Você já está usando a versão mais recente", Toast.LENGTH_SHORT).show()
                showUpdateDialog = false
            }

        }

    }

}


@Composable
fun OnLifecycleEvent(
    onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit
) {

    val eventHandler =
        rememberUpdatedState(onEvent)

    val lifecycleOwner =
        rememberUpdatedState(
            LocalLifecycleOwner.current
        )

    DisposableEffect(
        lifecycleOwner.value
    ) {

        val observer =
            LifecycleEventObserver { owner, event ->

                eventHandler.value(
                    owner,
                    event
                )

            }

        lifecycleOwner.value.lifecycle
            .addObserver(observer)

        onDispose {

            lifecycleOwner.value.lifecycle
                .removeObserver(observer)

        }

    }

}


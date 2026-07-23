package com.my.zyro.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Update

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.my.zyro.resources.R


fun Int.formatSize(): String =
    (this / 1024f / 1024f)
        .takeIf { it > 0f }
        ?.run {
            " ${String.format("%.2f", this)} MB"
        }
        ?: ""


@Composable
fun UpdateDialog(

    modifier: Modifier = Modifier,

    tagName: String? = null,

    newVersionPublishDate: String,

    newVersionSize: Int,

    newVersionLog: String,

    downloadProgress: Int? = null,

    isDownloading: Boolean = false,

    isReady: Boolean = false,

    isInstalling: Boolean = false,

    errorMessage: String? = null,

    onUpdateNow: () -> Unit,

    onInstallNow: () -> Unit = {},

    onCancelDownload: () -> Unit = {},

    onDismissRequest: () -> Unit = {}

) {

    val title = when {
        errorMessage != null -> "Erro na atualização"
        isInstalling -> "Instalando..."
        isReady -> "Download concluído"
        isDownloading -> "Baixando atualização..."
        else -> stringResource(R.string.change_log)
    }

    AlertDialog(

        modifier = modifier,

        onDismissRequest = {
            if (!isDownloading && !isInstalling) {
                onDismissRequest()
            }
        },

        icon = {
            if (errorMessage != null) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = "Erro",
                    tint = MaterialTheme.colorScheme.error
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Update,
                    contentDescription = "Update"
                )
            }
        },

        title = {

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(text = title)

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(
                    text =
                        "$newVersionPublishDate ${newVersionSize.formatSize()}",
                    color =
                        MaterialTheme.colorScheme.outline
                            .copy(
                                alpha = 0.7f
                            ),
                    style =
                        MaterialTheme.typography.bodyMedium
                )

                if (tagName != null) {
                    Text(
                        text = "Versão: $tagName",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )
            }

        },

        text = {

            Column {

                if (errorMessage != null) {

                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )

                } else if (isReady) {

                    Text(
                        text = "O APK foi baixado com sucesso. Clique em Instalar para prosseguir.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )

                } else if (!isDownloading && !isInstalling) {

                    SelectionContainer {

                        Text(
                            modifier =
                                Modifier.verticalScroll(
                                    rememberScrollState()
                                ),
                            text =
                                newVersionLog
                        )

                    }

                }

                if (isDownloading && downloadProgress != null) {

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Text(
                        text = "Baixando... $downloadProgress%",
                        style =
                            MaterialTheme.typography.bodyMedium,
                        color =
                            MaterialTheme.colorScheme.outline
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    LinearProgressIndicator(
                        progress = { downloadProgress / 100f },
                        modifier = Modifier.fillMaxWidth()
                    )

                }

                if (isInstalling) {

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )

                }

            }

        },

        confirmButton = {

            when {
                errorMessage != null -> {
                    TextButton(onClick = onUpdateNow) {
                        Text("Tentar novamente")
                    }
                }
                isReady -> {
                    TextButton(onClick = onInstallNow) {
                        Text("Instalar")
                    }
                }
                isDownloading -> {
                    TextButton(enabled = false, onClick = {}) {
                        Text("Baixando...")
                    }
                }
                else -> {
                    TextButton(
                        onClick = onUpdateNow,
                        enabled = !isDownloading && !isInstalling
                    ) {
                        Text(
                            stringResource(R.string.update)
                        )
                    }
                }
            }

        },

        dismissButton = {

            when {
                errorMessage != null -> {
                    TextButton(onClick = onDismissRequest) {
                        Text("Fechar")
                    }
                }
                isDownloading -> {
                    TextButton(onClick = onCancelDownload) {
                        Text("Cancelar")
                    }
                }
                else -> {
                    TextButton(
                        onClick = onDismissRequest,
                        enabled = !isInstalling
                    ) {
                        Text(
                            stringResource(R.string.cancel)
                        )
                    }
                }
            }

        }

    )

}



@Preview
@Composable
fun UpdateDialogPreview() {

    UpdateDialog(
        tagName = "beta-42",
        newVersionLog =
            "1. Fix bugs\n2. Fix bugs\n3. Fix bugs",
        newVersionPublishDate =
            "2021-10-10",
        newVersionSize =
            10000000,
        onUpdateNow = {},
        onDismissRequest = {},
        modifier =
            Modifier
                .height(500.dp)
                .width(300.dp)
    )

}


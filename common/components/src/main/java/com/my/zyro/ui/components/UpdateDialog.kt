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

    newVersionPublishDate: String,

    newVersionSize: Int,

    newVersionLog: String,

    downloadProgress: Int? = null,

    isDownloading: Boolean = false,

    onUpdateNow: () -> Unit,

    onDismissRequest: () -> Unit = {}

) {



    AlertDialog(

        modifier = modifier,


        onDismissRequest = onDismissRequest,


        icon = {

            Icon(

                imageVector = Icons.Outlined.Update,

                contentDescription = "Update"

            )

        },


        title = {

            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally

            ) {


                Text(

                    text =
                        stringResource(
                            R.string.change_log
                        )

                )


                Spacer(
                    modifier =
                        Modifier.height(16.dp)
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


                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

            }

        },


        text = {

            Column {


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



                if (
                    isDownloading &&
                    downloadProgress != null
                ) {


                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )


                    Text(

                        text =
                            "Baixando atualização... $downloadProgress%",


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

                        progress =
                            {
                                downloadProgress / 100f
                            },


                        modifier =
                            Modifier.fillMaxWidth()

                    )

                }

            }

        },


        confirmButton = {


            TextButton(

                onClick = onUpdateNow,


                enabled =
                    !isDownloading

            ) {


                Text(

                    text =
                        if (isDownloading)

                            "Baixando..."

                        else

                            stringResource(
                                R.string.update
                            )

                )

            }

        },


        dismissButton = {


            TextButton(

                onClick =
                    onDismissRequest,


                enabled =
                    !isDownloading

            ) {


                Text(

                    text =
                        stringResource(
                            R.string.cancel
                        )

                )

            }

        }

    )

}



@Preview
@Composable
fun UpdateDialogPreview() {


    UpdateDialog(

        newVersionLog =
            "1. Fix bugs\n2. Fix bugs\n3. Fix bugs",


        newVersionPublishDate =
            "2021-10-10",


        newVersionSize =
            1000000,


        onUpdateNow = {},


        onDismissRequest = {},


        modifier =
            Modifier
                .height(500.dp)
                .width(300.dp)

    )

}
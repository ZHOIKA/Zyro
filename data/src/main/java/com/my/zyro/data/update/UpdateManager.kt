package com.my.zyro.data.update

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun downloadApk(
        apkUrl: String,
        fileName: String = "Zyro.apk",
        onProgress: (Int) -> Unit = {}
    ): File = withContext(Dispatchers.IO) {
        val updatesDir = File(context.cacheDir, "updates").apply {
            if (!exists()) mkdirs()
        }

        val apkFile = File(updatesDir, fileName)
        if (apkFile.exists()) apkFile.delete()

        val connection = (URL(apkUrl).openConnection() as HttpURLConnection).apply {
            instanceFollowRedirects = true
            connectTimeout = 15_000
            readTimeout = 15_000
            requestMethod = "GET"
        }

        try {
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                throw IllegalStateException("Falha ao baixar APK. HTTP $responseCode")
            }

            val totalBytes = connection.contentLengthLong
            var downloadedBytes = 0L

            connection.inputStream.use { input ->
                FileOutputStream(apkFile).use { output ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    while (true) {
                        val read = input.read(buffer)
                        if (read == -1) break

                        output.write(buffer, 0, read)
                        downloadedBytes += read

                        if (totalBytes > 0) {
                            val progress = ((downloadedBytes * 100) / totalBytes)
                                .toInt()
                                .coerceIn(0, 100)
                            onProgress(progress)
                        }
                    }
                    output.flush()
                }
            }

            onProgress(100)
            apkFile
        } catch (e: CancellationException) {
            apkFile.delete()
            throw e
        } catch (e: Exception) {
            apkFile.delete()
            throw e
        } finally {
            connection.disconnect()
        }
    }

    fun installApk(apkFile: File) {
        if (!apkFile.exists()) {
            throw IllegalArgumentException("Arquivo APK não encontrado: ${apkFile.absolutePath}")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            !context.packageManager.canRequestPackageInstalls()
        ) {
            openUnknownSourcesSettings()
            return
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            apkFile
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            throw IllegalStateException("Nenhum instalador de APK encontrado.", e)
        }
    }

    fun openUnknownSourcesSettings() {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
            data = Uri.parse("package:${context.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}

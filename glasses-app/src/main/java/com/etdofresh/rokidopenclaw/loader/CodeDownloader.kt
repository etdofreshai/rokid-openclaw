package com.etdofresh.rokidopenclaw.loader

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

/**
 * Downloads a DEX/JAR code bundle from a GitHub release asset URL
 * and stores it in the app's internal storage.
 */
object CodeDownloader {

    private const val CODE_FILENAME = "payload.dex"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    /**
     * Downloads the code bundle to internal storage.
     * @param onProgress callback with progress 0.0..1.0 (-1 if unknown)
     * @return the downloaded File, or null on failure
     */
    suspend fun download(
        context: Context,
        url: String,
        onProgress: (Float) -> Unit = {}
    ): File? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val body = response.body ?: return@withContext null
            val contentLength = body.contentLength()
            val outputFile = File(context.filesDir, CODE_FILENAME)
            val tempFile = File(context.filesDir, "$CODE_FILENAME.tmp")

            FileOutputStream(tempFile).use { fos ->
                body.byteStream().use { input ->
                    val buffer = ByteArray(8192)
                    var totalRead = 0L
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        fos.write(buffer, 0, bytesRead)
                        totalRead += bytesRead
                        val progress = if (contentLength > 0) {
                            totalRead.toFloat() / contentLength
                        } else -1f
                        onProgress(progress)
                    }
                }
            }

            // Atomic rename
            tempFile.renameTo(outputFile)
            outputFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Returns the locally stored code bundle, or null if none exists.
     */
    fun getLocalCodeFile(context: Context): File? {
        val file = File(context.filesDir, CODE_FILENAME)
        return if (file.exists()) file else null
    }
}

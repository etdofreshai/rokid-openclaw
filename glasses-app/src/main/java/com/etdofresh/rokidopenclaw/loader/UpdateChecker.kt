package com.etdofresh.rokidopenclaw.loader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

/**
 * Checks GitHub releases API for the latest version and returns release info.
 */
data class ReleaseInfo(
    val tagName: String,
    val version: String,
    val downloadUrl: String?,
    val releaseName: String
)

object UpdateChecker {

    private const val RELEASES_URL =
        "https://api.github.com/repos/etdofreshai/rokid-openclaw/releases/latest"

    private val client = OkHttpClient()

    /**
     * Fetches the latest release info from GitHub.
     * Returns null if the request fails or no release exists.
     */
    suspend fun checkLatestRelease(): ReleaseInfo? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(RELEASES_URL)
                .header("Accept", "application/vnd.github.v3+json")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val body = response.body?.string() ?: return@withContext null
            val json = JSONObject(body)

            val tagName = json.getString("tag_name")
            val version = tagName.removePrefix("v")
            val releaseName = json.optString("name", tagName)

            // Find the DEX/JAR asset in the release
            val assets = json.getJSONArray("assets")
            var downloadUrl: String? = null
            for (i in 0 until assets.length()) {
                val asset = assets.getJSONObject(i)
                val name = asset.getString("name")
                if (name.endsWith(".dex") || name.endsWith(".jar")) {
                    downloadUrl = asset.getString("browser_download_url")
                    break
                }
            }

            ReleaseInfo(
                tagName = tagName,
                version = version,
                downloadUrl = downloadUrl,
                releaseName = releaseName
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Compares two semver strings. Returns true if remote is newer than local.
     */
    fun isNewer(localVersion: String, remoteVersion: String): Boolean {
        val local = localVersion.removePrefix("v").split(".").map { it.toIntOrNull() ?: 0 }
        val remote = remoteVersion.removePrefix("v").split(".").map { it.toIntOrNull() ?: 0 }
        val maxLen = maxOf(local.size, remote.size)
        for (i in 0 until maxLen) {
            val l = local.getOrElse(i) { 0 }
            val r = remote.getOrElse(i) { 0 }
            if (r > l) return true
            if (r < l) return false
        }
        return false
    }
}

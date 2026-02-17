package com.etdofresh.rokidopenclaw.loader

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

/**
 * Entry point for the app. Checks for updates from GitHub releases,
 * optionally downloads a new code bundle, and then launches the main
 * app — either from a dynamically loaded DEX or the built-in code.
 */
class LoaderActivity : ComponentActivity() {

    private lateinit var prefs: SharedPreferences

    companion object {
        private const val PREFS_NAME = "loader_prefs"
        private const val KEY_LOADED_VERSION = "loaded_version"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        setContent {
            var uiState by remember { mutableStateOf<LoaderUiState>(LoaderUiState.Checking) }
            var pendingRelease by remember { mutableStateOf<ReleaseInfo?>(null) }

            // Check for updates on launch
            LaunchedEffect(Unit) {
                checkForUpdate(
                    onState = { uiState = it },
                    onRelease = { pendingRelease = it }
                )
            }

            LoaderHudScreen(
                state = uiState,
                onUpdate = {
                    val release = pendingRelease ?: return@LoaderHudScreen
                    lifecycleScope.launch {
                        downloadAndLaunch(release) { uiState = it }
                    }
                },
                onSkip = { launchCurrent() }
            )
        }
    }

    private suspend fun checkForUpdate(
        onState: (LoaderUiState) -> Unit,
        onRelease: (ReleaseInfo?) -> Unit
    ) {
        onState(LoaderUiState.Checking)

        val release = UpdateChecker.checkLatestRelease()
        if (release == null) {
            // No internet or API error — launch what we have
            launchCurrent()
            return
        }

        val currentVersion = prefs.getString(KEY_LOADED_VERSION, getCurrentAppVersion()) ?: "0.0.0"

        if (UpdateChecker.isNewer(currentVersion, release.version) && release.downloadUrl != null) {
            onRelease(release)
            onState(LoaderUiState.UpdateAvailable(release.version))
        } else {
            // Already up to date
            launchCurrent()
        }
    }

    private suspend fun downloadAndLaunch(release: ReleaseInfo, onState: (LoaderUiState) -> Unit) {
        val url = release.downloadUrl ?: return

        onState(LoaderUiState.Downloading(0f))
        val dexFile = CodeDownloader.download(this@LoaderActivity, url) { progress ->
            lifecycleScope.launch {
                onState(LoaderUiState.Downloading(progress))
            }
        }

        if (dexFile != null) {
            // Save the new version
            prefs.edit().putString(KEY_LOADED_VERSION, release.version).apply()

            onState(LoaderUiState.Loading)
            val launched = DynamicLoader.loadAndLaunch(this@LoaderActivity, dexFile)
            if (!launched) {
                onState(LoaderUiState.Error("Failed to load update"))
            }
        } else {
            onState(LoaderUiState.Error("Download failed"))
        }
    }

    /**
     * Launches the current version — either from a previously downloaded DEX
     * or the built-in app code.
     */
    private fun launchCurrent() {
        val localDex = CodeDownloader.getLocalCodeFile(this)
        if (localDex != null) {
            val launched = DynamicLoader.loadAndLaunch(this, localDex)
            if (launched) return
        }
        // Fallback to built-in
        DynamicLoader.launchBuiltIn(this)
        finish()
    }

    private fun getCurrentAppVersion(): String {
        return try {
            packageManager.getPackageInfo(packageName, 0).versionName ?: "0.0.0"
        } catch (e: Exception) {
            "0.0.0"
        }
    }
}

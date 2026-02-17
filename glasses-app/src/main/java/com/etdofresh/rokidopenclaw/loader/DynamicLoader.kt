package com.etdofresh.rokidopenclaw.loader

import android.content.Context
import android.content.Intent
import dalvik.system.DexClassLoader
import java.io.File

/**
 * Loads a DEX/JAR file dynamically using DexClassLoader and launches
 * the main activity entry point from the loaded code.
 */
object DynamicLoader {

    private const val ENTRY_ACTIVITY = "com.etdofresh.rokidopenclaw.MainActivity"

    /**
     * Attempts to load the given DEX file and launch the entry activity.
     * @return true if launch succeeded, false otherwise
     */
    fun loadAndLaunch(context: Context, dexFile: File): Boolean {
        return try {
            val optimizedDir = File(context.codeCacheDir, "dex_opt").apply { mkdirs() }

            val classLoader = DexClassLoader(
                dexFile.absolutePath,
                optimizedDir.absolutePath,
                null,
                context.classLoader
            )

            val activityClass = classLoader.loadClass(ENTRY_ACTIVITY)
            val intent = Intent(context, activityClass).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Launches the built-in MainActivity directly (fallback when no DEX is available
     * or dynamic loading fails).
     */
    fun launchBuiltIn(context: Context) {
        try {
            val intent = Intent().apply {
                setClassName(context.packageName, ENTRY_ACTIVITY)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

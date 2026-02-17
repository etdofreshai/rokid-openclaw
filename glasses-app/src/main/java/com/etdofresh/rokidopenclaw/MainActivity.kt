package com.etdofresh.rokidopenclaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * Main entry point for the Rokid OpenClaw glasses app.
 * Runs directly on the Rokid AI Glasses (no phone bridge needed).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // TODO: Set up navigation between HUD chat UI and settings
            // TODO: Initialize session management
            // TODO: Request permissions (mic, camera, wifi)
        }
    }
}

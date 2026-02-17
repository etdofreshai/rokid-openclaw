package com.etdofresh.rokidopenclaw.loader

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Green-on-black monochrome HUD UI for the loader.
 * Designed for the 480×640 Rokid micro-LED display.
 */

private val HudGreen = Color(0xFF00FF41)
private val HudDimGreen = Color(0xFF00AA2A)
private val HudBlack = Color.Black
private val HudFont = FontFamily.Monospace

sealed class LoaderUiState {
    object Checking : LoaderUiState()
    data class UpdateAvailable(val version: String) : LoaderUiState()
    data class Downloading(val progress: Float) : LoaderUiState()
    object Loading : LoaderUiState()
    data class Error(val message: String) : LoaderUiState()
}

@Composable
fun LoaderHudScreen(
    state: LoaderUiState,
    onUpdate: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HudBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // App title
            Text(
                text = "◉ OPENCLAW",
                color = HudGreen,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = HudFont
            )

            Spacer(modifier = Modifier.height(32.dp))

            when (state) {
                is LoaderUiState.Checking -> {
                    Text(
                        text = "Checking for updates...",
                        color = HudDimGreen,
                        fontSize = 16.sp,
                        fontFamily = HudFont
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        color = HudGreen,
                        trackColor = HudDimGreen.copy(alpha = 0.3f)
                    )
                }

                is LoaderUiState.UpdateAvailable -> {
                    Text(
                        text = "Update available",
                        color = HudGreen,
                        fontSize = 18.sp,
                        fontFamily = HudFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "v${state.version}",
                        color = HudGreen,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = HudFont
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    // Update button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .background(HudGreen, RoundedCornerShape(4.dp))
                            .clickable { onUpdate() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "▶ TAP TO UPDATE",
                            color = HudBlack,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = HudFont
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Skip button
                    Text(
                        text = "[ SKIP ]",
                        color = HudDimGreen,
                        fontSize = 14.sp,
                        fontFamily = HudFont,
                        modifier = Modifier.clickable { onSkip() }
                    )
                }

                is LoaderUiState.Downloading -> {
                    Text(
                        text = "Downloading update...",
                        color = HudGreen,
                        fontSize = 16.sp,
                        fontFamily = HudFont
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (state.progress >= 0f) {
                        LinearProgressIndicator(
                            progress = { state.progress },
                            modifier = Modifier.fillMaxWidth(0.7f),
                            color = HudGreen,
                            trackColor = HudDimGreen.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${(state.progress * 100).toInt()}%",
                            color = HudGreen,
                            fontSize = 14.sp,
                            fontFamily = HudFont
                        )
                    } else {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(0.7f),
                            color = HudGreen,
                            trackColor = HudDimGreen.copy(alpha = 0.3f)
                        )
                    }
                }

                is LoaderUiState.Loading -> {
                    Text(
                        text = "Loading...",
                        color = HudGreen,
                        fontSize = 18.sp,
                        fontFamily = HudFont
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        color = HudGreen,
                        trackColor = HudDimGreen.copy(alpha = 0.3f)
                    )
                }

                is LoaderUiState.Error -> {
                    Text(
                        text = "⚠ ERROR",
                        color = HudGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = HudFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.message,
                        color = HudDimGreen,
                        fontSize = 14.sp,
                        fontFamily = HudFont,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "[ CONTINUE ANYWAY ]",
                        color = HudGreen,
                        fontSize = 14.sp,
                        fontFamily = HudFont,
                        modifier = Modifier.clickable { onSkip() }
                    )
                }
            }
        }
    }
}

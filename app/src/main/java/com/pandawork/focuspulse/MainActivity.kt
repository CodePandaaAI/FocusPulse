package com.pandawork.focuspulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pandawork.focuspulse.ui.theme.FocusPulseTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusPulseTheme {
                // Main app UI entry point
                FocusPulseScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FocusPulseScreen() {
    val viewModel: TimerViewModel = viewModel()
    val progress by animateFloatAsState(
        targetValue = if (viewModel.initialTime > 0) {
            1f - (viewModel.timeSeconds.toFloat() / viewModel.initialTime.toFloat())
        } else {
            0f
        },
        label = "Timer Progress"
    )

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState()) // Enable scrolling for smaller screens
                .padding(innerPadding).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Timer display
            Box(
                modifier = Modifier
                    .size(280.dp), // Adjusted size for better screen fit
                contentAlignment = Alignment.Center
            ) {
                CircularWavyProgressIndicator(
                    progress = { progress },
                    stroke = Stroke(width = 10.dp.value), // Adjusted stroke width
                    modifier = Modifier.fillMaxSize(),
                    gapSize = 12.dp, // Increased gap
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = formatTime(viewModel.timeSeconds),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 68.sp // Adjusted font size
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Space between timer and buttons
            Spacer(modifier = Modifier.height(32.dp))

            // Preset duration buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val presetDurations = listOf(1, 15, 25)
                presetDurations.forEach { duration ->
                    // Button for each preset duration
                    Button(
                        onClick = {
                            viewModel.setTimer(duration)
                        },
                        modifier = Modifier.weight(1f).height(56.dp), // Adjusted height
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text("${duration}min", // Compact text
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Space between preset buttons and control buttons
            Spacer(modifier = Modifier.height(32.dp))

            // Control buttons (Play/Pause, Reset)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Play/Pause button with dynamic icon
                FilledIconButton(
                    onClick = {
                        if (viewModel.isRunning) {
                            viewModel.pauseTimer()
                        } else {
                            viewModel.startTimer()
                        }
                    },
                    modifier = Modifier.size(110.dp), // Adjusted size
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = if (viewModel.isRunning) R.drawable.ic_pause else R.drawable.ic_play_arrow
                        ),
                        contentDescription = if (viewModel.isRunning) "Pause" else "Play",
                        modifier = Modifier.size(44.dp) // Adjusted icon size
                    )
                }
                // Reset button
                FilledIconButton(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier.size(110.dp), // Adjusted size
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_reset),
                        contentDescription = "Reset",
                        modifier = Modifier.size(44.dp) // Adjusted icon size
                    )
                }
            }
        }
    }
}

// Formats seconds into MM:SS string
private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true, widthDp = 360)
@Composable
private fun FocusPulseScreenPreview() {
    FocusPulseTheme {
        FocusPulseScreen()
    }
}
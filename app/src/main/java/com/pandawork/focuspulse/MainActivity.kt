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
                FocusPulseScreen(viewModel = viewModel())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FocusPulseScreen(viewModel: TimerViewModel = viewModel()) {
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
                .verticalScroll(rememberScrollState())
                .padding(innerPadding).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Timer display
            Box(
                modifier = Modifier
                    .size(300.dp), // Increased size
                contentAlignment = Alignment.Center
            ) {
                CircularWavyProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(), // Fill the Box
                    gapSize = 8.dp, // Increased gap
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = formatTime(viewModel.timeSeconds),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 72.sp // Increased font size for the timer text
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Space between timer and buttons
            Spacer(modifier = Modifier.height(32.dp))

            // Preset duration buttons
            Row( // Center the row of buttons
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val presetDurations = listOf(1, 15, 25)
                presetDurations.forEach { duration ->
                    Button(
                        onClick = { viewModel.setTimer(duration) },
                        modifier = Modifier.weight(1f) // Distribute space evenly
                            .height(48.dp), // Reduced height
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text("$duration min")
                    }
                }
            }

            // Space between preset buttons and control buttons
            Spacer(modifier = Modifier.height(32.dp))

            // Control buttons (Play/Pause, Reset)
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
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
                    modifier = Modifier.size(64.dp), // Reduced size for a more compact look
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
                        modifier = Modifier.size(36.dp) // Reduced icon size for better proportion
                    )
                }
                FilledIconButton(
                    onClick = { viewModel.resetTimer() }, // Reset timer action
                    modifier = Modifier.size(64.dp), // Reduced size
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_reset),
                        contentDescription = "Reset",
                        modifier = Modifier.size(36.dp) // Reduced icon size
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
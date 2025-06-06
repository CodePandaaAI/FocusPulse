package com.pandawork.focuspulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pandawork.focuspulse.ui.theme.FocusPulseTheme

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

@Composable
fun FocusPulseScreen(viewModel: TimerViewModel = viewModel()) {
    val timeDisplay = formatTime(viewModel.timeSeconds)
    // Scaffold: basic Material Design visual structure
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply padding from Scaffold
                .padding(16.dp), // Add some overall padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround // Distribute elements
        ) {
            // --- 1. Timer Display ---
            Surface(
                modifier = Modifier
                    .size(220.dp), // Circular timer size
                shape = MaterialTheme.shapes.extraLarge, // Rounded shape
                color = MaterialTheme.colorScheme.surfaceVariant, // Subtle background
                shadowElevation = 8.dp // Adds depth
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center // Center content
                ) {
                    // Actual timer countdown text
                    Text(
                        text = timeDisplay, // Formatted time from ViewModel
                        style = MaterialTheme.typography.displayLarge, // Large, bold text
                        color = MaterialTheme.colorScheme.primary // Vibrant color
                    )
                    // TODO: Add CircularProgressIndicator here
                }
            }

            Spacer(modifier = Modifier.height(32.dp)) // Vertical space

            // --- 2. Preset Duration Buttons ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly, // Distribute buttons
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.setTimer(5) },
                    shape = MaterialTheme.shapes.large, // Rounded shape
                    modifier = Modifier.weight(1f).height(56.dp) // Fill width evenly
                ) {
                    Text("5 min")
                }
                Spacer(modifier = Modifier.size(12.dp))
                Button(
                    onClick = { viewModel.setTimer(15) },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("15 min")
                }
                Spacer(modifier = Modifier.size(12.dp))
                Button(
                    onClick = { viewModel.setTimer(25) },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("25 min")
                }
            }

            Spacer(modifier = Modifier.height(32.dp)) // Vertical space

            // --- 3. Control Buttons (Start/Pause/Reset) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically // Center buttons
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
                    modifier = Modifier.size(72.dp) // Larger primary action button
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = if (viewModel.isRunning) R.drawable.ic_pause else R.drawable.ic_play_arrow
                        ),
                        contentDescription = "Start/Pause Timer",
                        modifier = Modifier.size(40.dp) // Large icon
                    )
                }
                FilledIconButton(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_reset),
                        contentDescription = "Reset Timer",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

// Formats seconds into MM:SS string
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}
@Preview(showBackground = true, widthDp = 360)
@Composable
fun FocusPulseScreenPreview() {
    FocusPulseTheme {
        FocusPulseScreen()
    }
}
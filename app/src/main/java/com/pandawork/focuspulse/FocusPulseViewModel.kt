package com.pandawork.focuspulse

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    // Current time in seconds. Default: 25 minutes
    var timeSeconds by mutableIntStateOf(DEFAULT_TIMER_DURATION_MINUTES * 60)
        private set

    // Tracks if timer is running
    var isRunning by mutableStateOf(false)
        private set

    // Stores originally set duration for reset
    private var initialDurationSeconds by mutableIntStateOf(DEFAULT_TIMER_DURATION_MINUTES * 60)

    private var countDownTimer: CountDownTimer? = null

    companion object {
        const val DEFAULT_TIMER_DURATION_MINUTES = 25
    }

    fun setTimer(minutes: Int) {
        val newDurationSeconds = minutes * 60
        initialDurationSeconds = newDurationSeconds
        timeSeconds = newDurationSeconds
        if (isRunning) { // If timer is running, reset it with new duration
            pauseTimer()
            startTimer()
        } else { // If paused, just update time and keep it paused
            countDownTimer?.cancel() // Cancel any existing timer
        }
    }

    fun startTimer() {
        if (isRunning || timeSeconds <= 0) return // Prevent starting if already running or time's up

        isRunning = true
        countDownTimer = object : CountDownTimer(timeSeconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeSeconds = (millisUntilFinished / 1000).toInt()
            }
            override fun onFinish() {
                isRunning = false
                timeSeconds = 0
                // TODO: Add sound/notification on finish
            }
        }.start()
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        // timeSeconds remains at its current value
        isRunning = false
    }

    fun resetTimer() {
        countDownTimer?.cancel()
        isRunning = false
        timeSeconds = initialDurationSeconds // Reset to the duration initially set by setTimer()
    }
}
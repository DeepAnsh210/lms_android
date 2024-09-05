package com.example.focus

import FocusTimerScreen
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startForegroundService
import com.example.focus.ui.theme.FocusTheme

class MainActivity : ComponentActivity() {

    private var isServiceRunning by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    FocusTimerScreen(
                        isServiceRunning = isServiceRunning,
                        onStart = { interval, soundResId ->
                            startFocusTimerService(interval, soundResId)
                        },
                        onStop = {
                            stopFocusTimerService()
                        }
                    )
                }
            }
        }
    }


    private fun startFocusTimerService(interval: Long, soundResId: Int) {
        val serviceIntent = Intent(this, FocusTimerService::class.java).apply {
            putExtra("interval", interval)
            putExtra("soundResId", soundResId)
        }
        startService(serviceIntent)
        isServiceRunning = true
    }

private fun stopFocusTimerService() {
    val serviceIntent = Intent(this, FocusTimerService::class.java)
    stopService(serviceIntent)
    isServiceRunning = false
}
}
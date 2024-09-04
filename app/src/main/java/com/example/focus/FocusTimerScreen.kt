package com.example.focus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun FocusTimerScreen(
    isServiceRunning: Boolean,
    onStart: (Long) -> Unit,
    onStop: () -> Unit
) {
    var intervalText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Focus Timer", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        TextField(
            value = intervalText,
            onValueChange = {
                intervalText = it
                errorText = ""
            },
            label = { Text("Interval in minutes") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            isError = errorText.isNotEmpty()
        )
        if (errorText.isNotEmpty()) {
            Text(text = errorText, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val interval = intervalText.toLongOrNull()
                if (interval != null && interval > 0) {
                    if (!isServiceRunning) {
                        onStart(interval)
                    } else {
                        onStop()
                    }
                } else {
                    errorText = "Please enter a valid number greater than 0"
                }
            }
        ) {
            Text(if (isServiceRunning) "Stop" else "Start")
        }
    }
}

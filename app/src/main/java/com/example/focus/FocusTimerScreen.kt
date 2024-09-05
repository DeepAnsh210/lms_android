import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.focus.R

@Composable
fun FocusTimerScreen(
    isServiceRunning: Boolean,
    onStart: (Long, Int) -> Unit,
    onStop: () -> Unit
) {
    var intervalText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedSound by remember { mutableStateOf(R.raw.alertsound) }
    var selectedSoundLabel by remember { mutableStateOf("Beep Sound 1") }


    val soundOptions = listOf(
        Pair("Beep Sound 1", R.raw.alertsound),
        Pair("Beep Sound 2", R.raw.levelup2),
        Pair("Beep Sound 3", R.raw.levelup3),

        Pair("Beep Sound 4", R.raw.simplenotification),
        Pair("Beep Sound 5", R.raw.stop)
    )

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
            isError = errorText.isNotEmpty()
        )

        if (errorText.isNotEmpty()) {
            Text(text = errorText, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sound Selection
        Text(text = "Select Beep Sound:")
        soundOptions.forEach { sound ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (selectedSound == sound.second),
                    onClick = { selectedSound = sound.second }
                )
                Text(text = sound.first)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val interval = intervalText.toLongOrNull()
                if (interval != null && interval > 0) {
                    if (!isServiceRunning) {
                        onStart(interval, selectedSound)
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

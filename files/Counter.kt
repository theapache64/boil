package $PACKAGE_NAME.commonui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Decrement
        Button(onClick = { count-- }) {
            Text(text = "-")
        }
        // Text
        Text(text = "$count", modifier = Modifier.padding(horizontal = 10.dp))
        // Increment
        Button(onClick = { count++ }) {
            Text(text = "+")
        }
    }
}
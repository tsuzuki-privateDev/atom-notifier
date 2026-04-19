package com.tatsu.atomnotifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tatsu.atomnotifier.ui.theme.AtomNotifierTheme

lateinit var server: AlertHttpServer

class MainActivity : ComponentActivity() {
    var condition by mutableStateOf("待機中")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        server = AlertHttpServer(
            8080,
            onAlert = {
                runOnUiThread {
                    condition = "ALERT受信"
                }
            },
            onReset = {
                runOnUiThread {
                    condition = "待機中"
                }
            }
        )
        server.start()

        enableEdgeToEdge()
        setContent {
            AtomNotifierTheme {
                AlertScreen(
                    condition = condition
                )
            }
        }
    }
}

@Composable
fun AlertScreen(condition: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = condition,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AtomNotifierTheme {
        AlertScreen("待機中")
    }
}
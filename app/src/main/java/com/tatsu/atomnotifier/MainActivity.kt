package com.tatsu.atomnotifier

import android.media.MediaPlayer
import android.os.Build
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
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

lateinit var server: AlertHttpServer

class MainActivity : ComponentActivity() {
    var condition by mutableStateOf("待機中")
    var mediaPlayer: MediaPlayer? = null

    fun playAlertSound() {
        // mediaPlayerがnullなら何もしない
        // あったらreleaseする
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(this, R.raw.kokage_de_yuttari_1)
        mediaPlayer?.start()

        // 再生が終了したらreleaseする処理だが、stopAlertSoundを作ったのでコメント化
//        mediaPlayer?.setOnCompletionListener {
//            it.release()
//        }
    }

    fun stopAlertSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        server = AlertHttpServer(
            8080,
            onAlert = {
                runOnUiThread {
                    condition = "ALERT受信"
                    playAlertSound()
                }
            },
            onReset = {
                runOnUiThread {
                    condition = "待機中"
                    stopAlertSound()
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

    if (condition == "ALERT受信") {

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Red),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = condition,
                fontSize = 60.sp,
                color = Color.White
            )
        }

    } else {
        // 現在時刻を取得（初期表示）
        val currentTime = remember {
            mutableStateOf(
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            )
        }

        // currentTime を state として持ち、1秒毎に中身を書き換える
        LaunchedEffect(Unit) {
            while (true) {
                currentTime.value =
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                delay(1000)
            }
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentTime.value,
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AtomNotifierTheme {
        AlertScreen("待機中")
    }
}
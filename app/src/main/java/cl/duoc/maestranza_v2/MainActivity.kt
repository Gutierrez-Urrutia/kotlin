package cl.duoc.maestranza_v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.maestranza_v2.ui.screens.welcome.WelcomeScreen
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Maestranza_V2Theme {
                WelcomeScreen()
            }
        }
    }
}
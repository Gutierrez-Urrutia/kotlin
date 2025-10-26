package cl.duoc.maestranza_v2.ui.screens.welcome

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass // <-- Llama al helper

@Composable
fun WelcomeScreen() {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> WelcomeScreenCompact()
        WindowWidthSizeClass.Medium -> WelcomeScreenMedium()
        WindowWidthSizeClass.Expanded -> WelcomeScreenExpanded()
    }
}
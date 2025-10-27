package cl.duoc.maestranza_v2.ui.screens.welcome

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass // <-- Llama al helper
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@Composable
fun WelcomeScreen(navController: NavController, viewModel: MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> WelcomeScreenCompact(navController, viewModel)
        WindowWidthSizeClass.Medium -> WelcomeScreenMedium(navController, viewModel)
        WindowWidthSizeClass.Expanded -> WelcomeScreenExpanded(navController, viewModel)
    }
}
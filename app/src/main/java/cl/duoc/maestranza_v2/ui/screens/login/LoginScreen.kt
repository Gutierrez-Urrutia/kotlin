package cl.duoc.maestranza_v2.ui.screens.login

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> LoginScreenCompact(navController, viewModel)
        WindowWidthSizeClass.Medium -> LoginScreenMedium(navController, viewModel)
        WindowWidthSizeClass.Expanded -> LoginScreenExpanded(navController, viewModel)
    }
}
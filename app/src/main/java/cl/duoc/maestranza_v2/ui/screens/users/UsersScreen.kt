package cl.duoc.maestranza_v2.ui.screens.user

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.ui.screens.users.*
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@Composable
fun UserScreen(navController : NavController, viewModel : MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> UserScreenCompact(navController, viewModel)
        WindowWidthSizeClass.Medium -> UserScreenMedium(navController, viewModel)
        WindowWidthSizeClass.Expanded -> UserScreenExpanded(navController, viewModel)
    }
}
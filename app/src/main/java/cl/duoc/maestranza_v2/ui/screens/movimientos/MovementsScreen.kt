package cl.duoc.maestranza_v2.ui.screens.movimientos

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@Composable
fun MovementsScreen(navController: NavController, viewModel: MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> cl.duoc.maestranza_v2.ui.screens.movimientos.MovementsScreenCompact(navController, viewModel)
        WindowWidthSizeClass.Medium -> cl.duoc.maestranza_v2.ui.screens.movimientos.MovementsScreenMedium(navController, viewModel)
        WindowWidthSizeClass.Expanded -> cl.duoc.maestranza_v2.ui.screens.movimientos.MovementsScreenExpanded(navController, viewModel)
    }
}


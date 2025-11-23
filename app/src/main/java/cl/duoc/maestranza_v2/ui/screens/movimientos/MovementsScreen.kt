package cl.duoc.maestranza_v2.ui.screens.movimientos

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass
import cl.duoc.maestranza_v2.viewmodel.MainViewModel
import cl.duoc.maestranza_v2.viewmodel.MovementsViewModel

@Composable
fun MovementsScreen(navController: NavController, viewModel: MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()
    val movementsViewModel: MovementsViewModel = viewModel()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> MovementsScreenCompact(navController, viewModel, movementsViewModel)
        WindowWidthSizeClass.Medium -> MovementsScreenMedium(navController, viewModel, movementsViewModel)
        WindowWidthSizeClass.Expanded -> MovementsScreenExpanded(navController, viewModel, movementsViewModel)
    }
}


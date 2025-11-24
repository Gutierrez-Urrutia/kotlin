package cl.duoc.maestranza_v2.ui.screens.movimientos

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass
import cl.duoc.maestranza_v2.viewmodel.MainViewModel
import cl.duoc.maestranza_v2.viewmodel.MovementsViewModel
import cl.duoc.maestranza_v2.viewmodel.AuthViewModel

@Composable
fun MovementsScreen(navController: NavController, viewModel: MainViewModel, authViewModel: AuthViewModel? = null) {
    val windowSizeClass = obtenerWindowSizeClass()
    val movementsViewModel: MovementsViewModel = viewModel()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> MovementsScreenCompact(navController, viewModel, movementsViewModel, authViewModel)
        WindowWidthSizeClass.Medium -> MovementsScreenMedium(navController, viewModel, movementsViewModel, authViewModel)
        WindowWidthSizeClass.Expanded -> MovementsScreenExpanded(navController, viewModel, movementsViewModel, authViewModel)
    }
}


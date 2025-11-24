package cl.duoc.maestranza_v2.ui.screens.movimientos

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass
import cl.duoc.maestranza_v2.viewmodel.MainViewModel
import cl.duoc.maestranza_v2.viewmodel.MovementsViewModel
import cl.duoc.maestranza_v2.viewmodel.AuthViewModel

@Composable
fun MovementsScreen(navController: NavController, viewModel: MainViewModel, authViewModel: AuthViewModel? = null) {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> MovementsScreenCompact(navController, viewModel, authViewModel)
        WindowWidthSizeClass.Medium -> MovementsScreenMedium(navController, viewModel, authViewModel)
        WindowWidthSizeClass.Expanded -> MovementsScreenExpanded(navController, viewModel, authViewModel)
    }
}


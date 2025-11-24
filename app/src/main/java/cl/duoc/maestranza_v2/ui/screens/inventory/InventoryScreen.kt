package cl.duoc.maestranza_v2.ui.screens.inventory

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass
import cl.duoc.maestranza_v2.viewmodel.MainViewModel
import cl.duoc.maestranza_v2.viewmodel.AuthViewModel

@Composable
fun InventoryScreen(navController : NavController, viewModel : MainViewModel, authViewModel: AuthViewModel? = null) {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> InventoryScreenCompact(navController, viewModel, authViewModel)
        WindowWidthSizeClass.Medium -> InventoryScreenMedium(navController, viewModel, authViewModel)
        WindowWidthSizeClass.Expanded -> InventoryScreenExpanded(navController, viewModel, authViewModel)
    }
}
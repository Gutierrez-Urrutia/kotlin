package cl.duoc.maestranza_v2.ui.screens.inventory

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@Composable
fun InventoryScreen(navController : NavController, viewModel : MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> InventoryScreenCompact(navController, viewModel)
        WindowWidthSizeClass.Medium -> InventoryScreenMedium(navController, viewModel)
        WindowWidthSizeClass.Expanded -> InventoryScreenExpanded(navController, viewModel)
    }
}
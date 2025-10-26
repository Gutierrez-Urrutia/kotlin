package cl.duoc.maestranza_v2.ui.screens.inventory

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.maestranza_v2.ui.utils.obtenerWindowSizeClass

@Composable
fun InventoryScreen() {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> InventoryScreenCompact()
        WindowWidthSizeClass.Medium -> InventoryScreenMedium()
        WindowWidthSizeClass.Expanded -> InventoryScreenExpanded()
    }
}
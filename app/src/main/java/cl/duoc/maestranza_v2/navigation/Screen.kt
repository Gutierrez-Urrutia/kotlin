package cl.duoc.maestranza_v2.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Inventory : Screen("inventario")
    data object Users : Screen("usuarios")
    data object AddProduct : Screen("agregarProducto")
    data object Movements : Screen("movimientos")
    data object EditProduct : Screen("editarProducto/{productCode}") {
        fun createRoute(productCode: String) = "editarProducto/$productCode"
    }
}
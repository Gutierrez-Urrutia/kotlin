package cl.duoc.maestranza_v2.navigation
sealed class Screen(val route: String) {
    data object  Welcome : Screen("welcome")
    data object Inventory : Screen("inventario")
    data object Users : Screen("usuarios")
    data object AddProduct : Screen("agregarProducto")
}

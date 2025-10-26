package cl.duoc.maestranza_v2.navigation
sealed class Screen(val route: String) {
    data object  Welcome : Screen("welcome_page")
    data object Inventory : Screen("inventory_page")
}

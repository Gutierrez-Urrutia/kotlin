package cl.duoc.maestranza_v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.maestranza_v2.viewmodel.MainViewModel
import cl.duoc.maestranza_v2.viewmodel.AuthViewModel
import cl.duoc.maestranza_v2.navigation.NavigationEvent
import cl.duoc.maestranza_v2.navigation.Screen
import cl.duoc.maestranza_v2.ui.screens.agregarProducto.AddProductScreen
import cl.duoc.maestranza_v2.ui.screens.editarProducto.EditProductScreen
import cl.duoc.maestranza_v2.ui.screens.editarUsuario.EditUserScreen
import cl.duoc.maestranza_v2.ui.screens.inventory.InventoryScreen
import cl.duoc.maestranza_v2.ui.screens.login.LoginScreen
import cl.duoc.maestranza_v2.ui.screens.movimientos.MovementsScreen
import cl.duoc.maestranza_v2.ui.screens.user.UsersScreen

import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.UsersViewModel
import kotlinx.coroutines.flow.collectLatest

import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Maestranza_V2Theme {
                val mainViewModel : MainViewModel = viewModel()
                val navController = rememberNavController()
                val context = this
                val authViewModel: AuthViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return AuthViewModel(context) as T
                    }
                })
                val authState by authViewModel.uiState.collectAsState()

                LaunchedEffect(key1 = Unit){
                    mainViewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(route = event.route.route)
                            }
                            is NavigationEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                            is NavigationEvent.NavigateUp -> {
                                navController.navigateUp()
                            }
                        }
                    }
                }

                // Escuchar cambios en isAuthenticated para redirigir al logout
                LaunchedEffect(authViewModel) {
                    snapshotFlow { authState.isAuthenticated }
                        .collect { isAuthenticated ->
                            if (!isAuthenticated && navController.currentDestination?.route != Screen.Login.route) {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                }
                Scaffold(modifier = Modifier.fillMaxSize()){ innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(paddingValues = innerPadding))
                    {
                        composable(route = Screen.Login.route) {
                            LoginScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable(route = Screen.Inventory.route){
                            InventoryScreen(navController = navController, viewModel = mainViewModel, authViewModel = authViewModel)
                        }
                        composable(route = Screen.Users.route){
                            UsersScreen(navController = navController, viewModel = mainViewModel, authViewModel = authViewModel)
                        }
                        composable(route = Screen.AddProduct.route){
                            AddProductScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                        composable(route = Screen.Movements.route){
                            MovementsScreen(navController = navController, viewModel = mainViewModel, authViewModel = authViewModel)
                        }
                        composable(
                            route = Screen.EditProduct.route,
                            arguments = listOf(navArgument("productCode") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val productCode = backStackEntry.arguments?.getString("productCode") ?: ""
                            EditProductScreen(
                                navController = navController,
                                productCode = productCode,
                                authViewModel = authViewModel
                            )
                        }
                        composable(
                            route = Screen.EditUser.route,
                            arguments = listOf(navArgument("userId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val userId = backStackEntry.arguments?.getString("userId") ?: ""
                            val usersViewModel: UsersViewModel = viewModel()
                            EditUserScreen(
                                navController = navController,
                                mainViewModel = mainViewModel,
                                usersViewModel = usersViewModel,
                                userId = userId,
                                authViewModel = authViewModel
                            )
                        }
                    }
                }

            }
        }
    }
}
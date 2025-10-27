package cl.duoc.maestranza_v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.viewmodel.MainViewModel
import cl.duoc.maestranza_v2.navigation.NavigationEvent
import cl.duoc.maestranza_v2.navigation.Screen
import cl.duoc.maestranza_v2.ui.screens.agregarProducto.AddProductScreen
import cl.duoc.maestranza_v2.ui.screens.inventory.InventoryScreen
import cl.duoc.maestranza_v2.ui.screens.user.UserScreen
import cl.duoc.maestranza_v2.ui.screens.welcome.WelcomeScreen
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Maestranza_V2Theme {
                val mainViewModel : MainViewModel = viewModel()
                val navController = rememberNavController()
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
                Scaffold(modifier = Modifier.fillMaxSize()){ innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Welcome.route,
                        modifier = Modifier.padding(paddingValues = innerPadding))
                    {
                        composable(route = Screen.Welcome.route) {
                            WelcomeScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable(route = Screen.Inventory.route){
                            InventoryScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable(route = Screen.Users.route){
                            UserScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable(route = Screen.AddProduct.route){
                            AddProductScreen(
                                navController = navController,
                                mainViewModel = mainViewModel
                            )
                        }
                    }
                }

            }
        }
    }
}
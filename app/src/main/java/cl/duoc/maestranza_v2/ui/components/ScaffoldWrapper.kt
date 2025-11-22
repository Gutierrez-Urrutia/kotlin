package cl.duoc.maestranza_v2.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWrapper(
    navController: NavController,
    showDrawer: Boolean = true,
    title: String = "Maestranza V2",
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (showDrawer) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerShape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                ) {
                    DrawerContent(
                        navController = navController,
                        currentRoute = currentRoute,
                        onItemClick = {
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(title) },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menú"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                content(innerPadding)
            }
        }
    } else {
        Scaffold(
            topBar = { }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}
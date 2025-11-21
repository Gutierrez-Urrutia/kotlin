package cl.duoc.maestranza_v2.ui.screens.inventory

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.navigation.Screen
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

data class InventoryItem(
    val code: String,
    val name: String,
    val category: String,
    val stock: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenCompact(
    navController: NavController,
    viewModel: MainViewModel
) {
    val inventoryList by viewModel.inventoryItems.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val items = listOf(Screen.Inventory, Screen.Users)
    var selectedItem by remember { mutableStateOf(0) }

    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Inventario") },
                navigationIcon = {
                    IconButton(onClick = { /* Acción futura */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción futura */}) {
                        Icon(Icons.Default.Person, contentDescription = "Opciones de Usuario")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            // Usamos el ViewModel para navegar, igual que en la guía
                            viewModel.navigateTo(screen)
                        },
                        label = { Text(text = screen.route.replaceFirstChar { it.uppercase() }) },
                        icon = {
                            Icon(
                                imageVector = if (screen == Screen.Inventory) Icons.Default.Inventory else Icons.Default.Person,
                                contentDescription = screen.route
                            )
                        }
                    )
                }
            }
        }


    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar en inventario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                trailingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                LazyColumn(modifier = Modifier.width(500.dp)) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("Código", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp))
                            Text("Nombre", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(200.dp)
                                .padding(8.dp))
                            Text("Categoría", fontWeight = FontWeight.Bold, modifier = Modifier
                                .width(180.dp)
                                .padding(8.dp))
                        }
                        Divider()
                    }
                    items(inventoryList) { item ->
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(item.code, modifier = Modifier
                                .width(120.dp)
                                .padding(8.dp))
                            Text(item.name, modifier = Modifier
                                .width(200.dp)
                                .padding(8.dp))
                            Text(item.category, modifier = Modifier
                                .width(180.dp)
                                .padding(8.dp))
                        }
                        Divider()
                    }
                }
            }
        }
    }
}


@SuppressLint("ComposableNaming", "ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "Compact",
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun InventoryScreenCompactPreview() {
    Maestranza_V2Theme {
        InventoryScreenCompact(
            navController = rememberNavController(),
            viewModel = MainViewModel()
        )
    }
}

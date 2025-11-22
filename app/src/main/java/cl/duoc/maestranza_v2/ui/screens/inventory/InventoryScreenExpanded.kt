package cl.duoc.maestranza_v2.ui.screens.inventory

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenExpanded(
    navController: NavController,
    viewModel: MainViewModel
) {
    val inventoryList by viewModel.inventoryItems.collectAsState()
    var searchText by remember { mutableStateOf("") }

    cl.duoc.maestranza_v2.ui.components.ScaffoldWrapper(
        navController = navController,
        showDrawer = true,
        title = "Gestión de Inventario"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar por nombre o código") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                trailingIcon = { Icon(Icons.Default.Search, "Buscar") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Código", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(1.5f)
                            .padding(12.dp))
                        Text("Nombre", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(3f)
                            .padding(12.dp))
                        Text("Categoría", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(2.5f)
                            .padding(12.dp))
                        Text("Stock", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(1f)
                            .padding(12.dp))
                        Text("Acciones", fontWeight = FontWeight.Bold, modifier = Modifier
                            .weight(1f)
                            .padding(12.dp))
                    }
                    Divider()
                }
                items(inventoryList) { item ->
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(item.code, modifier = Modifier
                            .weight(1.5f)
                            .padding(12.dp))
                        Text(item.name, modifier = Modifier
                            .weight(3f)
                            .padding(12.dp))
                        Text(item.category, modifier = Modifier
                            .weight(2.5f)
                            .padding(12.dp))
                        Text(item.stock.toString(), modifier = Modifier
                            .weight(1f)
                            .padding(12.dp))
                        IconButton(onClick = { /* Acción futura */ }, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                    }
                    Divider()
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
fun InventoryScreenExpandedPreview() {
    Maestranza_V2Theme {
        InventoryScreenExpanded(
            navController = rememberNavController(),
            viewModel = MainViewModel()
        )
    }
}
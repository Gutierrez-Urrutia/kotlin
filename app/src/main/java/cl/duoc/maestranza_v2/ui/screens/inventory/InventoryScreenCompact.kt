package cl.duoc.maestranza_v2.ui.screens.inventory

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.navigation.Screen
import cl.duoc.maestranza_v2.ui.components.AddProductBottomSheet
import cl.duoc.maestranza_v2.ui.components.DeleteConfirmationDialog
import cl.duoc.maestranza_v2.ui.components.FiltersBottomSheet
import cl.duoc.maestranza_v2.ui.components.ProductCard
import cl.duoc.maestranza_v2.ui.components.StockFilter
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

data class InventoryItem(
    val code: String,
    val name: String,
    val category: String,
    val description: String = "",
    val price: Double = 0.0,
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
    var showFilters by remember { mutableStateOf(false) }
    var showAddProduct by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<InventoryItem?>(null) }

    // Estados de filtros (hardcoded para demo)
    val categories = listOf("Herramientas", "Materiales", "Equipos", "Consumibles")
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var stockFilter by remember { mutableStateOf(StockFilter.All) }

    // Filtrar lista según búsqueda y filtros
    val filteredList = remember(searchText, selectedCategory, stockFilter, inventoryList) {
        inventoryList.filter { item ->
            val matchesSearch = item.name.contains(searchText, ignoreCase = true) ||
                    item.code.contains(searchText, ignoreCase = true)
            val matchesCategory = selectedCategory == null || item.category == selectedCategory
            val matchesStock = when (stockFilter) {
                StockFilter.All -> true
                StockFilter.InStock -> item.stock > 10
                StockFilter.LowStock -> item.stock in 1..10
                StockFilter.OutOfStock -> item.stock == 0
            }
            matchesSearch && matchesCategory && matchesStock
        }
    }

    cl.duoc.maestranza_v2.ui.components.ScaffoldWrapper(
        navController = navController,
        showDrawer = true,
        title = "Gestión de Inventario",
        actions = {
            IconButton(onClick = { showFilters = true }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtros")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddProduct = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Campo de búsqueda
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar por nombre o código") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // Chips con filtros activos
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                selectedCategory?.let {
                    AssistChip(
                        onClick = { showFilters = true },
                        label = { Text(it) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                    Spacer(Modifier.width(8.dp))
                }
                if (stockFilter != StockFilter.All) {
                    AssistChip(
                        onClick = { showFilters = true },
                        label = { Text(stockFilter.name) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Lista de productos con cards
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList, key = { it.code }) { product ->
                    ProductCard(
                        product = product,
                        onClick = { /* Navegar a detalle */ },
                        onEdit = { productCode ->
                            navController.navigate(
                                cl.duoc.maestranza_v2.navigation.Screen.EditProduct.createRoute(productCode)
                            )
                        },
                        onDelete = {
                            productToDelete = product
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }

        // Bottom sheet de filtros
        if (showFilters) {
            FiltersBottomSheet(
                categories = categories,
                selectedCategory = selectedCategory,
                stockFilter = stockFilter,
                onCategorySelected = { selectedCategory = it },
                onStockFilterSelected = { stockFilter = it },
                onClear = {
                    selectedCategory = null
                    stockFilter = StockFilter.All
                },
                onDismiss = { showFilters = false }
            )
        }

        // Bottom sheet para agregar producto
        if (showAddProduct) {
            AddProductBottomSheet(
                inventoryList = inventoryList,
                onDismiss = { showAddProduct = false },
                onProductAdded = { code, name, category, description, price, stock ->
                    viewModel.addProduct(code, name, category, description, price, stock)
                }
            )
        }

        // Diálogo de confirmación de eliminación
        DeleteConfirmationDialog(
            showDialog = showDeleteDialog,
            productName = productToDelete?.name ?: "",
            onDismiss = {
                showDeleteDialog = false
                productToDelete = null
            },
            onConfirm = {
                // TODO: Implementar lógica de eliminación
                showDeleteDialog = false
                productToDelete = null
            }
        )
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

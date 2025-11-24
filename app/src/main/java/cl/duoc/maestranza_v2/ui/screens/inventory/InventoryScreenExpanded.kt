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
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.navigation.Screen
import cl.duoc.maestranza_v2.ui.components.DeleteConfirmationDialog
import cl.duoc.maestranza_v2.ui.components.FiltersBottomSheet
import cl.duoc.maestranza_v2.ui.components.ProductCard
import cl.duoc.maestranza_v2.ui.components.StockFilter
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.InventoryViewModel
import cl.duoc.maestranza_v2.viewmodel.InventoryState
import cl.duoc.maestranza_v2.viewmodel.MainViewModel
import cl.duoc.maestranza_v2.viewmodel.AuthViewModel
import cl.duoc.maestranza_v2.data.model.InventoryItem
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenExpanded(
    navController: NavController,
    viewModel: MainViewModel,
    authViewModel: AuthViewModel? = null
) {
    val context = LocalContext.current

    val inventoryViewModel = remember {
        InventoryViewModel(context = context)
    }

    val inventoryState by inventoryViewModel.inventoryState.collectAsState()

    // Cargar inventario cuando se monta el composable
    LaunchedEffect(Unit) {
        inventoryViewModel.loadInventory()
    }

    val inventoryList = when (inventoryState) {
        is InventoryState.Success -> (inventoryState as InventoryState.Success).items
        else -> emptyList()
    }


    var searchText by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<InventoryItem?>(null) }

    // Extraer categorías dinámicamente
    val categories = remember(inventoryList) {
        inventoryList.map { it.category }.distinct().sorted()
    }
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
        authViewModel = authViewModel,
        showDrawer = true,
        title = "Gestión de Inventario",
        actions = {
            IconButton(onClick = { showFilters = true }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtros")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddProduct.route) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            when (inventoryState) {
                is InventoryState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(16.dp))
                            Text("Cargando inventario...")
                        }
                    }
                }
                is InventoryState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Error al cargar el inventario",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                (inventoryState as InventoryState.Error).message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(Modifier.height(24.dp))
                            Button(onClick = { inventoryViewModel.retry() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                is InventoryState.Success -> {
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

                    // Mostrar mensaje si no hay resultados
                    if (filteredList.isEmpty() && inventoryList.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se encontraron productos")
                        }
                    } else if (filteredList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay productos en el inventario")
                        }
                    } else {
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

        // Diálogo de confirmación de eliminación
        DeleteConfirmationDialog(
            showDialog = showDeleteDialog,
            productName = productToDelete?.name ?: "",
            onDismiss = {
                showDeleteDialog = false
                productToDelete = null
            },
            onConfirm = {
                productToDelete?.let { product ->
                    inventoryViewModel.deleteProductByCode(product.code)
                }
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
fun InventoryScreenExpandedPreview() {
    Maestranza_V2Theme {
        InventoryScreenExpanded(
            navController = rememberNavController(),
            viewModel = MainViewModel()
        )
    }
}
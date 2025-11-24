package cl.duoc.maestranza_v2.ui.screens.editarProducto

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.EditProductViewModel
import cl.duoc.maestranza_v2.viewmodel.InventoryViewModel
import cl.duoc.maestranza_v2.viewmodel.InventoryState
import cl.duoc.maestranza_v2.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    productCode: String,
    authViewModel: AuthViewModel? = null
) {
    val context = LocalContext.current
    val editProductViewModel: EditProductViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return EditProductViewModel(context) as T
            }
        }
    )

    val estado by editProductViewModel.estado.collectAsState()
    val categorias by editProductViewModel.categorias.collectAsState()
    var categoriasExpanded by remember { mutableStateOf(false) }

    // Obtener InventoryViewModel para leer inventario y operar via API
    val inventoryViewModel: InventoryViewModel = viewModel()
    val inventoryState by inventoryViewModel.inventoryState.collectAsState()
    val inventoryList = when (inventoryState) {
        is InventoryState.Success -> (inventoryState as InventoryState.Success).items
        else -> emptyList()
    }
    val product = inventoryList.find { it.code == productCode }

    // Cargar el producto cuando se monta el componente
    LaunchedEffect(productCode) {
        product?.let {
            editProductViewModel.loadProduct(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            // Código
            OutlinedTextField(
                value = estado.codigo,
                onValueChange = editProductViewModel::onCodigoChange,
                label = { Text("Código del Producto") },
                placeholder = { Text("Ej: HCOR-001") },
                isError = estado.errores.codigo != null,
                supportingText = {
                    estado.errores.codigo?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Nombre
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = editProductViewModel::onNombreChange,
                label = { Text("Nombre del Producto") },
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Categoría (Dropdown)
            ExposedDropdownMenuBox(
                expanded = categoriasExpanded,
                onExpandedChange = { categoriasExpanded = !categoriasExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = estado.categoria,
                    onValueChange = editProductViewModel::onCategoriaChange,
                    label = { Text("Categoría") },
                    isError = estado.errores.categoria != null,
                    supportingText = {
                        estado.errores.categoria?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriasExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = categoriasExpanded,
                    onDismissRequest = { categoriasExpanded = false }
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nombre) },
                            onClick = {
                                editProductViewModel.onCategoriaChange(categoria.nombre)
                                categoriasExpanded = false
                            }
                        )
                    }
                }
            }

            // Descripción
            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = editProductViewModel::onDescripcionChange,
                label = { Text("Descripción (Opcional)") },
                placeholder = { Text("Ej: Broca de acero templado para metal") },
                isError = estado.errores.descripcion != null,
                supportingText = {
                    estado.errores.descripcion?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // Precio
            OutlinedTextField(
                value = estado.precio,
                onValueChange = editProductViewModel::onPrecioChange,
                label = { Text("Precio") },
                placeholder = { Text("Ej: 1500") },
                isError = estado.errores.precio != null,
                supportingText = {
                    estado.errores.precio?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                prefix = { Text("$") }
            )

            // Stock
            OutlinedTextField(
                value = estado.stock,
                onValueChange = editProductViewModel::onStockChange,
                label = { Text("Stock") },
                placeholder = { Text("Ej: 50") },
                isError = estado.errores.stock != null,
                supportingText = {
                    estado.errores.stock?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón Guardar Cambios
            Button(
                onClick = {
                    if (editProductViewModel.validarFormulario(inventoryList, productCode)) {
                        // Actualizar el producto usando el código
                        inventoryViewModel.updateProduct(
                            code = estado.codigo,
                            name = estado.nombre,
                            category = estado.categoria,
                            description = estado.descripcion,
                            price = estado.precio.toDoubleOrNull() ?: 0.0,
                            stock = estado.stock.toIntOrNull() ?: 0
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Guardar Cambios")
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun EditProductScreenPreview() {
    Maestranza_V2Theme {
        EditProductScreen(
            navController = rememberNavController(),
            productCode = "HCOR-001"
        )
    }
}


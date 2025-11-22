package cl.duoc.maestranza_v2.ui.screens.editarProducto

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.EditProductViewModel
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    productCode: String,
    viewModel: EditProductViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()
    val inventoryList by mainViewModel.inventoryItems.collectAsState()
    val product = mainViewModel.getProductByCode(productCode)

    // Cargar el producto cuando se monta el componente
    LaunchedEffect(productCode) {
        product?.let {
            viewModel.loadProduct(it)
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
                onValueChange = viewModel::onCodigoChange,
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
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre del Producto") },
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Categoría
            OutlinedTextField(
                value = estado.categoria,
                onValueChange = viewModel::onCategoriaChange,
                label = { Text("Categoría") },
                isError = estado.errores.categoria != null,
                supportingText = {
                    estado.errores.categoria?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Descripción
            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = viewModel::onDescripcionChange,
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
                onValueChange = viewModel::onPrecioChange,
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
                onValueChange = viewModel::onStockChange,
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
                    if (viewModel.validarFormulario(inventoryList, productCode)) {
                        mainViewModel.updateProduct(
                            originalCode = productCode,
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
            mainViewModel = MainViewModel(),
            productCode = "HCOR-001"
        )
    }
}


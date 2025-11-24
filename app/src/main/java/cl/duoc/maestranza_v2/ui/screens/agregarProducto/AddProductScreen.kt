package cl.duoc.maestranza_v2.ui.screens.agregarProducto

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.AddProductViewModel
import cl.duoc.maestranza_v2.viewmodel.AuthViewModel
import cl.duoc.maestranza_v2.viewmodel.InventoryViewModel
import cl.duoc.maestranza_v2.viewmodel.InventoryState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    authViewModel: AuthViewModel? = null,
    addProductViewModel: AddProductViewModel = viewModel()
) {
    val estado by addProductViewModel.estado.collectAsState()

    // Obtener InventoryViewModel para leer inventario y operar via API
    val inventoryViewModel: InventoryViewModel = viewModel()
    val inventoryState by inventoryViewModel.inventoryState.collectAsState()
    val inventoryList = when (inventoryState) {
        is InventoryState.Success -> (inventoryState as InventoryState.Success).items
        else -> emptyList()
    }

    cl.duoc.maestranza_v2.ui.components.ScaffoldWrapper(
        navController = navController,
        authViewModel = authViewModel,
        showDrawer = true,
        title = "Agregar Producto"
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {

            OutlinedTextField(
                value = estado.codigo,
                onValueChange = addProductViewModel::onCodigoChange,
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

            OutlinedTextField(
                value = estado.nombre,
                onValueChange = addProductViewModel::onNombreChange,
                label = { Text("Nombre del Producto") },
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.categoria,
                onValueChange = addProductViewModel::onCategoriaChange,
                label = { Text("Categoría") },
                isError = estado.errores.categoria != null,
                supportingText = {
                    estado.errores.categoria?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = addProductViewModel::onDescripcionChange,
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

            OutlinedTextField(
                value = estado.precio,
                onValueChange = addProductViewModel::onPrecioChange,
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

            OutlinedTextField(
                value = estado.stock,
                onValueChange = addProductViewModel::onStockChange,
                label = { Text("Stock Inicial") },
                placeholder = { Text("Ej: 0") },
                isError = estado.errores.stock != null,
                supportingText = {
                    estado.errores.stock?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

            Button(
                onClick = {
                    if (addProductViewModel.validarFormulario(inventoryList)) {
                        // Llamar al InventoryViewModel para crear el producto vía API
                        inventoryViewModel.addProduct(
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
                Text(text = "Guardar Producto")
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddProductScreenPreview() {
    Maestranza_V2Theme {
        AddProductScreen(navController = rememberNavController())
    }
}

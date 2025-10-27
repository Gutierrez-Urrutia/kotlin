package cl.duoc.maestranza_v2.ui.screens.agregarProducto

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import cl.duoc.maestranza_v2.viewmodel.AddProductViewModel
import cl.duoc.maestranza_v2.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    mainViewModel : MainViewModel,
    viewModel: AddProductViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()
    val inventoryList by mainViewModel.inventoryItems.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto") },
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
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {

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

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

            Button(
                onClick = {
                    if (viewModel.validarFormulario(inventoryList)) {
                        mainViewModel.addProduct(
                            code = estado.codigo,
                            name = estado.nombre,
                            category = estado.categoria
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
        AddProductScreen(navController = rememberNavController(),
            mainViewModel = MainViewModel())
    }
}

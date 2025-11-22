package cl.duoc.maestranza_v2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.maestranza_v2.ui.screens.inventory.InventoryItem
import cl.duoc.maestranza_v2.viewmodel.AddProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductBottomSheet(
    inventoryList: List<InventoryItem>,
    onDismiss: () -> Unit,
    onProductAdded: (code: String, name: String, category: String, description: String, price: Double, stock: Int) -> Unit,
    viewModel: AddProductViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.resetForm()
            onDismiss()
        },
        containerColor = MaterialTheme.colorScheme.surface,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            // Header fijo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agregar Producto",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar"
                    )
                }
            }

            HorizontalDivider()

            // Body scrolleable
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Nombre
                OutlinedTextField(
                    value = estado.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = { Text("Nombre del Producto") },
                    placeholder = { Text("Ej: Broca HSS 5mm") },
                    isError = estado.errores.nombre != null,
                    supportingText = {
                        estado.errores.nombre?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Categoría
                OutlinedTextField(
                    value = estado.categoria,
                    onValueChange = viewModel::onCategoriaChange,
                    label = { Text("Categoría") },
                    placeholder = { Text("Ej: Herramientas") },
                    isError = estado.errores.categoria != null,
                    supportingText = {
                        estado.errores.categoria?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        } ?: Text(
                            text = "Herramientas, Materiales, Equipos, Consumibles",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Descripción (opcional)
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

                // Precio (requerido)
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

                // Stock inicial
                OutlinedTextField(
                    value = estado.stock,
                    onValueChange = viewModel::onStockChange,
                    label = { Text("Stock Inicial") },
                    placeholder = { Text("0") },
                    isError = estado.errores.stock != null,
                    supportingText = {
                        estado.errores.stock?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Footer fijo con botones
            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Cancelar
                OutlinedButton(
                    onClick = {
                        viewModel.resetForm()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                // Botón Agregar (CTA principal)
                Button(
                    onClick = {
                        if (viewModel.validarFormulario(inventoryList)) {
                            onProductAdded(
                                estado.codigo,
                                estado.nombre,
                                estado.categoria,
                                estado.descripcion,
                                estado.precio.toDoubleOrNull() ?: 0.0,
                                estado.stock.toIntOrNull() ?: 0
                            )
                            viewModel.resetForm()
                            onDismiss()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Agregar")
                }
            }
        }
    }
}


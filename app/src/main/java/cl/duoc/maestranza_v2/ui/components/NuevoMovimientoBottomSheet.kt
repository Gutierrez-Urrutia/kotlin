package cl.duoc.maestranza_v2.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.maestranza_v2.model.MovementType
import cl.duoc.maestranza_v2.viewmodel.ComprobanteSource
import cl.duoc.maestranza_v2.viewmodel.FormField
import cl.duoc.maestranza_v2.viewmodel.NuevoMovimientoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoMovimientoBottomSheet(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: NuevoMovimientoViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Launchers para galería y cámara
    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onComprobanteSeleccionado(it, ComprobanteSource.GALERIA) }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && state.comprobanteUri != null) {
            viewModel.onComprobanteSeleccionado(state.comprobanteUri!!, ComprobanteSource.CAMARA)
        }
    }

    LaunchedEffect(state.exito) {
        if (state.exito) {
            viewModel.reset()
            onSuccess()
            onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.reset()
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
                    text = "Nuevo Movimiento",
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
                // Tipo de Movimiento
                Column {
                    Text(
                        "Tipo de Movimiento*",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    var expandedTipo by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { expandedTipo = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(state.tipoSeleccionado?.name ?: "Seleccionar")
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(18.dp))
                        }

                        DropdownMenu(
                            expanded = expandedTipo,
                            onDismissRequest = { expandedTipo = false }
                        ) {
                            MovementType.values().forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo.name) },
                                    onClick = {
                                        viewModel.onTipoChanged(tipo)
                                        expandedTipo = false
                                    }
                                )
                            }
                        }
                    }

                    state.errores[FormField.TIPO]?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Código del Producto
                Column {
                    Text(
                        "Código del Producto*",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    var expandedProducto by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { expandedProducto = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(state.productoSeleccionado?.codigo ?: "Seleccionar producto")
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(18.dp))
                        }

                        DropdownMenu(
                            expanded = expandedProducto,
                            onDismissRequest = { expandedProducto = false }
                        ) {
                            state.productos.forEach { producto ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(producto.codigo)
                                            Text(
                                                producto.nombre,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    onClick = {
                                        viewModel.onProductoSeleccionado(producto)
                                        expandedProducto = false
                                    }
                                )
                            }
                        }
                    }

                    state.errores[FormField.PRODUCTO]?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Nombre del Producto (read-only)
                OutlinedTextField(
                    value = state.productoSeleccionado?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nombre del Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                // Cantidad
                OutlinedTextField(
                    value = state.cantidadTexto,
                    onValueChange = viewModel::onCantidadChanged,
                    label = { Text("Cantidad*") },
                    placeholder = { Text("0") },
                    isError = state.errores.containsKey(FormField.CANTIDAD),
                    supportingText = {
                        state.errores[FormField.CANTIDAD]?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !state.isLoading
                )

                // Descripción (opcional)
                OutlinedTextField(
                    value = state.descripcion,
                    onValueChange = viewModel::onDescripcionChanged,
                    label = { Text("Descripción (Opcional)") },
                    placeholder = { Text("Ej: Reabastecimiento de stock") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    enabled = !state.isLoading
                )

                // Comprobante (opcional)
                Column {
                    Text(
                        "Comprobante (Opcional)",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (state.comprobanteUri == null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { pickImageLauncher.launch("image/*") },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                enabled = !state.isLoading
                            ) {
                                Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Galería", style = MaterialTheme.typography.labelSmall)
                            }

                            OutlinedButton(
                                onClick = {
                                    val uri = Uri.parse("file:///tmp/camera_image.jpg")
                                    takePictureLauncher.launch(uri)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                enabled = !state.isLoading
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Cámara", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "✓ Comprobante adjuntado",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "Fuente: ${state.comprobanteSource?.name ?: ""}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            TextButton(
                                onClick = viewModel::onQuitarComprobante
                            ) {
                                Text("Quitar", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }

                // Error global
                state.errorGlobal?.let {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                it,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            // Footer con botones fijos
            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    enabled = !state.isLoading
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = viewModel::onCrearMovimiento,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Crear")
                    }
                }
            }
        }
    }
}


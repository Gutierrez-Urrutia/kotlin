package cl.duoc.maestranza_v2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.maestranza_v2.data.model.MovementTypeFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementsFilterBottomSheet(
    typeFilter: MovementTypeFilter,
    onTypeFilterChange: (MovementTypeFilter) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtros de Movimientos",
                    style = MaterialTheme.typography.headlineSmall
                )
                TextButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Limpiar")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Filtro de tipo con segmented button
            Text(
                text = "Tipo de Movimiento",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    selected = typeFilter == MovementTypeFilter.All,
                    onClick = { onTypeFilterChange(MovementTypeFilter.All) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                ) {
                    Text("Todos")
                }
                SegmentedButton(
                    selected = typeFilter == MovementTypeFilter.Entrada,
                    onClick = { onTypeFilterChange(MovementTypeFilter.Entrada) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                ) {
                    Text("Entradas")
                }
                SegmentedButton(
                    selected = typeFilter == MovementTypeFilter.Salida,
                    onClick = { onTypeFilterChange(MovementTypeFilter.Salida) },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                ) {
                    Text("Salidas")
                }
            }

            Spacer(Modifier.height(24.dp))

            // Botón aplicar
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aplicar Filtros")
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}


package cl.duoc.maestranza_v2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

enum class StockFilter {
    All, InStock, LowStock, OutOfStock
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    categories: List<String>,
    selectedCategory: String?,
    stockFilter: StockFilter,
    onCategorySelected: (String?) -> Unit,
    onStockFilterSelected: (StockFilter) -> Unit,
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
                    text = "Filtros",
                    style = MaterialTheme.typography.headlineSmall
                )
                TextButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Limpiar")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Categorías
            Text(
                text = "Categoría",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Column(Modifier.selectableGroup()) {
                FilterRadioButton(
                    text = "Todas",
                    selected = selectedCategory == null,
                    onClick = { onCategorySelected(null) }
                )
                categories.forEach { category ->
                    FilterRadioButton(
                        text = category,
                        selected = selectedCategory == category,
                        onClick = { onCategorySelected(category) }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Filtro de Stock
            Text(
                text = "Stock",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Column(Modifier.selectableGroup()) {
                FilterRadioButton(
                    text = "Todos",
                    selected = stockFilter == StockFilter.All,
                    onClick = { onStockFilterSelected(StockFilter.All) }
                )
                FilterRadioButton(
                    text = "En Stock",
                    selected = stockFilter == StockFilter.InStock,
                    onClick = { onStockFilterSelected(StockFilter.InStock) }
                )
                FilterRadioButton(
                    text = "Stock Bajo",
                    selected = stockFilter == StockFilter.LowStock,
                    onClick = { onStockFilterSelected(StockFilter.LowStock) }
                )
                FilterRadioButton(
                    text = "Sin Stock",
                    selected = stockFilter == StockFilter.OutOfStock,
                    onClick = { onStockFilterSelected(StockFilter.OutOfStock) }
                )
            }

            Spacer(Modifier.height(16.dp))

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

@Composable
private fun FilterRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


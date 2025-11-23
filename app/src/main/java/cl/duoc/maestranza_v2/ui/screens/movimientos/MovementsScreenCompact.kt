package cl.duoc.maestranza_v2.ui.screens.movimientos

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.maestranza_v2.ui.components.DateRangePickerDialog
import cl.duoc.maestranza_v2.ui.components.KPICard
import cl.duoc.maestranza_v2.ui.components.MovementCard
import cl.duoc.maestranza_v2.ui.components.MovementDetailBottomSheet
import cl.duoc.maestranza_v2.ui.components.MovementsFilterBottomSheet
import cl.duoc.maestranza_v2.ui.theme.Maestranza_V2Theme
import cl.duoc.maestranza_v2.viewmodel.MainViewModel
import cl.duoc.maestranza_v2.viewmodel.MovementsViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementsScreenCompact(
    navController: NavController,
    viewModel: MainViewModel,
    movementsViewModel: MovementsViewModel = viewModel()
) {
    val uiState by movementsViewModel.uiState.collectAsState()
    var showFilters by remember { mutableStateOf(false) }
    var showDetailSheet by remember { mutableStateOf(false) }
    var selectedMovement by remember { mutableStateOf<cl.duoc.maestranza_v2.model.Movement?>(null) }

    // Estados para date pickers
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    cl.duoc.maestranza_v2.ui.components.ScaffoldWrapper(
        navController = navController,
        showDrawer = true,
        title = "Movimientos",
        actions = {
            IconButton(onClick = { showFilters = true }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtros")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Nuevo movimiento */ }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo movimiento")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // KPIs en fila que actúan como filtros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KPICard(
                    title = "Total",
                    value = uiState.kpiTotal,
                    icon = Icons.AutoMirrored.Filled.List,
                    modifier = Modifier.weight(1f),
                    isSelected = uiState.typeFilter == cl.duoc.maestranza_v2.model.MovementTypeFilter.All,
                    onClick = {
                        movementsViewModel.onTypeFilterChange(cl.duoc.maestranza_v2.model.MovementTypeFilter.All)
                    }
                )
                KPICard(
                    title = "Entradas",
                    value = uiState.kpiEntradas,
                    icon = Icons.Default.ArrowDownward,
                    containerColor = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.weight(1f),
                    isSelected = uiState.typeFilter == cl.duoc.maestranza_v2.model.MovementTypeFilter.Entrada,
                    onClick = {
                        movementsViewModel.onTypeFilterChange(cl.duoc.maestranza_v2.model.MovementTypeFilter.Entrada)
                    }
                )
                KPICard(
                    title = "Salidas",
                    value = uiState.kpiSalidas,
                    icon = Icons.Default.ArrowUpward,
                    containerColor = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.weight(1f),
                    isSelected = uiState.typeFilter == cl.duoc.maestranza_v2.model.MovementTypeFilter.Salida,
                    onClick = {
                        movementsViewModel.onTypeFilterChange(cl.duoc.maestranza_v2.model.MovementTypeFilter.Salida)
                    }
                )
            }

            // Selector de fechas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showStartPicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (uiState.startDate != null && uiState.endDate != null) {
                            "${uiState.startDate} → ${uiState.endDate}"
                        } else if (uiState.startDate != null) {
                            "${uiState.startDate}"
                        } else {
                            "Seleccionar fecha"
                        }
                    )
                }
                if (uiState.startDate != null || uiState.endDate != null) {
                    IconButton(
                        onClick = { movementsViewModel.onDateRangeSelected(null, null) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar fecha")
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Lista de movimientos
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.filteredMovements, key = { it.id }) { movement ->
                    MovementCard(
                        movement = movement,
                        onClick = {
                            selectedMovement = movement
                            showDetailSheet = true
                        },
                        onOpenReceipt = { /* TODO: Ver comprobante */ }
                    )
                }
            }
        }

        // Bottom sheet de filtros
        if (showFilters) {
            MovementsFilterBottomSheet(
                typeFilter = uiState.typeFilter,
                onTypeFilterChange = movementsViewModel::onTypeFilterChange,
                onClear = movementsViewModel::clearFilters,
                onDismiss = { showFilters = false }
            )
        }

        // Bottom sheet de detalle
        if (showDetailSheet && selectedMovement != null) {
            MovementDetailBottomSheet(
                movement = selectedMovement!!,
                onDismiss = {
                    showDetailSheet = false
                    selectedMovement = null
                }
            )
        }

        // Selector de fechas personalizado
        if (showStartPicker) {
            DateRangePickerDialog(
                title = "Fecha de Inicio",
                subtitle = "Selecciona la fecha desde donde quieres filtrar",
                currentDate = uiState.startDate,
                onDateSelected = { selectedDate ->
                    movementsViewModel.onDateRangeSelected(selectedDate, null)
                    showStartPicker = false
                    showEndPicker = true
                },
                onDismiss = { showStartPicker = false }
            )
        }

        if (showEndPicker) {
            DateRangePickerDialog(
                title = "Fecha de Término",
                subtitle = "Selecciona la fecha hasta donde quieres filtrar (o la misma para un solo día)",
                currentDate = uiState.endDate,
                onDateSelected = { selectedDate ->
                    movementsViewModel.onDateRangeSelected(uiState.startDate, selectedDate)
                    showEndPicker = false
                },
                onDismiss = {
                    showEndPicker = false
                    showStartPicker = true
                }
            )
        }
    }
}

@SuppressLint("ComposableNaming", "ViewModelConstructorInComposable")
@Preview(
    showBackground = true,
    name = "Compact",
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun MovementsScreenCompactPreview() {
    Maestranza_V2Theme {
        MovementsScreenCompact(
            navController = rememberNavController(),
            viewModel = MainViewModel()
        )
    }
}


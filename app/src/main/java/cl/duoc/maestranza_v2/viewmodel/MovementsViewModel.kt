package cl.duoc.maestranza_v2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.maestranza_v2.model.Movement
import cl.duoc.maestranza_v2.model.MovementType
import cl.duoc.maestranza_v2.model.MovementTypeFilter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

data class MovementsUiState(
    val movements: List<Movement> = emptyList(),
    val filteredMovements: List<Movement> = emptyList(),
    val query: String = "",
    val typeFilter: MovementTypeFilter = MovementTypeFilter.All,
    val kpiTotal: Int = 0,
    val kpiEntradas: Int = 0,
    val kpiSalidas: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)

@OptIn(FlowPreview::class)
class MovementsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MovementsUiState())
    val uiState: StateFlow<MovementsUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        loadMovements()

        // Búsqueda con debounce de 300ms
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .collectLatest { query ->
                    _uiState.update { it.copy(query = query) }
                    applyFilters()
                }
        }
    }

    private fun loadMovements() {
        // Datos hardcoded para demo
        val movements = listOf(
            Movement(
                id = "1",
                fecha = LocalDateTime.now().minusHours(2),
                usuario = "admin",
                productoId = "HCOR-001",
                productoCodigo = "HCOR-001",
                productoNombre = "Broca HSS 5mm",
                cantidad = 50,
                tipo = MovementType.ENTRADA,
                descripcion = "Reabastecimiento stock",
                comprobantePath = "https://via.placeholder.com/600x400/4CAF50/FFFFFF?text=Comprobante+Entrada"
            ),
            Movement(
                id = "2",
                fecha = LocalDateTime.now().minusHours(5),
                usuario = "compras1",
                productoId = "MAT-001",
                productoCodigo = "MAT-001",
                productoNombre = "Lámina Acero Inox 1mm",
                cantidad = 20,
                tipo = MovementType.SALIDA,
                descripcion = "Salida a proyecto Planta Norte"
            ),
            Movement(
                id = "3",
                fecha = LocalDateTime.now().minusDays(1),
                usuario = "admin",
                productoId = "EQ-001",
                productoCodigo = "EQ-001",
                productoNombre = "Soldadora MIG 200A",
                cantidad = 2,
                tipo = MovementType.ENTRADA,
                descripcion = "Compra nueva maquinaria",
                comprobantePath = "https://via.placeholder.com/600x400/2196F3/FFFFFF?text=Factura+Compra"
            ),
            Movement(
                id = "4",
                fecha = LocalDateTime.now().minusDays(1).minusHours(3),
                usuario = "ventas1",
                productoId = "CONS-001",
                productoCodigo = "CONS-001",
                productoNombre = "Disco Corte 4.5\"",
                cantidad = 100,
                tipo = MovementType.SALIDA,
                descripcion = "Venta cliente ABC",
                comprobantePath = "https://via.placeholder.com/600x400/F44336/FFFFFF?text=Guia+Despacho"
            ),
            Movement(
                id = "5",
                fecha = LocalDateTime.now().minusDays(2),
                usuario = "supervisor1",
                productoId = "HCOR-003",
                productoCodigo = "HCOR-003",
                productoNombre = "Sierra Circular 7\"",
                cantidad = 5,
                tipo = MovementType.ENTRADA,
                descripcion = "Devolución de bodega externa"
            ),
            Movement(
                id = "6",
                fecha = LocalDateTime.now().minusDays(2).minusHours(4),
                usuario = "compras2",
                productoId = "MAT-002",
                productoCodigo = "MAT-002",
                productoNombre = "Tubo Acero 2\" x 6m",
                cantidad = 15,
                tipo = MovementType.SALIDA,
                descripcion = "Transferencia a sucursal sur"
            ),
            Movement(
                id = "7",
                fecha = LocalDateTime.now().minusDays(3),
                usuario = "admin",
                productoId = "CONS-003",
                productoCodigo = "CONS-003",
                productoNombre = "Electrodo 6013 3.2mm",
                cantidad = 200,
                tipo = MovementType.ENTRADA,
                descripcion = "Compra proveedor principal"
            ),
            Movement(
                id = "8",
                fecha = LocalDateTime.now().minusDays(3).minusHours(6),
                usuario = "empleado1",
                productoId = "HCOR-004",
                productoCodigo = "HCOR-004",
                productoNombre = "Juego Llaves Allen",
                cantidad = 3,
                tipo = MovementType.SALIDA,
                descripcion = "Asignación herramientas técnico"
            )
        )

        _uiState.update {
            it.copy(
                movements = movements,
                filteredMovements = movements
            )
        }
        calculateKPIs()
    }

    private fun calculateKPIs() {
        val state = _uiState.value
        val total = state.movements.size
        val entradas = state.movements.count { it.tipo == MovementType.ENTRADA }
        val salidas = state.movements.count { it.tipo == MovementType.SALIDA }

        _uiState.update {
            it.copy(
                kpiTotal = total,
                kpiEntradas = entradas,
                kpiSalidas = salidas
            )
        }
    }

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onTypeFilterChange(typeFilter: MovementTypeFilter) {
        _uiState.update { it.copy(typeFilter = typeFilter) }
        applyFilters()
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(
                query = "",
                typeFilter = MovementTypeFilter.All,
                startDate = null,
                endDate = null
            )
        }
        _searchQuery.value = ""
        applyFilters()
    }

    fun onDateRangeSelected(startDate: LocalDate?, endDate: LocalDate?) {
        _uiState.update { it.copy(startDate = startDate, endDate = endDate) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val filtered = state.movements.filter { movement ->
            // Filtro de búsqueda
            val matchesQuery = if (state.query.isBlank()) {
                true
            } else {
                movement.productoCodigo.contains(state.query, ignoreCase = true) ||
                movement.productoNombre.contains(state.query, ignoreCase = true) ||
                movement.usuario.contains(state.query, ignoreCase = true)
            }

            // Filtro de tipo
            val matchesType = when (state.typeFilter) {
                MovementTypeFilter.All -> true
                MovementTypeFilter.Entrada -> movement.tipo == MovementType.ENTRADA
                MovementTypeFilter.Salida -> movement.tipo == MovementType.SALIDA
            }

            // Filtro de fecha
            val matchesDate = if (state.startDate == null && state.endDate == null) {
                true
            } else {
                val mvDate = movement.fecha.toLocalDate()
                when {
                    state.startDate != null && state.endDate != null ->
                        !mvDate.isBefore(state.startDate) && !mvDate.isAfter(state.endDate)
                    state.startDate != null -> mvDate == state.startDate
                    state.endDate != null -> mvDate == state.endDate
                    else -> true
                }
            }

            matchesQuery && matchesType && matchesDate
        }

        _uiState.update { it.copy(filteredMovements = filtered) }
    }

    fun addMovement(movement: Movement) {
        _uiState.update { state ->
            val updatedMovements = listOf(movement) + state.movements
            state.copy(movements = updatedMovements)
        }
        calculateKPIs()
        applyFilters()
    }

    fun getMovementById(id: String): Movement? {
        return _uiState.value.movements.find { it.id == id }
    }
}


package cl.duoc.maestranza_v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.maestranza_v2.data.model.Movement
import cl.duoc.maestranza_v2.data.model.MovementType
import cl.duoc.maestranza_v2.data.model.MovementTypeFilter
import cl.duoc.maestranza_v2.data.model.toMovement
import cl.duoc.maestranza_v2.data.remote.ApiClient
import cl.duoc.maestranza_v2.data.repository.MovementsRepository
import cl.duoc.maestranza_v2.data.repository.Result
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
class MovementsViewModel(context: Context? = null) : ViewModel() {

    private val _uiState = MutableStateFlow(MovementsUiState())
    val uiState: StateFlow<MovementsUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    private val movementsRepository: MovementsRepository? = context?.let {
        MovementsRepository(ApiClient(it))
    }

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
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            if (movementsRepository != null) {
                // Cargar movimientos desde API
                val result = movementsRepository.getMovimientos()
                when (result) {
                    is Result.Success -> {
                        val movements = result.data.map { it.toMovement() }.sortedByDescending { it.fecha }
                        Log.d("MovementsViewModel", "Movimientos cargados: ${movements.size} items")
                        _uiState.update {
                            it.copy(
                                movements = movements,
                                filteredMovements = movements,
                                isLoading = false,
                                error = null
                            )
                        }
                        calculateKPIs()
                    }
                    is Result.Error -> {
                        Log.e("MovementsViewModel", "Error al cargar movimientos: ${result.exception.message}")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Error desconocido"
                            )
                        }
                    }
                    is Result.Loading -> {}
                }
            } else {
                Log.w("MovementsViewModel", "Repositorio no disponible, usando datos de demostración")
                // Fallback: datos de demostración
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
                        descripcion = "Reabastecimiento stock"
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
                    )
                )

                _uiState.update {
                    it.copy(
                        movements = movements,
                        filteredMovements = movements,
                        isLoading = false
                    )
                }
                calculateKPIs()
            }
        }
    }

    fun retry() {
        loadMovements()
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

    fun refreshMovements() {
        // Recargar datos desde la API
        loadMovements()
    }

    fun getMovementById(id: String): Movement? {
        return _uiState.value.movements.find { it.id == id }
    }
}


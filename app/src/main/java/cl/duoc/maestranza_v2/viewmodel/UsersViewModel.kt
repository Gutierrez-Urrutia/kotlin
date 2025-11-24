package cl.duoc.maestranza_v2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.maestranza_v2.data.model.User
import cl.duoc.maestranza_v2.data.model.UserFilters
import cl.duoc.maestranza_v2.data.model.UserStatusFilter
import cl.duoc.maestranza_v2.data.model.toUser
import cl.duoc.maestranza_v2.data.remote.ApiClient
import cl.duoc.maestranza_v2.data.repository.Result
import cl.duoc.maestranza_v2.data.repository.UsersRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class UsersViewModel(context: Context? = null) : ViewModel() {

    data class UsersUiState(
        val users: List<User> = emptyList(),
        val filteredUsers: List<User> = emptyList(),
        val query: String = "",
        val filters: UserFilters = UserFilters(),
        val availableRoles: List<String> = listOf(
            "ROLE_ADMINISTRADOR",
            "ROLE_AUDITOR",
            "ROLE_COMPRAS",
            "ROLE_VENTAS",
            "ROLE_SUPERVISOR",
            "ROLE_EMPLEADO"
        ),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    private val usersRepository: UsersRepository? = context?.let {
        UsersRepository(ApiClient(it))
    }

    init {
        loadUsers()

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

    private fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            if (usersRepository != null) {
                // Cargar desde API
                val result = usersRepository.getUsuarios()
                when (result) {
                    is Result.Success -> {
                        val users = result.data.map { it.toUser() }
                        _uiState.update {
                            it.copy(
                                users = users,
                                filteredUsers = users,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Error desconocido"
                            )
                        }
                    }
                    is Result.Loading -> {
                        // Ya está en loading
                    }
                }
            } else {
                // Fallback: datos hardcodeados si no hay contexto
                val users = listOf(
                    User(
                        id = "1",
                        username = "admin",
                        nombre = "Juan",
                        apellido = "Pérez",
                        email = "juan.perez@maestranza.cl",
                        activo = true,
                        roles = listOf("ROLE_ADMINISTRADOR")
                    ),
                    User(
                        id = "2",
                        username = "auditor1",
                        nombre = "María",
                        apellido = "González",
                        email = "maria.gonzalez@maestranza.cl",
                        activo = true,
                        roles = listOf("ROLE_AUDITOR")
                    ),
                    User(
                        id = "3",
                        username = "compras1",
                        nombre = "Carlos",
                        apellido = "Rodríguez",
                        email = "carlos.rodriguez@maestranza.cl",
                        activo = true,
                        roles = listOf("ROLE_COMPRAS")
                    ),
                    User(
                        id = "4",
                        username = "ventas1",
                        nombre = "Ana",
                        apellido = "Martínez",
                        email = "ana.martinez@maestranza.cl",
                        activo = false,
                        roles = listOf("ROLE_VENTAS")
                    ),
                    User(
                        id = "5",
                        username = "supervisor1",
                        nombre = "Luis",
                        apellido = "Fernández",
                        email = "luis.fernandez@maestranza.cl",
                        activo = true,
                        roles = listOf("ROLE_SUPERVISOR", "ROLE_EMPLEADO")
                    ),
                    User(
                        id = "6",
                        username = "empleado1",
                        nombre = "Patricia",
                        apellido = "López",
                        email = "patricia.lopez@maestranza.cl",
                        activo = true,
                        roles = listOf("ROLE_EMPLEADO")
                    ),
                    User(
                        id = "7",
                        username = "empleado2",
                        nombre = "Roberto",
                        apellido = "Silva",
                        email = "roberto.silva@maestranza.cl",
                        activo = false,
                        roles = listOf("ROLE_EMPLEADO")
                    ),
                    User(
                        id = "8",
                        username = "compras2",
                        nombre = "Sofía",
                        apellido = "Torres",
                        email = "sofia.torres@maestranza.cl",
                        activo = true,
                        roles = listOf("ROLE_COMPRAS", "ROLE_VENTAS")
                    )
                )

                _uiState.update {
                    it.copy(
                        users = users,
                        filteredUsers = users,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun retry() {
        loadUsers()
    }

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onRoleFilterChange(role: String) {
        val currentRoles = _uiState.value.filters.selectedRoles.toMutableSet()
        if (currentRoles.contains(role)) {
            currentRoles.remove(role)
        } else {
            currentRoles.add(role)
        }

        _uiState.update {
            it.copy(
                filters = it.filters.copy(selectedRoles = currentRoles)
            )
        }
        applyFilters()
    }

    fun onStatusFilterChange(statusFilter: UserStatusFilter) {
        _uiState.update {
            it.copy(
                filters = it.filters.copy(statusFilter = statusFilter)
            )
        }
        applyFilters()
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(
                query = "",
                filters = UserFilters()
            )
        }
        _searchQuery.value = ""
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val filtered = state.users.filter { user ->
            // Filtro de búsqueda
            val matchesQuery = if (state.query.isBlank()) {
                true
            } else {
                user.username.contains(state.query, ignoreCase = true) ||
                user.nombreCompleto.contains(state.query, ignoreCase = true) ||
                user.email.contains(state.query, ignoreCase = true)
            }

            // Filtro de roles
            val matchesRoles = if (state.filters.selectedRoles.isEmpty()) {
                true
            } else {
                user.roles.any { it in state.filters.selectedRoles }
            }

            // Filtro de estado
            val matchesStatus = when (state.filters.statusFilter) {
                UserStatusFilter.All -> true
                UserStatusFilter.Active -> user.activo
                UserStatusFilter.Inactive -> !user.activo
            }

            matchesQuery && matchesRoles && matchesStatus
        }

        _uiState.update { it.copy(filteredUsers = filtered) }
    }

    fun toggleUserActive(userId: String) {
        _uiState.update { state ->
            val updatedUsers = state.users.map { user ->
                if (user.id == userId) {
                    user.copy(activo = !user.activo)
                } else {
                    user
                }
            }
            state.copy(users = updatedUsers)
        }
        applyFilters()
    }

    fun deleteUser(userId: String) {
        _uiState.update { state ->
            val updatedUsers = state.users.filter { it.id != userId }
            state.copy(users = updatedUsers)
        }
        applyFilters()
    }

    fun addUser(user: User) {
        _uiState.update { state ->
            val updatedUsers = state.users + user
            state.copy(users = updatedUsers)
        }
        applyFilters()
    }

    fun updateUser(updatedUser: User) {
        _uiState.update { state ->
            val updatedUsers = state.users.map { user ->
                if (user.id == updatedUser.id) updatedUser else user
            }
            state.copy(users = updatedUsers)
        }
        applyFilters()
    }

    fun getUserById(id: String): User? {
        return _uiState.value.users.find { it.id == id }
    }
}



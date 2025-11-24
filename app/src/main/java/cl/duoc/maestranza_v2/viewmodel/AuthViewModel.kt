package cl.duoc.maestranza_v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.maestranza_v2.data.remote.ApiClient
import cl.duoc.maestranza_v2.data.repository.AuthRepository
import cl.duoc.maestranza_v2.data.repository.Result
import cl.duoc.maestranza_v2.data.model.JwtResponse
import cl.duoc.maestranza_v2.data.local.UserInfo
import cl.duoc.maestranza_v2.data.local.CredentialsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val username: String = "",
    val error: String? = null,
    val token: String? = null,
    val userInfo: UserInfo? = null,
    val successMessage: String? = null
)

class AuthViewModel(context: Context) : ViewModel() {
    private val apiClient = ApiClient(context)
    private val authRepository = AuthRepository(apiClient)
    private val credentialsManager = CredentialsManager(context)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        // Verificar si ya hay un token guardado
        checkExistingToken()
    }

    /**
     * Verificar si existe un token guardado
     */
    private fun checkExistingToken() {
        viewModelScope.launch {
            authRepository.getTokenFlow().collect { token ->
                if (token != null) {
                    _uiState.update { it.copy(token = token, isAuthenticated = true) }
                    // Obtener información del usuario
                    authRepository.getUserInfoFlow().collect { userInfo ->
                        _uiState.update { it.copy(userInfo = userInfo) }
                    }
                } else {
                    // Si el token es nulo, resetear el estado de autenticación
                    _uiState.update { it.copy(token = null, isAuthenticated = false, userInfo = null) }
                }
            }
        }
    }

    /**
     * Realizar login
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = authRepository.login(username, password)

            when (result) {
                is Result.Success -> {
                    val jwtResponse = result.data
                    Log.d("AuthViewModel", ">>> LOGIN EXITOSO - Intentando guardar credenciales para: $username")
                    // Guardar credenciales de forma segura para login biométrico
                    // Esperar a que se complete el guardado
                    try {
                        Log.d("AuthViewModel", ">>> Llamando saveCredentials()...")
                        credentialsManager.saveCredentials(username, password)
                        Log.d("AuthViewModel", ">>> ✓ saveCredentials() completado")
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", ">>> ✗ Error guardando credenciales: ${e.message}", e)
                        e.printStackTrace()
                    }

                    Log.d("AuthViewModel", ">>> Actualizando UI state a isAuthenticated=true")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            token = jwtResponse.token,
                            username = jwtResponse.username,
                            userInfo = UserInfo(
                                userId = jwtResponse.id.toString(),
                                username = jwtResponse.username,
                                email = jwtResponse.email,
                                nombre = jwtResponse.nombre,
                                apellido = jwtResponse.apellido
                            ),
                            successMessage = "Bienvenido ${jwtResponse.nombre}"
                        )
                    }
                }

                is Result.Error -> {
                    // Crear un mensaje más amigable basado en el error
                    val friendlyMessage = when {
                        result.exception.message?.contains("401", ignoreCase = true) == true ->
                            "Nombre de usuario y/o contraseña incorrecta"
                        result.exception.message?.contains("403", ignoreCase = true) == true ->
                            "Usuario inactivo o sin permisos"
                        result.exception.message?.contains("400", ignoreCase = true) == true ->
                            "Datos inválidos. Por favor verifica tu usuario y contraseña"
                        result.exception.message?.contains("connection", ignoreCase = true) == true ||
                        result.exception.message?.contains("timeout", ignoreCase = true) == true ||
                        result.exception.message?.contains("network", ignoreCase = true) == true ->
                            "No se pudo conectar al servidor. Verifica tu conexión"
                        else -> result.exception.message ?: "Error de autenticación"
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = friendlyMessage
                        )
                    }
                }

                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    /**
     * Realizar signup (registro)
     */
    fun signup(
        username: String,
        email: String,
        password: String,
        nombre: String,
        apellido: String,
        roles: List<String>
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = authRepository.signup(
                username, email, password, nombre, apellido, roles
            )

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = result.data
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Error en registro"
                        )
                    }
                }

                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    /**
     * Realizar logout
     */
    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.logout()

            when (result) {
                is Result.Success -> {
                    // Limpiar credenciales guardadas
                    credentialsManager.clearCredentials()

                    _uiState.update {
                        AuthUiState(
                            isLoading = false,
                            isAuthenticated = false,
                            successMessage = "Sesión cerrada correctamente"
                        )
                    }
                }

                is Result.Error -> {
                    // Aunque falle la llamada al servidor, limpiar la sesión localmente
                    credentialsManager.clearCredentials()

                    _uiState.update {
                        AuthUiState(
                            isLoading = false,
                            isAuthenticated = false,
                            successMessage = "Sesión cerrada"
                        )
                    }
                }

                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    /**
     * Verificar si el token es válido
     */
    fun validateToken() {
        viewModelScope.launch {
            val result = authRepository.validateToken()

            when (result) {
                is Result.Success -> {
                    _uiState.update { it.copy(isAuthenticated = true) }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isAuthenticated = false,
                            error = "Token inválido"
                        )
                    }
                }

                is Result.Loading -> {}
            }
        }
    }

    /**
     * Limpiar mensajes de éxito
     */
    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    /**
     * Limpiar mensajes de error
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Login con biometría (huella digital)
     */
}


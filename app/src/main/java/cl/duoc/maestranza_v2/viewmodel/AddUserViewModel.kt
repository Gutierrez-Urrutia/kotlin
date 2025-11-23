package cl.duoc.maestranza_v2.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.maestranza_v2.data.model.AddUserErrors
import cl.duoc.maestranza_v2.data.model.AddUserState
import cl.duoc.maestranza_v2.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddUserViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddUserState())
    val state: StateFlow<AddUserState> = _state.asStateFlow()

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val usernameRegex = Regex("^[a-zA-Z0-9._-]{3,20}$")

    fun onNombreChange(nombre: String) {
        _state.update { it.copy(nombre = nombre) }
    }

    fun onApellidoChange(apellido: String) {
        _state.update { it.copy(apellido = apellido) }
    }

    fun onUsernameChange(username: String) {
        // Solo permitir caracteres válidos sin espacios
        if (username.isEmpty() || username.matches(Regex("^[a-zA-Z0-9._-]*$"))) {
            _state.update { it.copy(username = username.lowercase()) }
        }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email.trim()) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun togglePasswordVisibility() {
        _state.update { it.copy(showPassword = !it.showPassword) }
    }

    fun toggleConfirmPasswordVisibility() {
        _state.update { it.copy(showConfirmPassword = !it.showConfirmPassword) }
    }

    fun onRoleToggle(role: String) {
        val currentRoles = _state.value.selectedRoles.toMutableSet()
        if (currentRoles.contains(role)) {
            currentRoles.remove(role)
        } else {
            currentRoles.add(role)
        }
        _state.update { it.copy(selectedRoles = currentRoles) }
    }

    fun resetForm() {
        _state.value = AddUserState()
    }

    fun validateForm(existingUsers: List<User>): Boolean {
        val currentState = _state.value
        val errors = AddUserErrors(
            nombre = when {
                currentState.nombre.isBlank() -> "El nombre es obligatorio"
                currentState.nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
                else -> null
            },
            apellido = when {
                currentState.apellido.isBlank() -> "El apellido es obligatorio"
                currentState.apellido.length < 2 -> "El apellido debe tener al menos 2 caracteres"
                else -> null
            },
            username = when {
                currentState.username.isBlank() -> "El nombre de usuario es obligatorio"
                !usernameRegex.matches(currentState.username) ->
                    "Usuario debe tener 3-20 caracteres (letras, números, . _ -)"
                existingUsers.any { it.username.equals(currentState.username, ignoreCase = true) } ->
                    "Este nombre de usuario ya existe"
                else -> null
            },
            email = when {
                currentState.email.isBlank() -> "El correo electrónico es obligatorio"
                !emailRegex.matches(currentState.email) -> "Formato de correo inválido"
                existingUsers.any { it.email.equals(currentState.email, ignoreCase = true) } ->
                    "Este correo ya está registrado"
                else -> null
            },
            password = when {
                currentState.password.isBlank() -> "La contraseña es obligatoria"
                currentState.password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                else -> null
            },
            confirmPassword = when {
                currentState.confirmPassword.isBlank() -> "Debe confirmar la contraseña"
                currentState.confirmPassword != currentState.password -> "Las contraseñas no coinciden"
                else -> null
            },
            roles = when {
                currentState.selectedRoles.isEmpty() -> "Debe seleccionar al menos un rol"
                else -> null
            }
        )

        _state.update { it.copy(errors = errors) }

        return errors.nombre == null && errors.apellido == null &&
               errors.username == null && errors.email == null &&
               errors.password == null && errors.confirmPassword == null &&
               errors.roles == null
    }

    fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }
}


package cl.duoc.maestranza_v2.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.maestranza_v2.model.AddUserErrors
import cl.duoc.maestranza_v2.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EditUserState(
    val originalUser: User? = null,
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val username: String = "",
    val selectedRoles: Set<String> = emptySet(),
    val activo: Boolean = true,
    val isLoading: Boolean = false,
    val isLoadingUser: Boolean = false,
    val error: String? = null,
    val errors: AddUserErrors = AddUserErrors()
) {
    val hasChanges: Boolean
        get() = originalUser?.let {
            nombre != it.nombre ||
            apellido != it.apellido ||
            email != it.email ||
            selectedRoles != it.roles.toSet()
        } ?: false
}

class EditUserViewModel : ViewModel() {

    private val _state = MutableStateFlow(EditUserState())
    val state: StateFlow<EditUserState> = _state.asStateFlow()

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun loadUser(user: User) {
        _state.update {
            EditUserState(
                originalUser = user,
                nombre = user.nombre,
                apellido = user.apellido,
                email = user.email,
                username = user.username,
                selectedRoles = user.roles.toSet(),
                activo = user.activo,
                isLoadingUser = false
            )
        }
    }

    fun onNombreChange(nombre: String) {
        _state.update { it.copy(nombre = nombre) }
    }

    fun onApellidoChange(apellido: String) {
        _state.update { it.copy(apellido = apellido) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email.trim()) }
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

    fun validateForm(existingUsers: List<User>): Boolean {
        val currentState = _state.value
        val originalUserId = currentState.originalUser?.id

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
            email = when {
                currentState.email.isBlank() -> "El correo electrónico es obligatorio"
                !emailRegex.matches(currentState.email) -> "Formato de correo inválido"
                existingUsers.any {
                    it.id != originalUserId &&
                    it.email.equals(currentState.email, ignoreCase = true)
                } -> "Este correo ya está registrado"
                else -> null
            },
            roles = when {
                currentState.selectedRoles.isEmpty() -> "Debe seleccionar al menos un rol"
                else -> null
            }
        )

        _state.update { it.copy(errors = errors) }

        return errors.nombre == null && errors.apellido == null &&
               errors.email == null && errors.roles == null
    }

    fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    fun setError(error: String?) {
        _state.update { it.copy(error = error) }
    }
}


package cl.duoc.maestranza_v2.model

data class AddUserErrors(
    val nombre: String? = null,
    val apellido: String? = null,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val roles: String? = null
)

data class AddUserState(
    val nombre: String = "",
    val apellido: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val selectedRoles: Set<String> = emptySet(),
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val isLoading: Boolean = false,
    val errors: AddUserErrors = AddUserErrors()
) {
    val hasChanges: Boolean
        get() = nombre.isNotBlank() || apellido.isNotBlank() || username.isNotBlank() ||
                email.isNotBlank() || password.isNotBlank() || confirmPassword.isNotBlank() ||
                selectedRoles.isNotEmpty()
}


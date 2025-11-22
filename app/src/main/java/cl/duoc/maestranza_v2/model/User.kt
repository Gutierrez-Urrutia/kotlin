package cl.duoc.maestranza_v2.model

data class User(
    val id: String,
    val username: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val activo: Boolean,
    val roles: List<String>
) {
    val nombreCompleto: String
        get() = "$nombre $apellido"

    fun getRolesFormatted(): List<String> {
        return roles.map { role ->
            when (role) {
                "ROLE_ADMINISTRADOR" -> "Administrador"
                "ROLE_AUDITOR" -> "Auditor"
                "ROLE_COMPRAS" -> "Compras"
                "ROLE_VENTAS" -> "Ventas"
                "ROLE_SUPERVISOR" -> "Supervisor"
                "ROLE_EMPLEADO" -> "Empleado"
                else -> role.removePrefix("ROLE_").lowercase()
                    .replaceFirstChar { it.uppercase() }
            }
        }
    }
}

enum class UserStatusFilter {
    All, Active, Inactive
}

data class UserFilters(
    val selectedRoles: Set<String> = emptySet(),
    val statusFilter: UserStatusFilter = UserStatusFilter.All
)


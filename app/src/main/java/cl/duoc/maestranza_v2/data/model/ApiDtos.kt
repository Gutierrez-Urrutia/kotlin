package cl.duoc.maestranza_v2.data.model

import com.google.gson.annotations.SerializedName

// ==================== AUTH DTOs ====================

data class LoginRequest(
    val username: String,
    val password: String
)

data class JwtResponse(
    val token: String,
    val type: String,
    val id: Long,
    val username: String,
    val email: String,
    val nombre: String,
    val apellido: String,
    val roles: List<String>,
    val activo: Boolean
)

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val nombre: String,
    val apellido: String,
    val roles: List<String>
)

data class MessageResponse(
    val message: String
)

// ==================== PRODUCTO DTOs ====================

data class ProductoDTO(
    val id: Long,
    val codigo: String,
    val nombre: String,
    val descripcion: String? = null,
    val stock: Int,
    val imageUrl: String? = null,
    val fechaIngreso: String? = null,
    val ubicacion: String? = null,
    val activo: Boolean = true,
    val umbralStock: Int? = null,
    val categoriaId: Long? = null,
    val categoria: CategoriaDTO? = null,
    val precio: Double? = null,
    val precioActual: Double? = null
)

// ==================== CATEGORIA DTOs ====================

data class CategoriaDTO(
    val id: Long,
    val nombre: String,
    val descripcion: String? = null
)

// ==================== MOVIMIENTO DTOs ====================

data class MovimientoDTO(
    val id: Long,
    val fecha: String,
    val usuarioId: Long,
    val usuario: UsuarioDTO? = null,
    val productoId: Long,
    val producto: ProductoDTO? = null,
    val cantidad: Int,
    val tipo: String, // "ENTRADA" o "SALIDA"
    val descripcion: String? = null,
    val productoCodigo: String? = null,
    val productoNombre: String? = null,
    val imagePath: String? = null
)

data class CrearMovimientoRequest(
    val productoId: Long,
    val cantidad: Int,
    val tipo: String, // "ENTRADA" o "SALIDA"
    val descripcion: String? = null
)

// ==================== USUARIO DTOs ====================

data class UsuarioDTO(
    val id: Long,
    val username: String,
    val email: String,
    val nombre: String,
    val apellido: String,
    val activo: Boolean,
    val roles: List<String>
)

data class ActualizarUsuarioDTO(
    val email: String? = null,
    val nombre: String? = null,
    val apellido: String? = null,
    val roles: List<String>? = null
)

// ==================== ROL DTOs ====================

data class RolDTO(
    val id: Int,
    val nombre: String // "ROLE_ADMINISTRADOR", etc.
)

// ==================== ALERTA DTOs ====================

data class AlertaDTO(
    val id: Long,
    val nombre: String,
    val descripcion: String? = null,
    val fecha: String,
    val activo: Boolean,
    val productoId: Long? = null,
    val productoCodigo: String? = null,
    val productoNombre: String? = null,
    val productoStock: Int? = null,
    val productoUmbralStock: Int? = null,
    val productoUbicacion: String? = null,
    val categoriaNombre: String? = null,
    val tiempoTranscurrido: String? = null,
    val nivelUrgencia: String? = null,
    val resumenCorto: String? = null
)

// ==================== HISTORIAL PRECIOS DTOs ====================

data class HistorialPreciosDTO(
    val id: Long,
    val precio: Double,
    val fechaCreacion: String,
    val productoId: Long
)


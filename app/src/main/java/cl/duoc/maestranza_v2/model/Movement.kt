package cl.duoc.maestranza_v2.model

import java.time.LocalDateTime

enum class MovementType {
    ENTRADA, SALIDA
}

data class Movement(
    val id: String,
    val fecha: LocalDateTime,
    val usuario: String, // username o nombre del usuario
    val productoId: String,
    val productoCodigo: String,
    val productoNombre: String,
    val productoImagePath: String? = null,
    val cantidad: Int,
    val tipo: MovementType,
    val descripcion: String? = null,
    val comprobantePath: String? = null
)

enum class MovementTypeFilter {
    All, Entrada, Salida
}


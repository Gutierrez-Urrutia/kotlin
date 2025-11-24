package cl.duoc.maestranza_v2.data.model

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

/**
 * Extensión para mapear MovimientoDTO a Movement
 */
fun MovimientoDTO.toMovement(): Movement {
    return Movement(
        id = this.id.toString(),
        fecha = java.time.LocalDateTime.parse(this.fecha),
        usuario = this.usuario?.username ?: "Sistema",
        productoId = this.productoId.toString(),
        productoCodigo = this.productoCodigo ?: "N/A",
        productoNombre = this.productoNombre ?: "Producto desconocido",
        productoImagePath = this.imagePath,
        cantidad = this.cantidad,
        tipo = when (this.tipo.uppercase()) {
            "ENTRADA" -> MovementType.ENTRADA
            "SALIDA" -> MovementType.SALIDA
            else -> MovementType.ENTRADA
        },
        descripcion = this.descripcion,
        comprobantePath = this.imagePath
    )
}


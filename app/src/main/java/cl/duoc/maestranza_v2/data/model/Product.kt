package cl.duoc.maestranza_v2.data.model

// InventoryItem - Modelo de dominio para productos
data class InventoryItem(
    val code: String,
    val name: String,
    val category: String,
    val description: String = "",
    val price: Double = 0.0,
    val stock: Int
)

// SimpleProducto - Modelo simplificado para formularios
data class SimpleProducto(
    val id: String,
    val codigo: String,
    val nombre: String,
    val stock: Int
)


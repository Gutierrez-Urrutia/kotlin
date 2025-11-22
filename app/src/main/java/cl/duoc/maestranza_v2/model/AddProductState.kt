package cl.duoc.maestranza_v2.model

data class AddProductErrors(
    val codigo: String? = null,
    val nombre: String? = null,
    val categoria: String? = null,
    val descripcion: String? = null,
    val precio: String? = null,
    val stock: String? = null
)

data class AddProductState(
    val codigo: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val stock: String = "0",
    val errores: AddProductErrors = AddProductErrors()
)
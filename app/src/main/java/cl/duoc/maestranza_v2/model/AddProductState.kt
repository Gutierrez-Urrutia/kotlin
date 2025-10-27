package cl.duoc.maestranza_v2.model

data class AddProductErrors(
    val codigo: String? = null,
    val nombre: String? = null,
    val categoria: String? = null
)

data class AddProductState(
    val codigo: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val errores: AddProductErrors = AddProductErrors()
)
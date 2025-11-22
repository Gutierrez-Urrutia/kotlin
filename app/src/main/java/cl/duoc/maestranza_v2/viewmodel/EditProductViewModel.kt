package cl.duoc.maestranza_v2.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.maestranza_v2.model.AddProductErrors
import cl.duoc.maestranza_v2.model.AddProductState
import cl.duoc.maestranza_v2.ui.screens.inventory.InventoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditProductViewModel : ViewModel() {

    private val _estado = MutableStateFlow(AddProductState())
    val estado: StateFlow<AddProductState> = _estado.asStateFlow()

    private val formatoCodigoRegex = Regex("^[A-Z]{4}-\\d{3}$")

    fun loadProduct(product: InventoryItem) {
        _estado.value = AddProductState(
            codigo = product.code,
            nombre = product.name,
            categoria = product.category,
            descripcion = product.description,
            precio = product.price.toString(),
            stock = product.stock.toString()
        )
    }

    fun onCodigoChange(codigo: String) {
        _estado.update { it.copy(codigo = codigo.uppercase()) }
    }

    fun onNombreChange(nombre: String) {
        _estado.update { it.copy(nombre = nombre) }
    }

    fun onCategoriaChange(categoria: String) {
        _estado.update { it.copy(categoria = categoria) }
    }

    fun onDescripcionChange(descripcion: String) {
        _estado.update { it.copy(descripcion = descripcion) }
    }

    fun onPrecioChange(precio: String) {
        // Permitir números y punto decimal
        if (precio.isEmpty() || precio.matches(Regex("^\\d*\\.?\\d*$"))) {
            _estado.update { it.copy(precio = precio) }
        }
    }

    fun onStockChange(stock: String) {
        // Solo permitir números
        if (stock.isEmpty() || stock.all { it.isDigit() }) {
            _estado.update { it.copy(stock = stock) }
        }
    }

    fun validarFormulario(productosExistentes: List<InventoryItem>, originalCode: String): Boolean {
        val estadoActual = _estado.value
        val errores = AddProductErrors(
            codigo = if (estadoActual.codigo.isBlank()) {
                "El código es obligatorio"
            } else if (!formatoCodigoRegex.matches(estadoActual.codigo)) {
                "El formato debe ser AAAA-123"
            } else if (estadoActual.codigo != originalCode &&
                       productosExistentes.any { it.code == estadoActual.codigo }) {
                "El código ya existe"
            } else {
                null
            },
            nombre = if (estadoActual.nombre.isBlank()) "El nombre es obligatorio" else null,
            categoria = if (estadoActual.categoria.isBlank()) "La categoría es obligatoria" else null,
            descripcion = null,
            precio = if (estadoActual.precio.isBlank()) {
                "El precio es obligatorio"
            } else if (estadoActual.precio.toDoubleOrNull() == null) {
                "El precio debe ser un número válido"
            } else if (estadoActual.precio.toDouble() < 0) {
                "El precio no puede ser negativo"
            } else {
                null
            },
            stock = if (estadoActual.stock.isBlank()) {
                "El stock es obligatorio"
            } else if (estadoActual.stock.toIntOrNull() == null) {
                "El stock debe ser un número válido"
            } else if (estadoActual.stock.toInt() < 0) {
                "El stock no puede ser negativo"
            } else {
                null
            }
        )

        _estado.update { it.copy(errores = errores) }

        return errores.codigo == null && errores.nombre == null && errores.categoria == null &&
               errores.precio == null && errores.stock == null
    }
}


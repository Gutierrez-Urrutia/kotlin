package cl.duoc.maestranza_v2.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.maestranza_v2.model.AddProductErrors
import cl.duoc.maestranza_v2.model.AddProductState
import cl.duoc.maestranza_v2.ui.screens.inventory.InventoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddProductViewModel : ViewModel() {

    private val _estado = MutableStateFlow(AddProductState())
    val estado: StateFlow<AddProductState> = _estado.asStateFlow()

    private val formatoCodigoRegex = Regex("^[A-Z]{4}-\\d{3}$")

    fun onCodigoChange(codigo: String) {
        _estado.update { it.copy(codigo = codigo.uppercase()) }
    }

    fun onNombreChange(nombre: String) {
        _estado.update { it.copy(nombre = nombre) }
    }

    fun onCategoriaChange(categoria: String) {
        _estado.update { it.copy(categoria = categoria) }
    }

    fun validarFormulario(productosExistentes : List<InventoryItem>): Boolean {
        val estadoActual = _estado.value
        val errores = AddProductErrors(
            codigo = if (estadoActual.codigo.isBlank()) {
                "El código es obligatorio"
            } else if (!formatoCodigoRegex.matches(estadoActual.codigo)) {
                "El formato debe ser AAAA-123"
            } else if (productosExistentes.any { it.code == estadoActual.codigo }){
                "El código ya existe"
            } else {
                null
            },
            nombre = if (estadoActual.nombre.isBlank()) "El nombre es obligatorio" else null,
            categoria = if (estadoActual.categoria.isBlank()) "La categoría es obligatoria" else null
        )

        _estado.update { it.copy(errores = errores) }

        return errores.codigo == null && errores.nombre == null && errores.categoria == null
    }
}
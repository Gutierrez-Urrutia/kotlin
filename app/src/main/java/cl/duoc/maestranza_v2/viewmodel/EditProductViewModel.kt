package cl.duoc.maestranza_v2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.maestranza_v2.data.model.AddProductErrors
import cl.duoc.maestranza_v2.data.model.AddProductState
import cl.duoc.maestranza_v2.data.model.CategoriaDTO
import cl.duoc.maestranza_v2.data.model.InventoryItem
import cl.duoc.maestranza_v2.data.remote.ApiClient
import cl.duoc.maestranza_v2.data.repository.InventoryRepository
import cl.duoc.maestranza_v2.data.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProductViewModel(context: Context? = null) : ViewModel() {

    private val inventoryRepository = context?.let { InventoryRepository(ApiClient(it)) }

    private val _estado = MutableStateFlow(AddProductState())
    val estado: StateFlow<AddProductState> = _estado.asStateFlow()

    private val _categorias = MutableStateFlow<List<CategoriaDTO>>(emptyList())
    val categorias: StateFlow<List<CategoriaDTO>> = _categorias.asStateFlow()

    private val _categoriasLoading = MutableStateFlow(false)
    val categoriasLoading: StateFlow<Boolean> = _categoriasLoading.asStateFlow()

    private val formatoCodigoRegex = Regex("^[A-Z]{4}-\\d{3}$")

    init {
        loadCategorias()
    }

    fun loadCategorias() {
        if (inventoryRepository == null) return

        viewModelScope.launch {
            _categoriasLoading.update { true }
            val result = inventoryRepository.getCategorias()
            when (result) {
                is Result.Success -> {
                    _categorias.update { result.data }
                }
                is Result.Error -> {
                    // Error al cargar categorías, pero continuamos
                }
                is Result.Loading -> {}
            }
            _categoriasLoading.update { false }
        }
    }

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


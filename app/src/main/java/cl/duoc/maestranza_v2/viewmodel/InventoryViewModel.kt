package cl.duoc.maestranza_v2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.maestranza_v2.data.model.InventoryItem
import cl.duoc.maestranza_v2.data.model.ProductoDTO
import cl.duoc.maestranza_v2.data.remote.ApiClient
import cl.duoc.maestranza_v2.data.repository.InventoryRepository
import cl.duoc.maestranza_v2.data.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estados posibles para la carga de inventario
 */
sealed class InventoryState {
    object Loading : InventoryState()
    data class Success(val items: List<InventoryItem>) : InventoryState()
    data class Error(val message: String) : InventoryState()
}

/**
 * Estados posibles para operaciones (crear, actualizar, eliminar)
 */
sealed class OperationState {
    object Idle : OperationState()
    object Loading : OperationState()
    data class Success(val message: String) : OperationState()
    data class Error(val message: String) : OperationState()
}

/**
 * ViewModel especializado para la gestión del inventario
 * Maneja la carga de productos desde la API y el estado de UI
 */
class InventoryViewModel(context: Context? = null) : ViewModel() {

    private val inventoryRepository = context?.let { InventoryRepository(ApiClient(it)) }

    private val _inventoryState = MutableStateFlow<InventoryState>(InventoryState.Loading)
    val inventoryState = _inventoryState.asStateFlow()

    private val _allInventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())
    val allInventoryItems = _allInventoryItems.asStateFlow()

    // Cache de DTOs para obtener IDs
    private val _productoDTOs = MutableStateFlow<Map<String, ProductoDTO>>(emptyMap())
    val productoDTOs = _productoDTOs.asStateFlow()

    // Estado de operaciones (crear, actualizar, eliminar)
    private val _operationState = MutableStateFlow<OperationState>(OperationState.Idle)
    val operationState = _operationState.asStateFlow()

    companion object {
        private const val TAG = "InventoryViewModel"
    }

    /**
     * Cargar inventario desde la API
     */
    fun loadInventory() {
        if (inventoryRepository == null) {
            _inventoryState.update { InventoryState.Error("Repositorio no disponible") }
            return
        }

        viewModelScope.launch {
            _inventoryState.update { InventoryState.Loading }

            val result = inventoryRepository.getProductos()
            when (result) {
                is Result.Success -> {
                    // Cachear DTOs y convertir a InventoryItem
                    val dtoMap = result.data.associateBy { it.codigo }
                    _productoDTOs.update { dtoMap }

                    val items = result.data.map { dto ->
                        InventoryItem(
                            code = dto.codigo,
                            name = dto.nombre,
                            category = dto.categoria?.nombre ?: "Sin categoría",
                            description = dto.descripcion ?: "",
                            price = dto.precioActual ?: dto.precio ?: 0.0,
                            stock = dto.stock
                        )
                    }
                    _allInventoryItems.update { items }
                    _inventoryState.update { InventoryState.Success(items) }
                }
                is Result.Error -> {
                    val errorMessage = result.exception.message ?: "Error desconocido"
                    _inventoryState.update { InventoryState.Error(errorMessage) }
                }
                is Result.Loading -> {
                    // Ya está en estado Loading
                }
            }
        }
    }

    /**
     * Reintento de carga (para botón de retry)
     */
    fun retry() {
        loadInventory()
    }

    /**
     * Obtener producto por código
     */
    fun getProductByCode(code: String): InventoryItem? {
        return _allInventoryItems.value.find { it.code == code }
    }

    /**
     * Obtener ProductoDTO por código para acceder al ID
     */
    fun getProductoDTOByCode(code: String): ProductoDTO? {
        return _productoDTOs.value[code]
    }

    /**
     * Actualizar producto en la API
     */
    fun updateProduct(
        code: String,
        name: String,
        category: String,
        description: String = "",
        price: Double = 0.0,
        stock: Int = 0
    ) {
        if (inventoryRepository == null) {
            _operationState.update { OperationState.Error("Repositorio no disponible") }
            return
        }

        viewModelScope.launch {
            _operationState.update { OperationState.Loading }

            // Obtener el ID real del producto
            val productoDTO = _productoDTOs.value[code]
            if (productoDTO == null) {
                _operationState.update { OperationState.Error("Producto no encontrado") }
                return@launch
            }

            val updateDto = ProductoDTO(
                id = productoDTO.id,
                codigo = code,
                nombre = name,
                descripcion = description,
                precio = null,  // ✅ SIEMPRE null por estructura backend
                precioActual = price,  // ✅ AQUÍ va el precio real
                stock = stock,
                categoria = productoDTO.categoria,
                activo = productoDTO.activo,
                imageUrl = productoDTO.imageUrl,
                fechaIngreso = productoDTO.fechaIngreso,
                ubicacion = productoDTO.ubicacion,
                umbralStock = productoDTO.umbralStock,
                categoriaId = productoDTO.categoriaId
            )

            val result = inventoryRepository.updateProducto(productoDTO.id, updateDto)
            when (result) {
                is Result.Success -> {
                    // Actualizar en caché
                    _productoDTOs.update { it + (code to result.data) }
                    _allInventoryItems.update { currentList ->
                        currentList.map { item ->
                            if (item.code == code) {
                                InventoryItem(
                                    code = code,
                                    name = name,
                                    category = category,
                                    description = description,
                                    price = price,
                                    stock = stock
                                )
                            } else {
                                item
                            }
                        }
                    }
                    if (_inventoryState.value is InventoryState.Success) {
                        _inventoryState.update { InventoryState.Success(_allInventoryItems.value) }
                    }
                    _operationState.update { OperationState.Success("Producto actualizado exitosamente") }
                }
                is Result.Error -> {
                    _operationState.update { OperationState.Error("Error al actualizar: ${result.exception.message}") }
                }
                is Result.Loading -> {}
            }
        }
    }

    /**
     * Agregar producto en la API
     */
    fun addProduct(
        code: String,
        name: String,
        category: String,
        description: String = "",
        price: Double = 0.0,
        stock: Int = 0
    ) {
        if (inventoryRepository == null) {
            _operationState.update { OperationState.Error("Repositorio no disponible") }
            return
        }

        viewModelScope.launch {
            _operationState.update { OperationState.Loading }

            // Obtener la categoría completa por nombre para conseguir el ID
            var categoriaDTO: cl.duoc.maestranza_v2.data.model.CategoriaDTO? = null
            val categoriasResult = inventoryRepository.getCategorias()
            if (categoriasResult is Result.Success) {
                categoriaDTO = categoriasResult.data.find { it.nombre == category }
            }

            val newProductDto = ProductoDTO(
                id = 0L,  // ✅ El servidor generará el ID automáticamente
                codigo = code,
                nombre = name,
                descripcion = description,
                precio = null,  // ✅ SIEMPRE null por estructura backend
                precioActual = price,  // ✅ AQUÍ va el precio real
                stock = stock,
                categoria = categoriaDTO,
                categoriaId = categoriaDTO?.id,
                activo = true,
                umbralStock = stock
            )

            Log.d(TAG, "addProduct - Enviando: codigo=$code, nombre=$name, stock=$stock, categoriaId=${categoriaDTO?.id}, precioActual=$price")
            Log.d(TAG, "addProduct - DTO completo: $newProductDto")

            val result = inventoryRepository.createProducto(newProductDto)
            when (result) {
                is Result.Success -> {
                    val createdProducto = result.data
                    Log.d(TAG, "addProduct - Éxito! Respuesta: id=${createdProducto.id}, codigo=${createdProducto.codigo}, precioActual=${createdProducto.precioActual}")
                    // Cachear el nuevo DTO con su ID asignado
                    _productoDTOs.update { it + (createdProducto.codigo to createdProducto) }

                    val newItem = InventoryItem(
                        code = createdProducto.codigo,
                        name = createdProducto.nombre,
                        category = createdProducto.categoria?.nombre ?: category,
                        description = createdProducto.descripcion ?: description,
                        price = createdProducto.precioActual ?: createdProducto.precio ?: price,
                        stock = createdProducto.stock
                    )
                    _allInventoryItems.update { currentList ->
                        currentList + newItem
                    }
                    if (_inventoryState.value is InventoryState.Success) {
                        _inventoryState.update { InventoryState.Success(_allInventoryItems.value) }
                    }
                    _operationState.update { OperationState.Success("Producto creado exitosamente") }

                    // No recargar aquí - ya actualizamos la lista localmente
                    // loadInventory() puede causar bloqueos
                }
                is Result.Error -> {
                    Log.e(TAG, "addProduct - ERROR: ${result.exception.message}", result.exception)
                    _operationState.update { OperationState.Error("Error al agregar: ${result.exception.message}") }
                }
                is Result.Loading -> {}
            }
        }
    }

    /**
     * Eliminar producto en la API por código
     */
    fun deleteProductByCode(code: String) {
        if (inventoryRepository == null) {
            _operationState.update { OperationState.Error("Repositorio no disponible") }
            return
        }

        viewModelScope.launch {
            _operationState.update { OperationState.Loading }

            // Obtener el ID real del producto
            val productoDTO = _productoDTOs.value[code]
            if (productoDTO == null) {
                _operationState.update { OperationState.Error("Producto no encontrado") }
                return@launch
            }

            val result = inventoryRepository.deleteProducto(productoDTO.id)
            when (result) {
                is Result.Success -> {
                    // Eliminar del caché
                    _productoDTOs.update { it - code }
                    _allInventoryItems.update { currentList ->
                        currentList.filterNot { it.code == code }
                    }
                    if (_inventoryState.value is InventoryState.Success) {
                        _inventoryState.update { InventoryState.Success(_allInventoryItems.value) }
                    }
                    _operationState.update { OperationState.Success("Producto eliminado exitosamente") }
                }
                is Result.Error -> {
                    _operationState.update { OperationState.Error("Error al eliminar: ${result.exception.message}") }
                }
                is Result.Loading -> {}
            }
        }
    }

    /**
     * Eliminar producto en la API por ID
     */
    fun deleteProduct(productId: Long, code: String) {
        if (inventoryRepository == null) {
            _operationState.update { OperationState.Error("Repositorio no disponible") }
            return
        }

        viewModelScope.launch {
            _operationState.update { OperationState.Loading }

            val result = inventoryRepository.deleteProducto(productId)
            when (result) {
                is Result.Success -> {
                    // Eliminar del caché
                    _productoDTOs.update { it - code }
                    _allInventoryItems.update { currentList ->
                        currentList.filterNot { it.code == code }
                    }
                    if (_inventoryState.value is InventoryState.Success) {
                        _inventoryState.update { InventoryState.Success(_allInventoryItems.value) }
                    }
                    _operationState.update { OperationState.Success("Producto eliminado exitosamente") }
                }
                is Result.Error -> {
                    _operationState.update { OperationState.Error("Error al eliminar: ${result.exception.message}") }
                }
                is Result.Loading -> {}
            }
        }
    }
}


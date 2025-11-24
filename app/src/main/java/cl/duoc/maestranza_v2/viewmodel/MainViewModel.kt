package cl.duoc.maestranza_v2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.maestranza_v2.navigation.NavigationEvent
import cl.duoc.maestranza_v2.navigation.Screen
import cl.duoc.maestranza_v2.data.model.InventoryItem
import cl.duoc.maestranza_v2.data.remote.ApiClient
import cl.duoc.maestranza_v2.data.repository.InventoryRepository
import cl.duoc.maestranza_v2.data.repository.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(context: Context? = null) : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private val inventoryRepository = context?.let { InventoryRepository(ApiClient(it)) }

    // Datos hardcodeados como fallback
    private val _hardcodedInventory = listOf(
        InventoryItem("HCOR-001", "Broca HSS 5mm", "Herramientas", "Broca de acero de alta velocidad para perforación en metal", 1500.0, 50),
        InventoryItem("HCOR-002", "Broca HSS 10mm", "Herramientas", "Broca de acero de alta velocidad diámetro 10mm", 2200.0, 35),
        InventoryItem("HCOR-003", "Sierra Circular 7\"", "Herramientas", "Sierra circular eléctrica 1400W con disco de 7 pulgadas", 45000.0, 8),
        InventoryItem("MAT-001", "Lámina Acero Inox 1mm", "Materiales", "Lámina de acero inoxidable calibre 1mm, 1x2 metros", 28000.0, 120),
        InventoryItem("MAT-002", "Tubo Acero 2\" x 6m", "Materiales", "Tubo de acero al carbono 2 pulgadas de diámetro, largo 6 metros", 18500.0, 45),
        InventoryItem("MAT-003", "Perfil Aluminio 40x40", "Materiales", "Perfil de aluminio estructural 40x40mm, largo 6 metros", 12000.0, 0),
        InventoryItem("EQ-001", "Soldadora MIG 200A", "Equipos", "Soldadora de alambre MIG/MAG 200 amperes monofásica", 285000.0, 5),
        InventoryItem("EQ-002", "Compresor 50L", "Equipos", "Compresor de aire 50 litros, 2.5HP, 8 bar", 165000.0, 12),
        InventoryItem("CONS-001", "Disco Corte 4.5\"", "Consumibles", "Disco de corte para metal 4.5 pulgadas espesor 1mm", 850.0, 250),
        InventoryItem("CONS-002", "Lija Grano 80", "Consumibles", "Lija de papel abrasivo grano 80 para metal", 320.0, 3),
        InventoryItem("CONS-003", "Electrodo 6013 3.2mm", "Consumibles", "Electrodo revestido 6013 diámetro 3.2mm para soldadura", 450.0, 180),
        InventoryItem("HCOR-004", "Juego Llaves Allen", "Herramientas", "Set de 9 llaves Allen hexagonales métricas 1.5mm a 10mm", 8500.0, 22)
    )

    private val _inventoryItems = MutableStateFlow(_hardcodedInventory)
    val inventoryItems = _inventoryItems.asStateFlow()

    init {
        // Intentar cargar desde API si tenemos repositorio
        if (inventoryRepository != null) {
            loadProductsFromAPI()
        }
    }

    /**
     * Cargar productos desde la API
     */
    private fun loadProductsFromAPI() {
        viewModelScope.launch {
            val result = inventoryRepository!!.getProductos()
            when (result) {
                is Result.Success -> {
                    // Convertir ProductoDTO a InventoryItem
                    val items = result.data.map { dto ->
                        InventoryItem(
                            code = dto.codigo,
                            name = dto.nombre,
                            category = "",  // Usar nombre de categoría si está disponible
                            description = dto.descripcion ?: "",
                            price = dto.precio ?: 0.0,
                            stock = dto.stock
                        )
                    }
                    _inventoryItems.update { items }
                }
                is Result.Error -> {
                    // Si hay error, mantener los datos hardcodeados
                }
                is Result.Loading -> {
                    // No hacer nada durante la carga
                }
            }
        }
    }

    fun addProduct(
        code: String,
        name: String,
        category: String,
        description: String = "",
        price: Double = 0.0,
        stock: Int = 0
    ) {
        val newItem = InventoryItem(
            code = code,
            name = name,
            category = category,
            description = description,
            price = price,
            stock = stock
        )
        _inventoryItems.update { currentList ->
            currentList + newItem
        }
    }

    fun updateProduct(
        originalCode: String,
        code: String,
        name: String,
        category: String,
        description: String = "",
        price: Double = 0.0,
        stock: Int = 0
    ) {
        _inventoryItems.update { currentList ->
            currentList.map { item ->
                if (item.code == originalCode) {
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
    }

    fun getProductByCode(code: String): InventoryItem? {
        return _inventoryItems.value.find { it.code == code }
    }

    fun navigateTo(screen: Screen) {
        CoroutineScope(context = Dispatchers.Main).launch {
            _navigationEvents.emit(value = NavigationEvent.NavigateTo(route = screen))
        }
    }

    fun navigateBack() {
        CoroutineScope(context = Dispatchers.Main).launch {
            _navigationEvents.emit(value = NavigationEvent.PopBackStack)
        }
    }

    fun navigateUp() {
        CoroutineScope(context = Dispatchers.Main).launch {
            _navigationEvents.emit(value = NavigationEvent.NavigateUp)
        }
    }
}


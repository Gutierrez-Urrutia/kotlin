package cl.duoc.maestranza_v2.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.maestranza_v2.navigation.NavigationEvent
import cl.duoc.maestranza_v2.navigation.Screen
import cl.duoc.maestranza_v2.ui.screens.inventory.InventoryItem

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private val _inventoryItems = MutableStateFlow(
        listOf(
            InventoryItem("HCOR-001", "Broca HSS 5mm", "Herramientas", 50),
            InventoryItem("HCOR-002", "Broca HSS 10mm", "Herramientas", 35),
            InventoryItem("HCOR-003", "Sierra Circular 7\"", "Herramientas", 8),
            InventoryItem("MAT-001", "Lámina Acero Inox 1mm", "Materiales", 120),
            InventoryItem("MAT-002", "Tubo Acero 2\" x 6m", "Materiales", 45),
            InventoryItem("MAT-003", "Perfil Aluminio 40x40", "Materiales", 0),
            InventoryItem("EQ-001", "Soldadora MIG 200A", "Equipos", 5),
            InventoryItem("EQ-002", "Compresor 50L", "Equipos", 12),
            InventoryItem("CONS-001", "Disco Corte 4.5\"", "Consumibles", 250),
            InventoryItem("CONS-002", "Lija Grano 80", "Consumibles", 3),
            InventoryItem("CONS-003", "Electrodo 6013 3.2mm", "Consumibles", 180),
            InventoryItem("HCOR-004", "Juego Llaves Allen", "Herramientas", 22)
        )
    )
    val inventoryItems = _inventoryItems.asStateFlow()

    fun addProduct(code: String, name: String, category: String) {
        val newItem = InventoryItem(
            code = code,
            name = name,
            category = category,
            stock = 0 // Le ponemos un stock inicial de 0 por defecto
        )
        _inventoryItems.update { currentList ->
            currentList + newItem // Añade el nuevo item a la lista actual
        }
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
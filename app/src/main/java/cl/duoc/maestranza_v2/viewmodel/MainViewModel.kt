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
            InventoryItem("HCOR-001", "Broca HSS 5mm", "Herramientas de Corte", 50),
            InventoryItem("HCOR-002", "Broca HSS 10mm", "Herramientas de Corte", 35),
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
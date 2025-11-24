package cl.duoc.maestranza_v2.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.maestranza_v2.navigation.NavigationEvent
import cl.duoc.maestranza_v2.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * MainViewModel - Gestiona la navegación de la aplicación
 * La lógica de inventario ha sido delegada a InventoryViewModel
 */
class MainViewModel : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()


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


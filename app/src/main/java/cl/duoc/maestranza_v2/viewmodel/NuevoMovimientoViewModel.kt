package cl.duoc.maestranza_v2.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.maestranza_v2.model.Movement
import cl.duoc.maestranza_v2.model.MovementType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

// Data classes para estado del formulario
data class SimpleProducto(
    val id: String,
    val codigo: String,
    val nombre: String,
    val stock: Int
)

enum class ComprobanteSource { GALERIA, CAMARA }
enum class FormField { TIPO, PRODUCTO, CANTIDAD }

data class NuevoMovimientoUiState(
    val isLoading: Boolean = false,
    val tipoSeleccionado: MovementType? = null,
    val usuarioNombre: String = "Usuario Demo",
    val productos: List<SimpleProducto> = emptyList(),
    val productoSeleccionado: SimpleProducto? = null,
    val cantidadTexto: String = "",
    val descripcion: String = "",
    val comprobanteUri: Uri? = null,
    val comprobanteSource: ComprobanteSource? = null,
    val errores: Map<FormField, String> = emptyMap(),
    val exito: Boolean = false,
    val errorGlobal: String? = null
)

class NuevoMovimientoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NuevoMovimientoUiState())
    val uiState: StateFlow<NuevoMovimientoUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        // Datos hardcodeados para demo
        val productosDemo = listOf(
            SimpleProducto("1", "HCOR-001", "Broca HSS 5mm", 150),
            SimpleProducto("2", "MAT-001", "Lámina Acero Inox 1mm", 45),
            SimpleProducto("3", "EQ-001", "Soldadora MIG 200A", 5),
            SimpleProducto("4", "CONS-001", "Disco Corte 4.5\"", 200),
            SimpleProducto("5", "HCOR-003", "Sierra Circular 7\"", 12),
            SimpleProducto("6", "MAT-002", "Tubo Acero 2\" x 6m", 30),
            SimpleProducto("7", "CONS-003", "Electrodo 6013 3.2mm", 500),
            SimpleProducto("8", "HCOR-004", "Juego Llaves Allen", 25)
        )

        _uiState.update {
            it.copy(
                productos = productosDemo,
                usuarioNombre = "Admin Demo"
            )
        }
    }

    fun onTipoChanged(tipo: MovementType) {
        _uiState.update { it.copy(tipoSeleccionado = tipo) }
        limpiarErrores(FormField.TIPO)
    }

    fun onProductoSeleccionado(producto: SimpleProducto) {
        _uiState.update { it.copy(productoSeleccionado = producto) }
        limpiarErrores(FormField.PRODUCTO)
    }

    fun onCantidadChanged(cantidad: String) {
        _uiState.update { it.copy(cantidadTexto = cantidad) }
        limpiarErrores(FormField.CANTIDAD)
    }

    fun onDescripcionChanged(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion) }
    }

    fun onComprobanteSeleccionado(uri: Uri, source: ComprobanteSource) {
        _uiState.update { it.copy(comprobanteUri = uri, comprobanteSource = source) }
    }

    fun onQuitarComprobante() {
        _uiState.update { it.copy(comprobanteUri = null, comprobanteSource = null) }
    }

    private fun limpiarErrores(field: FormField) {
        _uiState.update { state ->
            state.copy(errores = state.errores.toMutableMap().apply { remove(field) })
        }
    }

    private fun validar(): Boolean {
        val state = _uiState.value
        val errores = mutableMapOf<FormField, String>()

        if (state.tipoSeleccionado == null) {
            errores[FormField.TIPO] = "Selecciona un tipo de movimiento"
        }

        if (state.productoSeleccionado == null) {
            errores[FormField.PRODUCTO] = "Selecciona un producto"
        }

        val cantidad = state.cantidadTexto.toIntOrNull()
        if (cantidad == null || cantidad <= 0) {
            errores[FormField.CANTIDAD] = "Ingresa una cantidad válida"
        } else if (state.tipoSeleccionado == MovementType.SALIDA && state.productoSeleccionado != null) {
            if (cantidad > state.productoSeleccionado.stock) {
                errores[FormField.CANTIDAD] = "Stock insuficiente (disponible: ${state.productoSeleccionado.stock})"
            }
        }

        _uiState.update { it.copy(errores = errores) }
        return errores.isEmpty()
    }

    fun onCrearMovimiento() {
        if (!validar()) return

        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorGlobal = null) }

            try {
                // Simulación de creación (después se integra con API real)
                val nuevoMovimiento = Movement(
                    id = (Math.random() * 1000).toInt().toString(),
                    fecha = LocalDateTime.now(),
                    usuario = state.usuarioNombre,
                    productoId = state.productoSeleccionado?.id ?: "",
                    productoCodigo = state.productoSeleccionado?.codigo ?: "",
                    productoNombre = state.productoSeleccionado?.nombre ?: "",
                    productoImagePath = null,
                    cantidad = state.cantidadTexto.toInt(),
                    tipo = state.tipoSeleccionado ?: MovementType.ENTRADA,
                    descripcion = state.descripcion.takeIf { it.isNotBlank() },
                    comprobantePath = null
                )

                // Simulación de envío
                kotlinx.coroutines.delay(1500)

                _uiState.update { it.copy(isLoading = false, exito = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorGlobal = e.message ?: "Error al crear el movimiento"
                    )
                }
            }
        }
    }

    fun reset() {
        _uiState.update {
            NuevoMovimientoUiState(
                productos = it.productos,
                usuarioNombre = it.usuarioNombre
            )
        }
    }
}


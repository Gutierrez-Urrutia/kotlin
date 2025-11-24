package cl.duoc.maestranza_v2.data.repository

import cl.duoc.maestranza_v2.data.model.MovimientoDTO
import cl.duoc.maestranza_v2.data.model.CrearMovimientoRequest
import cl.duoc.maestranza_v2.data.remote.ApiClient

class MovementsRepository(private val apiClient: ApiClient) {

    private val api = apiClient.service

    /**
     * Obtener todos los movimientos
     */
    suspend fun getMovimientos(): Result<List<MovimientoDTO>> {
        return try {
            val response = api.getMovimientos()
            if (response.isSuccessful) {
                val movimientos = response.body() ?: emptyList()
                Result.Success(movimientos)
            } else {
                Result.Error(Exception("Error al obtener movimientos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener movimiento por ID
     */
    suspend fun getMovimientoById(id: Long): Result<MovimientoDTO> {
        return try {
            val response = api.getMovimientoById(id)
            if (response.isSuccessful) {
                val movimiento = response.body()
                if (movimiento != null) {
                    Result.Success(movimiento)
                } else {
                    Result.Error(Exception("Movimiento no encontrado"))
                }
            } else {
                Result.Error(Exception("Error al obtener movimiento: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Crear nuevo movimiento
     */
    suspend fun createMovimiento(movimiento: CrearMovimientoRequest): Result<MovimientoDTO> {
        return try {
            val response = api.createMovimiento(movimiento)
            if (response.isSuccessful) {
                val nuevoMovimiento = response.body()
                if (nuevoMovimiento != null) {
                    Result.Success(nuevoMovimiento)
                } else {
                    Result.Error(Exception("Respuesta vacía al crear movimiento"))
                }
            } else {
                Result.Error(Exception("Error al crear movimiento: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Actualizar movimiento
     */
    suspend fun updateMovimiento(id: Long, movimiento: MovimientoDTO): Result<MovimientoDTO> {
        return try {
            val response = api.updateMovimiento(id, movimiento)
            if (response.isSuccessful) {
                val movimientoActualizado = response.body()
                if (movimientoActualizado != null) {
                    Result.Success(movimientoActualizado)
                } else {
                    Result.Error(Exception("Respuesta vacía al actualizar"))
                }
            } else {
                Result.Error(Exception("Error al actualizar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Eliminar movimiento
     */
    suspend fun deleteMovimiento(id: Long): Result<Boolean> {
        return try {
            val response = api.deleteMovimiento(id)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("Error al eliminar movimiento: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener movimientos por tipo (ENTRADA o SALIDA)
     */
    suspend fun getMovimientosPorTipo(tipo: String): Result<List<MovimientoDTO>> {
        return try {
            val response = api.getMovimientosPorTipo(tipo)
            if (response.isSuccessful) {
                val movimientos = response.body() ?: emptyList()
                Result.Success(movimientos)
            } else {
                Result.Error(Exception("Error al obtener movimientos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener movimientos por producto
     */
    suspend fun getMovimientosPorProducto(productoId: Long): Result<List<MovimientoDTO>> {
        return try {
            val response = api.getMovimientosPorProducto(productoId)
            if (response.isSuccessful) {
                val movimientos = response.body() ?: emptyList()
                Result.Success(movimientos)
            } else {
                Result.Error(Exception("Error al obtener movimientos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener movimientos por usuario
     */
    suspend fun getMovimientosPorUsuario(usuarioId: Long): Result<List<MovimientoDTO>> {
        return try {
            val response = api.getMovimientosPorUsuario(usuarioId)
            if (response.isSuccessful) {
                val movimientos = response.body() ?: emptyList()
                Result.Success(movimientos)
            } else {
                Result.Error(Exception("Error al obtener movimientos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Buscar movimientos con filtros avanzados
     */
    suspend fun buscarConFiltros(
        productoId: Long? = null,
        usuarioId: Long? = null,
        tipo: String? = null,
        fechaInicio: String? = null,
        fechaFin: String? = null
    ): Result<List<MovimientoDTO>> {
        return try {
            val response = api.buscarMovimientosConFiltros(
                productoId, usuarioId, tipo, fechaInicio, fechaFin
            )
            if (response.isSuccessful) {
                val movimientos = response.body() ?: emptyList()
                Result.Success(movimientos)
            } else {
                Result.Error(Exception("Error al buscar movimientos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener movimientos por rango de fechas
     */
    suspend fun getMovimientosPorRangoFechas(
        fechaInicio: String,
        fechaFin: String
    ): Result<List<MovimientoDTO>> {
        return try {
            val response = api.getMovimientosPorRangoFechas(fechaInicio, fechaFin)
            if (response.isSuccessful) {
                val movimientos = response.body() ?: emptyList()
                Result.Success(movimientos)
            } else {
                Result.Error(Exception("Error al obtener movimientos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Buscar movimientos por código de producto
     */
    suspend fun buscarPorCodigoProducto(codigo: String): Result<List<MovimientoDTO>> {
        return try {
            val response = api.buscarMovimientosPorCodigoProducto(codigo)
            if (response.isSuccessful) {
                val movimientos = response.body() ?: emptyList()
                Result.Success(movimientos)
            } else {
                Result.Error(Exception("Error al buscar movimientos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Buscar movimientos por nombre de producto
     */
    suspend fun buscarPorNombreProducto(nombre: String): Result<List<MovimientoDTO>> {
        return try {
            val response = api.buscarMovimientosPorNombreProducto(nombre)
            if (response.isSuccessful) {
                val movimientos = response.body() ?: emptyList()
                Result.Success(movimientos)
            } else {
                Result.Error(Exception("Error al buscar movimientos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}


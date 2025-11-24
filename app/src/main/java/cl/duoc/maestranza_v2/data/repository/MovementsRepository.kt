package cl.duoc.maestranza_v2.data.repository

import android.util.Log
import cl.duoc.maestranza_v2.data.model.MovimientoDTO
import cl.duoc.maestranza_v2.data.remote.ApiClient

class MovementsRepository(private val apiClient: ApiClient) {

    private val api = apiClient.service

    /**
     * Obtener todos los movimientos
     */
    suspend fun getMovimientos(): Result<List<MovimientoDTO>> {
        return try {
            Log.d("MovementsRepository", "Obteniendo movimientos desde API...")
            val response = api.getMovimientos()
            if (response.isSuccessful) {
                val movimientos = response.body() ?: emptyList()
                Log.d("MovementsRepository", "Movimientos obtenidos: ${movimientos.size} items")
                Result.Success(movimientos)
            } else {
                Log.e("MovementsRepository", "Error al obtener movimientos: ${response.code()}")
                Result.Error(Exception("Error al obtener movimientos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("MovementsRepository", "Excepción al obtener movimientos", e)
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
     * Obtener movimientos por tipo
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
}


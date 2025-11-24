package cl.duoc.maestranza_v2.data.repository

import android.util.Log
import cl.duoc.maestranza_v2.data.model.RolDTO
import cl.duoc.maestranza_v2.data.remote.ApiClient

class RolesRepository(private val apiClient: ApiClient) {

    private val api = apiClient.service

    /**
     * Obtener todos los roles
     */
    suspend fun getRoles(): Result<List<RolDTO>> {
        return try {
            val response = api.getRoles()
            if (response.isSuccessful) {
                val roles = response.body() ?: emptyList()
                Log.d("RolesRepository", "Roles cargados desde API: $roles")
                Result.Success(roles)
            } else {
                Log.e("RolesRepository", "Error al obtener roles: ${response.code()}")
                Result.Error(Exception("Error al obtener roles: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("RolesRepository", "Excepción al obtener roles", e)
            Result.Error(e)
        }
    }

    /**
     * Obtener rol por ID
     */
    suspend fun getRolById(id: Int): Result<RolDTO> {
        return try {
            val response = api.getRolById(id)
            if (response.isSuccessful) {
                val rol = response.body()
                if (rol != null) {
                    Result.Success(rol)
                } else {
                    Result.Error(Exception("Rol no encontrado"))
                }
            } else {
                Result.Error(Exception("Error al obtener rol: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener rol por nombre
     */
    suspend fun getRolPorNombre(nombre: String): Result<RolDTO> {
        return try {
            val response = api.getRolPorNombre(nombre)
            if (response.isSuccessful) {
                val rol = response.body()
                if (rol != null) {
                    Result.Success(rol)
                } else {
                    Result.Error(Exception("Rol no encontrado"))
                }
            } else {
                Result.Error(Exception("Error al obtener rol: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}


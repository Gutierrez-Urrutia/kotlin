package cl.duoc.maestranza_v2.data.repository

import cl.duoc.maestranza_v2.data.model.UsuarioDTO
import cl.duoc.maestranza_v2.data.model.ActualizarUsuarioDTO
import cl.duoc.maestranza_v2.data.remote.ApiClient

class UsersRepository(private val apiClient: ApiClient) {

    private val api = apiClient.service

    /**
     * Obtener todos los usuarios
     */
    suspend fun getUsuarios(): Result<List<UsuarioDTO>> {
        return try {
            val response = api.getUsuarios()
            if (response.isSuccessful) {
                val usuarios = response.body() ?: emptyList()
                Result.Success(usuarios)
            } else {
                Result.Error(Exception("Error al obtener usuarios: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener usuario por ID
     */
    suspend fun getUsuarioById(id: Long): Result<UsuarioDTO> {
        return try {
            val response = api.getUsuarioById(id)
            if (response.isSuccessful) {
                val usuario = response.body()
                if (usuario != null) {
                    Result.Success(usuario)
                } else {
                    Result.Error(Exception("Usuario no encontrado"))
                }
            } else {
                Result.Error(Exception("Error al obtener usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener usuario por username
     */
    suspend fun getUsuarioPorUsername(username: String): Result<UsuarioDTO> {
        return try {
            val response = api.getUsuarioPorUsername(username)
            if (response.isSuccessful) {
                val usuario = response.body()
                if (usuario != null) {
                    Result.Success(usuario)
                } else {
                    Result.Error(Exception("Usuario no encontrado"))
                }
            } else {
                Result.Error(Exception("Error al obtener usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Crear nuevo usuario
     */
    suspend fun createUsuario(usuario: UsuarioDTO): Result<Boolean> {
        return try {
            val response = api.createUsuario(usuario)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("Error al crear usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Actualizar usuario
     */
    suspend fun updateUsuario(id: Long, usuario: ActualizarUsuarioDTO): Result<Boolean> {
        return try {
            val response = api.updateUsuario(id, usuario)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("Error al actualizar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Eliminar usuario
     */
    suspend fun deleteUsuario(id: Long): Result<Boolean> {
        return try {
            val response = api.deleteUsuario(id)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("Error al eliminar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Activar usuario
     */
    suspend fun activarUsuario(id: Long): Result<Boolean> {
        return try {
            val response = api.activarUsuario(id)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("Error al activar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Desactivar usuario
     */
    suspend fun desactivarUsuario(id: Long): Result<Boolean> {
        return try {
            val response = api.desactivarUsuario(id)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("Error al desactivar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}


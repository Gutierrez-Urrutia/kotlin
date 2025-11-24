package cl.duoc.maestranza_v2.data.repository

import android.util.Log
import cl.duoc.maestranza_v2.data.model.UsuarioDTO
import cl.duoc.maestranza_v2.data.model.ActualizarUsuarioDTO
import cl.duoc.maestranza_v2.data.model.CrearUsuarioDTO
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
    suspend fun createUsuario(usuario: CrearUsuarioDTO): Result<Boolean> {
        return try {
            Log.d("UsersRepository", "Enviando usuario: username=${usuario.username}, email=${usuario.email}, roles=${usuario.roles}")

            val response = api.createUsuario(usuario)

            Log.d("UsersRepository", "Response code: ${response.code()}, isSuccessful: ${response.isSuccessful}")

            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                // Intentar obtener el mensaje de error del servidor
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Log.e("UsersRepository", "Error body: $errorBody")

                val mensajeUsuario = when (response.code()) {
                    400 -> {
                        // Error de validación - intentar extraer detalles
                        val detalles = when {
                            errorBody.contains("username", ignoreCase = true) ->
                                "El nombre de usuario no es válido o ya existe. Intenta con otro."
                            errorBody.contains("email", ignoreCase = true) ->
                                "El correo electrónico no es válido o ya existe. Intenta con otro."
                            errorBody.contains("password", ignoreCase = true) ->
                                "La contraseña no cumple los requisitos de seguridad. Debe tener al menos 6 caracteres."
                            errorBody.contains("nombre", ignoreCase = true) ->
                                "El nombre contiene caracteres inválidos."
                            errorBody.contains("apellido", ignoreCase = true) ->
                                "El apellido contiene caracteres inválidos."
                            errorBody.contains("roles", ignoreCase = true) ||
                            errorBody.contains("role", ignoreCase = true) ->
                                "Los roles seleccionados no son válidos. Por favor, selecciona roles existentes."
                            else -> "Verifica que todos los campos sean correctos y válidos. $errorBody"
                        }
                        "Datos inválidos. $detalles"
                    }
                    401 -> "No tienes permiso para crear usuarios. Contacta al administrador."
                    409 -> "El usuario o correo ya existe en el sistema. Intenta con otros datos."
                    500 -> "Error en el servidor. Por favor, intenta más tarde o contacta al administrador."
                    503 -> "El servidor está en mantenimiento. Por favor, intenta más tarde."
                    else -> "Error al crear usuario (Código: ${response.code()}). Por favor, intenta de nuevo."
                }

                Result.Error(Exception(mensajeUsuario))
            }
        } catch (e: Exception) {
            Log.e("UsersRepository", "Exception creating user", e)

            val mensajeUsuario = when {
                e.message?.contains("timeout", ignoreCase = true) == true ->
                    "La conexión tardó demasiado. Verifica tu conexión a internet e intenta de nuevo."
                e.message?.contains("connection", ignoreCase = true) == true ->
                    "Error de conexión. Verifica que estés conectado a internet."
                e.message?.contains("unreachable", ignoreCase = true) == true ->
                    "No se puede alcanzar el servidor. Verifica tu conexión a internet."
                else -> "No se pudo crear el usuario. Por favor, intenta de nuevo."
            }
            Result.Error(Exception(mensajeUsuario))
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


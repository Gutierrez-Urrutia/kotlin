package cl.duoc.maestranza_v2.data.repository

import cl.duoc.maestranza_v2.data.local.TokenManager
import cl.duoc.maestranza_v2.data.model.LoginRequest
import cl.duoc.maestranza_v2.data.model.JwtResponse
import cl.duoc.maestranza_v2.data.model.SignupRequest
import cl.duoc.maestranza_v2.data.remote.ApiClient

class AuthRepository(private val apiClient: ApiClient) {

    private val api = apiClient.service
    private val tokenManager = apiClient.getTokenManager()

    /**
     * Login del usuario
     */
    suspend fun login(username: String, password: String): Result<JwtResponse> {
        return try {
            val request = LoginRequest(username, password)
            val response = api.login(request)

            if (response.isSuccessful) {
                val jwtResponse = response.body()
                if (jwtResponse != null) {
                    // Guardar token y datos del usuario
                    tokenManager.saveToken(jwtResponse.token)
                    tokenManager.saveUserInfo(
                        userId = jwtResponse.id.toString(),
                        username = jwtResponse.username,
                        email = jwtResponse.email,
                        nombre = jwtResponse.nombre,
                        apellido = jwtResponse.apellido
                    )
                    Result.Success(jwtResponse)
                } else {
                    Result.Error(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.Error(Exception("Error de autenticación: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Registro de nuevo usuario
     */
    suspend fun signup(
        username: String,
        email: String,
        password: String,
        nombre: String,
        apellido: String,
        roles: List<String>
    ): Result<String> {
        return try {
            val request = SignupRequest(
                username = username,
                email = email,
                password = password,
                nombre = nombre,
                apellido = apellido,
                roles = roles
            )
            val response = api.signup(request)

            if (response.isSuccessful) {
                val messageResponse = response.body()
                if (messageResponse != null) {
                    Result.Success(messageResponse.message)
                } else {
                    Result.Error(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.Error(Exception("Error en registro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Logout del usuario
     */
    suspend fun logout(): Result<String> {
        return try {
            val response = api.logout()

            // Siempre limpiar datos locales, sin importar si el servidor responde
            tokenManager.clearAll()

            if (response.isSuccessful) {
                Result.Success("Sesión cerrada correctamente")
            } else {
                // Aunque el servidor falle, consideramos el logout como exitoso
                // ya que los datos locales están limpios
                Result.Success("Sesión cerrada localmente")
            }
        } catch (e: Exception) {
            // Incluso si hay error de red, limpiar datos locales
            tokenManager.clearAll()
            Result.Success("Sesión cerrada localmente")
        }
    }

    /**
     * Verificar si el usuario está autenticado y activo
     */
    suspend fun verify(): Result<Boolean> {
        return try {
            val response = api.verify()
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("No autenticado"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Validar que el token sea válido
     */
    suspend fun validateToken(): Result<Boolean> {
        return try {
            val response = api.validateToken()
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Exception("Token inválido"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Obtener el token actual
     */
    fun getTokenFlow() = tokenManager.getToken()

    /**
     * Obtener información del usuario actual
     */
    fun getUserInfoFlow() = tokenManager.getUserInfo()
}


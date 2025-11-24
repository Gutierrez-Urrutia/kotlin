package cl.duoc.maestranza_v2.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import cl.duoc.maestranza_v2.data.local.TokenManager

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Obtener el token de forma sincrónica (en contexto de interceptor)
        val token = runBlocking {
            tokenManager.getToken().first()
        }

        // Si hay token, agregar el header Authorization
        val requestWithAuth = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(requestWithAuth)
    }
}


package cl.duoc.maestranza_v2.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "maestranza_preferences"
private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

class TokenManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NOMBRE_KEY = stringPreferencesKey("user_nombre")
        private val USER_APELLIDO_KEY = stringPreferencesKey("user_apellido")
    }

    // Guardar token
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // Obtener token
    fun getToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    // Guardar información del usuario
    suspend fun saveUserInfo(
        userId: String,
        username: String,
        email: String,
        nombre: String,
        apellido: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USERNAME_KEY] = username
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_NOMBRE_KEY] = nombre
            preferences[USER_APELLIDO_KEY] = apellido
        }
    }

    // Obtener información del usuario
    fun getUserInfo(): Flow<UserInfo?> = context.dataStore.data.map { preferences ->
        val userId = preferences[USER_ID_KEY]
        val username = preferences[USERNAME_KEY]
        val email = preferences[USER_EMAIL_KEY]
        val nombre = preferences[USER_NOMBRE_KEY]
        val apellido = preferences[USER_APELLIDO_KEY]

        if (userId != null && username != null && email != null) {
            UserInfo(
                userId = userId,
                username = username,
                email = email,
                nombre = nombre ?: "",
                apellido = apellido ?: ""
            )
        } else {
            null
        }
    }

    // Limpiar datos (logout)
    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

data class UserInfo(
    val userId: String,
    val username: String,
    val email: String,
    val nombre: String,
    val apellido: String
)


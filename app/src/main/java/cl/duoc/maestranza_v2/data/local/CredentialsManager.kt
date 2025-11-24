package cl.duoc.maestranza_v2.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private const val CREDENTIALS_STORE_NAME = "maestranza_credentials"
private val Context.credentialsDataStore by preferencesDataStore(name = CREDENTIALS_STORE_NAME)

/**
 * Gestor para almacenar credenciales de forma segura usando DataStore
 */
class CredentialsManager(private val context: Context) {
    companion object {
        private const val TAG = "CredentialsManager"
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
    }

    private val dataStore = context.credentialsDataStore

    /**
     * Guardar credenciales de forma cifrada
     */
    suspend fun saveCredentials(username: String, password: String) {
        try {
            Log.d(TAG, ">>> Intentando guardar credenciales para: $username")
            dataStore.edit { preferences ->
                Log.d(TAG, ">>> Antes de edit: username=${preferences[USERNAME_KEY]}")
                preferences[USERNAME_KEY] = username
                preferences[PASSWORD_KEY] = password
                Log.d(TAG, ">>> Después de edit: username=${preferences[USERNAME_KEY]}")
            }
            Log.d(TAG, ">>> ✓ Credenciales guardadas exitosamente para usuario: $username")
        } catch (e: Exception) {
            Log.e(TAG, ">>> ✗ Error al guardar credenciales: ${e.message}", e)
            e.printStackTrace()
        }
    }

    /**
     * Obtener credenciales guardadas
     */
    suspend fun getCredentials(): Pair<String?, String?>? {
        return try {
            Log.d(TAG, ">>> Recuperando credenciales...")
            val preferences = dataStore.data.first()
            val username = preferences[USERNAME_KEY]
            val password = preferences[PASSWORD_KEY]
            Log.d(TAG, ">>> Datos del DataStore - username: $username, password: ${if (password != null) "***" else "null"}")

            if (username != null && password != null) {
                Log.d(TAG, ">>> ✓ Credenciales encontradas, retornando...")
                Pair(username, password)
            } else {
                Log.d(TAG, ">>> ✗ No hay credenciales completas guardadas")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, ">>> ✗ Error al recuperar credenciales: ${e.message}", e)
            e.printStackTrace()
            null
        }
    }

    /**
     * Verificar si hay credenciales guardadas
     */
    suspend fun hasCredentials(): Boolean {
        return try {
            val preferences = dataStore.data.first()
            val has = preferences.contains(USERNAME_KEY) && preferences.contains(PASSWORD_KEY)
            Log.d(TAG, ">>> hasCredentials: $has")
            has
        } catch (e: Exception) {
            Log.e(TAG, ">>> Error al verificar credenciales: ${e.message}", e)
            false
        }
    }

    /**
     * Limpiar credenciales (logout)
     */
    suspend fun clearCredentials() {
        try {
            Log.d(TAG, ">>> Limpiando credenciales...")
            dataStore.edit { preferences ->
                preferences.remove(USERNAME_KEY)
                preferences.remove(PASSWORD_KEY)
            }
            Log.d(TAG, ">>> ✓ Credenciales limpiadas")
        } catch (e: Exception) {
            Log.e(TAG, ">>> ✗ Error al limpiar credenciales: ${e.message}", e)
        }
    }
}


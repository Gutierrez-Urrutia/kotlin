package cl.duoc.maestranza_v2.data.repository

/**
 * Sealed class para manejar resultados de operaciones asincrónicas
 * Permite representar tres estados: Success, Error, Loading
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    /**
     * Ejecuta un bloque de código si el resultado es Success
     */
    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(exception)
        is Loading -> Loading
    }

    /**
     * Obtiene el valor si es Success o null si no
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
        is Loading -> null
    }

    /**
     * Obtiene la excepción si es Error o null si no
     */
    fun exceptionOrNull(): Exception? = when (this) {
        is Success -> null
        is Error -> exception
        is Loading -> null
    }

    /**
     * Verifica si el resultado es Success
     */
    fun isSuccess(): Boolean = this is Success

    /**
     * Verifica si el resultado es Error
     */
    fun isError(): Boolean = this is Error

    /**
     * Verifica si está en estado Loading
     */
    fun isLoading(): Boolean = this is Loading
}


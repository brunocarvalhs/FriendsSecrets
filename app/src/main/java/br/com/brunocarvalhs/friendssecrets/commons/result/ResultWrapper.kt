package br.com.brunocarvalhs.friendssecrets.commons.result

import br.com.brunocarvalhs.friendssecrets.commons.extensions.report
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * A utility class that wraps a suspending operation and returns a [Result] with success or error.
 * This helps standardize error handling across the application.
 */
sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: Throwable) : ResultWrapper<Nothing>()
    data object Loading : ResultWrapper<Nothing>()

    companion object {
        /**
         * Executes a suspending operation and wraps the result in a [ResultWrapper].
         * @param dispatcher The coroutine dispatcher to use for the operation
         * @param block The suspending operation to execute
         * @return A [ResultWrapper] with the result of the operation
         */
        suspend fun <T> execute(
            dispatcher: CoroutineDispatcher,
            block: suspend () -> T
        ): ResultWrapper<T> {
            return withContext(dispatcher) {
                try {
                    Success(block())
                } catch (e: Exception) {
                    Timber.e(e.report(), "Error executing operation")
                    Error(e)
                }
            }
        }
    }

    /**
     * Executes the given block if this is a [Success].
     * @param block The block to execute with the success data
     * @return This [ResultWrapper]
     */
    inline fun onSuccess(block: (T) -> Unit): ResultWrapper<T> {
        if (this is Success) {
            block(data)
        }
        return this
    }

    /**
     * Executes the given block if this is an [Error].
     * @param block The block to execute with the error
     * @return This [ResultWrapper]
     */
    inline fun onError(block: (Throwable) -> Unit): ResultWrapper<T> {
        if (this is Error) {
            block(exception)
        }
        return this
    }

    /**
     * Executes the given block if this is [Loading].
     * @param block The block to execute
     * @return This [ResultWrapper]
     */
    inline fun onLoading(block: () -> Unit): ResultWrapper<T> {
        if (this is Loading) {
            block()
        }
        return this
    }

    /**
     * Maps the success data to a new value.
     * @param transform The transformation function
     * @return A new [ResultWrapper] with the transformed data
     */
    inline fun <R> map(transform: (T) -> R): ResultWrapper<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(exception)
            is Loading -> Loading
        }
    }

    /**
     * Returns the success data or null if this is not a [Success].
     * @return The success data or null
     */
    fun getOrNull(): T? {
        return when (this) {
            is Success -> data
            else -> null
        }
    }

    /**
     * Returns the success data or the result of the given function if this is not a [Success].
     * @param defaultValue The function to execute if this is not a [Success]
     * @return The success data or the result of the given function
     */
    inline fun getOrElse(defaultValue: () -> @UnsafeVariance T): T {
        return when (this) {
            is Success -> data
            else -> defaultValue()
        }
    }
}
package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.commons.coroutines.CoroutineDispatcherProvider
import br.com.brunocarvalhs.friendssecrets.commons.result.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A generic class that can provide a resource backed by both the local database and the network.
 * This is useful for implementing the offline-first approach.
 */
abstract class NetworkBoundResource<ResultType, RequestType> {

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    /**
     * Called to get the cached data from the database.
     */
    protected abstract suspend fun loadFromDb(): ResultType?

    /**
     * Called to create the API request.
     */
    protected abstract suspend fun createCall(): RequestType

    /**
     * Called to save the result of the API response into the database.
     */
    protected abstract suspend fun saveCallResult(item: RequestType)

    /**
     * Called to determine whether to fetch potentially updated data from the network.
     */
    protected open fun shouldFetch(data: ResultType?): Boolean = true

    /**
     * Called to process the result before returning it.
     */
    protected open suspend fun processResult(data: ResultType): ResultType = data

    /**
     * Returns a flow that emits the resource as it's loaded from the network and the database.
     */
    fun asFlow(): Flow<ResultWrapper<ResultType>> = flow {
        emit(ResultWrapper.Loading)

        // Load from database first
        val dbSource = loadFromDb()

        if (dbSource != null && !shouldFetch(dbSource)) {
            // If we don't need to fetch from network, return the database result
            emit(ResultWrapper.Success(processResult(dbSource)))
        } else {
            // Otherwise, fetch from network
            try {
                withContext(dispatchers.io) {
                    val apiResponse = createCall()
                    saveCallResult(apiResponse)
                }
                val newData = loadFromDb()
                if (newData != null) {
                    emit(ResultWrapper.Success(processResult(newData)))
                } else {
                    emit(ResultWrapper.Error(Exception("Failed to load data")))
                }
            } catch (e: Exception) {
                // If we have data in the database, emit it even if the network request failed
                if (dbSource != null) {
                    emit(ResultWrapper.Success(processResult(dbSource)))
                } else {
                    emit(ResultWrapper.Error(e))
                }
            }
        }
    }
}
package br.com.brunocarvalhs.friendssecrets.commons.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides coroutine dispatchers for different contexts.
 * This class is useful for testing as it allows for easy mocking of dispatchers.
 */
@Singleton
class CoroutineDispatcherProvider @Inject constructor() {
    val main: CoroutineDispatcher = Dispatchers.Main
    val io: CoroutineDispatcher = Dispatchers.IO
    val default: CoroutineDispatcher = Dispatchers.Default
    val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}
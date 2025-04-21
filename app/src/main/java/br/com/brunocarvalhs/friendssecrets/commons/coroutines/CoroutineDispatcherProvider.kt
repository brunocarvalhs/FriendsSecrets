package br.com.brunocarvalhs.friendssecrets.commons.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Provedor de dispatchers para coroutines.
 * Esta classe é útil para testes, pois permite a substituição fácil dos dispatchers.
 */
class CoroutineDispatcherProvider {
    val main: CoroutineDispatcher = Dispatchers.Main
    val io: CoroutineDispatcher = Dispatchers.IO
    val default: CoroutineDispatcher = Dispatchers.Default
    val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}

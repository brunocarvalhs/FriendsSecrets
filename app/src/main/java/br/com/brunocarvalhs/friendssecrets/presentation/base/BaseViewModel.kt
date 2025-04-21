package br.com.brunocarvalhs.friendssecrets.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.commons.coroutines.CoroutineDispatcherProvider
import br.com.brunocarvalhs.friendssecrets.di.ServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel base que fornece funcionalidades comuns para todos os ViewModels.
 * Inclui helpers para coroutines com dispatchers apropriados.
 */
abstract class BaseViewModel : ViewModel() {

    // Obtém o CoroutineDispatcherProvider do ServiceLocator
    private val dispatchers: CoroutineDispatcherProvider = ServiceLocator.getCoroutineDispatcherProvider()

    /**
     * Lança uma coroutine no dispatcher IO e muda para Main quando completa
     */
    protected fun launchIO(
        onError: (Throwable) -> Unit = { throw it },
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(dispatchers.io) {
            try {
                block()
            } catch (e: Throwable) {
                withContext(dispatchers.main) {
                    onError(e)
                }
            }
        }
    }

    /**
     * Lança uma coroutine no dispatcher Main
     */
    protected fun launchMain(
        onError: (Throwable) -> Unit = { throw it },
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(dispatchers.main) {
            try {
                block()
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }
}

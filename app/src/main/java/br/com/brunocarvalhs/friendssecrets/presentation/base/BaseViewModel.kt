package br.com.brunocarvalhs.friendssecrets.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.commons.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Base ViewModel class that provides common functionality for all ViewModels.
 * It includes coroutine helpers with appropriate dispatchers.
 */
abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    /**
     * Launch a coroutine in the IO dispatcher and switch to Main when complete
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
     * Launch a coroutine in the Main dispatcher
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
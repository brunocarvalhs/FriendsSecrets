package br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.useCases.DrawRevelationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DrawViewModel(
    private val drawRevelationUseCase: DrawRevelationUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<DrawUiState> =
        MutableStateFlow(DrawUiState.Idle)

    val uiState: StateFlow<DrawUiState> =
        _uiState.asStateFlow()

    fun eventIntent(intent: DrawIntent) {
        when (intent) {
            is DrawIntent.FetchDraw -> fetchDraw(group = intent.group, code = intent.code)
            DrawIntent.Refresh -> {
                _uiState.value = DrawUiState.Idle
            }
        }
    }

    private fun fetchDraw(group: String, code: String? = null) {
        _uiState.value = DrawUiState.Loading
        drawRevelationUseCase.invoke(group, code).onSuccess {
            it?.let {
                _uiState.value = DrawUiState.Success(it)
            }
        }.onFailure {
            _uiState.value = DrawUiState.Error(it.message.orEmpty())
        }
    }

    companion object {


        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val storage = StorageService()
                val cryptoService = CryptoService()
                val drawRevelationUseCase = DrawRevelationUseCase(
                    storage = storage,
                    cryptoService = cryptoService
                )
                DrawViewModel(drawRevelationUseCase = drawRevelationUseCase)
            }
        }
    }
}
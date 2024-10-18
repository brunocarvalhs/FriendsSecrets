package br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.extensions.report
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.repository.GroupRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.useCases.DrawRevelationUseCase
import br.com.brunocarvalhs.friendssecrets.presentation.views.generative.GenerativeNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

            is DrawIntent.GenerativeDraw -> generativeDraw(
                context = intent.context,
                navigation = intent.navigation,
                group = intent.group,
                secret = intent.secret,
                likes = intent.likes
            )
        }
    }

    private fun generativeDraw(
        context: Context,
        navigation: NavController,
        group: GroupEntities,
        secret: String,
        likes: List<String> = emptyList(),
    ) {
        val prompt = context.getString(R.string.ai_prompt, secret, likes.toString(), group.name)
        
        navigation.navigate(
            route = GenerativeNavigation.Chat.createRoute(prompt)
        )
    }

    private fun fetchDraw(group: String, code: String? = null) {
        _uiState.value = DrawUiState.Loading
        viewModelScope.launch {
            drawRevelationUseCase.invoke(group, code).onSuccess {
                it?.let {
                    _uiState.value = DrawUiState.Success(group = it.first, draw = it.second)
                } ?: run {
                    _uiState.value = DrawUiState.Idle
                }
            }.onFailure {
                _uiState.value = DrawUiState.Error(it.report()?.message.orEmpty())
            }
        }
    }

    companion object {


        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = GroupRepositoryImpl()
                val storage = StorageService()
                val cryptoService = CryptoService()
                val drawRevelationUseCase = DrawRevelationUseCase(
                    repository = repository,
                    storage = storage,
                    cryptoService = cryptoService
                )
                DrawViewModel(drawRevelationUseCase = drawRevelationUseCase)
            }
        }
    }
}
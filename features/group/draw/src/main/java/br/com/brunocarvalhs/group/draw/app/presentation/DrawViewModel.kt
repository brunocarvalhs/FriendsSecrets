package br.com.brunocarvalhs.group.draw.app.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.group.draw.app.domain.useCases.DrawUseCase
import br.com.brunocarvalhs.group.draw.app.domain.useCases.ShareSecretFriendsUseCase
import br.com.brunocarvalhs.group.draw.commons.navigation.DrawGraphRouter
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
internal class DrawViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val shareSecretFriendsUseCase: ShareSecretFriendsUseCase,
    private val drawUseCase: DrawUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<DrawGraphRouter>(DrawGraphRouter.typeMap)

    private val _uiState: MutableStateFlow<DrawUiState> = MutableStateFlow(
        DrawUiState(
            members = args.group.members,
            results = args.group.draws,
            isDrawn = args.group.draws.isNotEmpty()
        )
    )
    val uiState: StateFlow<DrawUiState> = _uiState.asStateFlow()

    @AddTrace(name = "DrawViewModel.eventIntent", enabled = true)
    fun handleIntent(intent: DrawIntent) = when (intent) {
        is DrawIntent.Share -> share(intent.secret)
        DrawIntent.Draw -> draw()
    }

    private fun share(secret: String) {
        shareSecretFriendsUseCase(group = args.group, secret = secret).onSuccess {

        }.onFailure {

        }
    }

    private fun draw() {
        viewModelScope.launch {
            drawUseCase(group = args.group).onSuccess { results ->
                _uiState.value = _uiState.value.copy(
                    results = results,
                    isDrawn = true
                )
            }.onFailure {

            }
        }
    }
}

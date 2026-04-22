package br.com.brunocarvalhs.group.draw.app.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.friendssecrets.core.navigation.DrawGraph
import br.com.brunocarvalhs.group.draw.app.domain.useCases.DrawUseCase
import br.com.brunocarvalhs.group.draw.app.domain.useCases.ShareSecretFriendsUseCase
import br.com.brunocarvalhs.group.draw.commons.analytics.DrawAnalytics
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
    private val drawUseCase: DrawUseCase,
    private val analytics: DrawAnalytics
) : ViewModel() {

    private val args = savedStateHandle.toRoute<DrawGraph>(DrawGraph.typeMap)

    private val _uiState: MutableStateFlow<DrawUiState> = MutableStateFlow(
        DrawUiState(
            members = args.group.members,
            results = args.group.draws,
            isDrawn = args.group.draws.isNotEmpty()
        )
    )
    val uiState: StateFlow<DrawUiState> = _uiState.asStateFlow()

    init {
        analytics.trackScreenView()
    }

    @AddTrace(name = "DrawViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: DrawIntent) = when (intent) {
        is DrawIntent.Share -> share(intent.secret)
        DrawIntent.Draw -> draw()
    }

    @AddTrace(name = "DrawViewModel.share", enabled = true)
    private fun share(secret: String) {
        analytics.trackShareAction()
        shareSecretFriendsUseCase(group = args.group, secret = secret).onSuccess {

        }.onFailure {

        }
    }

    @AddTrace(name = "DrawViewModel.draw", enabled = true)
    private fun draw() {
        analytics.trackDrawAction()
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

package br.com.brunocarvalhs.group.draw.app.presentation

import AnalyticsParam
import android.app.Activity
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.core.analytics.AnalyticsService
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import br.com.brunocarvalhs.core.navigation.routers.DrawGraph
import br.com.brunocarvalhs.core.review.data.InAppReviewLauncher
import br.com.brunocarvalhs.core.review.domain.ReviewPromptService
import br.com.brunocarvalhs.group.draw.app.domain.useCases.DrawUseCase
import br.com.brunocarvalhs.group.draw.app.domain.useCases.ShareSecretFriendsUseCase
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@Stable
@HiltViewModel
internal class DrawViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val shareSecretFriendsUseCase: ShareSecretFriendsUseCase,
    private val drawUseCase: DrawUseCase,
    private val analyticsService: AnalyticsService,
    private val reviewPromptService: ReviewPromptService,
    private val inAppReviewLauncher: InAppReviewLauncher
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
        analyticsService.logEvent(name = AnalyticsEvent.DRAW_STARTED)
    }

    @AddTrace(name = "DrawViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: DrawIntent) = when (intent) {
        is DrawIntent.Share -> share(intent.secret)
        DrawIntent.Draw -> draw()
        is DrawIntent.RequestReview -> requestReview(intent.activity)
    }

    @AddTrace(name = "DrawViewModel.share", enabled = true)
    private fun share(secret: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "share"
            )
        )
        shareSecretFriendsUseCase(group = args.group, secret = secret)
            .onSuccess { success() }
            .onFailure { t -> error(t) }
    }

    @AddTrace(name = "DrawViewModel.draw", enabled = true)
    private fun draw() {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "draw"
            )
        )
        viewModelScope.launch {
            drawUseCase(group = args.group).onSuccess { results ->
                analyticsService.logEvent(name = AnalyticsEvent.DRAW_COMPLETED)
                _uiState.value = _uiState.value.copy(
                    results = results,
                    isDrawn = true,
                    shouldRequestReview = reviewPromptService.shouldPrompt()
                )
            }.onFailure(::error)
        }
    }

    @AddTrace(name = "DrawViewModel.requestReview", enabled = true)
    private fun requestReview(activity: Activity) {
        if (!_uiState.value.shouldRequestReview) return
        _uiState.update { it.copy(shouldRequestReview = false) }
        viewModelScope.launch {
            reviewPromptService.recordPrompted()
            inAppReviewLauncher.launch(activity)
        }
    }

    @AddTrace(name = "DrawViewModel.success", enabled = true)
    private fun success() {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.RESULT to "success"
            )
        )
        analyticsService.logEvent(name = AnalyticsEvent.DRAW_REVEALED)
        Timber.d("Draw success")
    }

    @AddTrace(name = "DrawViewModel.error", enabled = true)
    private fun error(t: Throwable) {
        analyticsService.logEvent(
            name = AnalyticsEvent.ERROR,
            params = mapOf(
                AnalyticsParam.RESULT to t.message
            )
        )
        Timber.e(t, "Error drawing")
    }
}

package br.com.brunocarvalhs.biometric.app.presentation

import AnalyticsParam
import androidx.compose.runtime.Stable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.biometric.R
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricResult
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricUseCase
import br.com.brunocarvalhs.core.analytics.annotation.Analytics
import br.com.brunocarvalhs.core.analytics.annotation.Param
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
internal class BiometricViewModel @Inject constructor(
    private val biometricUseCase: BiometricUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(BiometricUiState())
    val state: StateFlow<BiometricUiState> = _state.asStateFlow()

    init {
        checkCanAuthenticate()
    }

    @AddTrace(name = "$CLASS.handleIntent", enabled = true)
    fun handleIntent(intent: BiometricIntent) {
        when (intent) {
            is BiometricIntent.Authenticate -> authenticate(intent.activity)
        }
    }

    @Analytics(
        event = AnalyticsEvent.VIEW,
        params = [
            Param(AnalyticsParam.FEATURE, FEATURE),
            Param(AnalyticsParam.SCREEN, "biometric"),
            Param(AnalyticsParam.ACTION, "check_can_authenticate")
        ]
    )
    @AddTrace(name = "$CLASS.checkCanAuthenticate", enabled = true)
    private fun checkCanAuthenticate() {
        val canAuthenticate = biometricUseCase.canAuthenticate()
        _state.update { it.copy(canAuthenticate = canAuthenticate) }
    }

    @Analytics(
        event = AnalyticsEvent.CLICK,
        params = [
            Param(AnalyticsParam.FEATURE, FEATURE),
            Param(AnalyticsParam.ACTION, "authenticate")
        ]
    )
    @AddTrace(name = "$CLASS.authenticate")
    private fun authenticate(activity: FragmentActivity) {
        if (!_state.value.canAuthenticate) {
            _state.update { it.copy(isAuthenticated = true) }
            return
        }

        _state.update { it.copy(isLoading = true, error = null, failedAttemptMessage = null) }
        viewModelScope.launch {
            runCatching {
                biometricUseCase.authenticate(activity).collect { result ->
                    when (result) {
                        is BiometricResult.Success -> success()
                        is BiometricResult.FailedAttempt -> failedAttempt(activity)
                        is BiometricResult.Error -> error(result.message)
                    }
                }
            }.onFailure {
                _state.update { it.copy(error = it.error, isLoading = false) }
            }
        }
    }

    @Analytics(
        event = AnalyticsEvent.SUBMIT,
        params = [
            Param(AnalyticsParam.FEATURE, FEATURE),
            Param(AnalyticsParam.RESULT, "success")
        ]
    )
    @AddTrace(name = "$CLASS.authenticate")
    private fun success() {
        _state.update {
            it.copy(
                isAuthenticated = true,
                isLoading = false,
                failedAttemptMessage = null
            )
        }
    }

    @Analytics(
        event = AnalyticsEvent.ERROR,
        params = [
            Param(AnalyticsParam.FEATURE, FEATURE),
        ]
    )
    @AddTrace(name = "$CLASS.error")
    private fun error(message: String) {
        _state.update {
            it.copy(
                error = message,
                isLoading = false,
                failedAttemptMessage = null
            )
        }
    }

    @Analytics(
        event = AnalyticsEvent.SUBMIT,
        params = [
            Param(AnalyticsParam.FEATURE, FEATURE),
            Param(AnalyticsParam.RESULT, "failed_attempt")
        ]
    )
    @AddTrace(name = "$CLASS.failedAttempt")
    private fun failedAttempt(activity: FragmentActivity) {
        _state.update {
            it.copy(
                failedAttemptMessage = activity.getString(R.string.biometric_not_found)
            )
        }
    }

    companion object {
        private const val FEATURE = "biometric"
        private const val CLASS = "BiometricViewModel"
    }
}

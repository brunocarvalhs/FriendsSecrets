package br.com.brunocarvalhs.biometric.app.presentation

import androidx.compose.runtime.Stable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.biometric.R
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricResult
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricUseCase
import br.com.brunocarvalhs.biometric.commons.analytics.BiometricAnalytics
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
    private val analytics: BiometricAnalytics
) : ViewModel() {

    private val _state = MutableStateFlow(BiometricUiState())
    val state: StateFlow<BiometricUiState> = _state.asStateFlow()

    init {
        analytics.trackScreenView()
        checkCanAuthenticate()
    }

    @AddTrace(name = "BiometricViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: BiometricIntent) {
        when (intent) {
            is BiometricIntent.Authenticate -> authenticate(intent.activity)
        }
    }

    @AddTrace(name = "BiometricViewModel.checkCanAuthenticate", enabled = true)
    private fun checkCanAuthenticate() {
        val canAuthenticate = biometricUseCase.canAuthenticate()
        _state.update { it.copy(canAuthenticate = canAuthenticate) }
    }

    @AddTrace(name = "BiometricViewModel.authenticate", enabled = true)
    private fun authenticate(activity: FragmentActivity) {
        if (!_state.value.canAuthenticate) {
            _state.update { it.copy(isAuthenticated = true) }
            return
        }

        _state.update { it.copy(isLoading = true, error = null, failedAttemptMessage = null) }
        viewModelScope.launch {
            try {
                biometricUseCase.authenticate(activity).collect { result ->
                    when (result) {
                        is BiometricResult.Success -> {
                            analytics.trackAuthenticationResult(true)
                            _state.update { it.copy(isAuthenticated = true, isLoading = false, failedAttemptMessage = null) }
                        }
                        is BiometricResult.FailedAttempt -> {
                            analytics.trackAuthenticationResult(false, "failed_attempt")
                            _state.update { it.copy(failedAttemptMessage = activity.getString(R.string.biometric_not_found)) }
                        }
                        is BiometricResult.Error -> {
                            analytics.trackAuthenticationResult(false, result.message)
                            _state.update { it.copy(error = result.message, isLoading = false, failedAttemptMessage = null) }
                        }
                    }
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}

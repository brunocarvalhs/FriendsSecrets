package br.com.brunocarvalhs.biometric.app.presentation

import androidx.compose.runtime.Stable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.biometric.R
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricResult
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricUseCase
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

    init { checkCanAuthenticate() }

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

    private fun success() {
        _state.update {
            it.copy(
                isAuthenticated = true,
                isLoading = false,
                failedAttemptMessage = null
            )
        }
    }

    private fun error(message: String) {
        _state.update {
            it.copy(
                error = message,
                isLoading = false,
                failedAttemptMessage = null
            )
        }
    }

    private fun failedAttempt(activity: FragmentActivity) {
        _state.update {
            it.copy(
                failedAttemptMessage = activity.getString(R.string.biometric_not_found)
            )
        }
    }
}

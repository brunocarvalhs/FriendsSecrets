package br.com.brunocarvalhs.biometric.app.presentation

import androidx.compose.runtime.Stable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricResult
import br.com.brunocarvalhs.biometric.app.domain.useCases.BiometricUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class BiometricViewModel @Inject constructor(
    private val biometricUseCase: BiometricUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BiometricUiState())
    val state: StateFlow<BiometricUiState> = _state.asStateFlow()

    init {
        checkCanAuthenticate()
    }

    fun handleIntent(intent: BiometricIntent) {
        when (intent) {
            is BiometricIntent.Authenticate -> authenticate(intent.activity)
        }
    }

    private fun checkCanAuthenticate() {
        val canAuthenticate = biometricUseCase.canAuthenticate()
        _state.update { it.copy(canAuthenticate = canAuthenticate) }
    }

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
                            _state.update { it.copy(isAuthenticated = true, isLoading = false, failedAttemptMessage = null) }
                        }
                        is BiometricResult.FailedAttempt -> {
                            _state.update { it.copy(failedAttemptMessage = "Digital não reconhecida. Tente novamente.") }
                        }
                        is BiometricResult.Error -> {
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

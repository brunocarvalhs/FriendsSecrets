package br.com.brunocarvalhs.settings.app.list

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.services.BiometricService
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val biometricService: BiometricService,
    private val analytics: SettingsAnalytics,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        analytics.trackScreenView()
        checkBiometricSupport()
        observeBiometricStatus()
    }

    @AddTrace(name = "SettingsViewModel.checkBiometricSupport", enabled = true)
    private fun checkBiometricSupport() {
        val isSupported = biometricService.canAuthenticate()
        _state.update { it.copy(isBiometricSupported = isSupported) }
    }

    @AddTrace(name = "SettingsViewModel.observeBiometricStatus", enabled = true)
    private fun observeBiometricStatus() {
        viewModelScope.launch {
            biometricService.isBiometricPromptEnabled.collect { isEnabled ->
                _state.update { it.copy(isBiometricPromptEnabled = isEnabled) }
            }
        }
    }

    @AddTrace(name = "SettingsViewModel.handleIntent", enabled = true)
    fun handleIntent(event: SettingsIntent) {
        when (event) {
            is SettingsIntent.SetBiometricPromptEnabled -> setBiometricPromptEnabled(event.state)
        }
    }

    @AddTrace(name = "SettingsViewModel.setBiometricPromptEnabled", enabled = true)
    private fun setBiometricPromptEnabled(enabled: Boolean) {
        analytics.trackToggleBiometric(enabled)
        viewModelScope.launch {
            biometricService.setBiometricPromptEnabled(enabled)
        }
    }
}
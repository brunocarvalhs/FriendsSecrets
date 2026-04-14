package br.com.brunocarvalhs.settings.app.list

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.services.BiometricService
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
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        checkBiometricSupport()
        observeBiometricStatus()
    }

    private fun checkBiometricSupport() {
        val isSupported = biometricService.canAuthenticate()
        _state.update { it.copy(isBiometricSupported = isSupported) }
    }

    private fun observeBiometricStatus() {
        viewModelScope.launch {
            biometricService.isBiometricPromptEnabled.collect { isEnabled ->
                _state.update { it.copy(isBiometricPromptEnabled = isEnabled) }
            }
        }
    }

    fun handleIntent(event: SettingsIntent) {
        when (event) {
            is SettingsIntent.SetBiometricPromptEnabled -> setBiometricPromptEnabled(event.state)
        }
    }

    private fun setBiometricPromptEnabled(enabled: Boolean) {
        viewModelScope.launch {
            biometricService.setBiometricPromptEnabled(enabled)
        }
    }
}
package br.com.brunocarvalhs.settings.app.list

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.services.BiometricService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val biometricManager: BiometricService
) : ViewModel() {

    private val _state = MutableStateFlow(
        SettingsState(
            isBiometricPromptEnabled = biometricManager.isBiometricPromptEnabled()
        )
    )
    val state = _state.asStateFlow()

    fun handleIntent(event: SettingsIntent) {
        when (event) {
            is SettingsIntent.SetBiometricPromptEnabled -> setBiometricPromptEnabled(event.state)
        }
    }

    private fun setBiometricPromptEnabled(state: Boolean) {
        viewModelScope.launch {
            biometricManager.setBiometricPromptEnabled(state)
            _state.value = _state.value.copy(isBiometricPromptEnabled = state)
        }
    }
}
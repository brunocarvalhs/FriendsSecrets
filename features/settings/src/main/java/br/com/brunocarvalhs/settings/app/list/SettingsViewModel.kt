package br.com.brunocarvalhs.settings.app.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.common.security.BiometricManager
import br.com.brunocarvalhs.friendssecrets.common.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val toggleManager: ToggleManager,
    private val themeManager: ThemeManager,
    private val biometricManager: BiometricManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    fun onEvent(event: SettingsIntent) {
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
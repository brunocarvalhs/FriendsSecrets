package br.com.brunocarvalhs.settings.app.appearence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.common.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppearanceViewModel @Inject constructor(
    private val themeManager: ThemeManager,
) : ViewModel() {

    private val _state = MutableStateFlow(
        AppearanceState(
            themeSelected = themeManager.theme.value.type,
            isDynamicThemeEnabled = themeManager.isDynamicThemeEnabled.value
        )
    )
    val state: StateFlow<AppearanceState> = _state.asStateFlow()

    fun onEvent(intent: AppearanceIntent) {
        when (intent) {
            is AppearanceIntent.SetTheme -> setTheme(intent.theme)
            is AppearanceIntent.SetDynamicThemeEnabled -> setDynamicThemeEnabled(intent.enabled)
        }
    }

    private fun setTheme(theme: String) {
        viewModelScope.launch {
            themeManager.setTheme(ThemeManager.Theme.valueOf(theme.uppercase()))
            _state.update { it.copy(themeSelected = theme) }
        }
    }

    private fun setDynamicThemeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            themeManager.setDynamicThemeEnabled(enabled)
            _state.update { it.copy(isDynamicThemeEnabled = enabled) }
        }
    }
}
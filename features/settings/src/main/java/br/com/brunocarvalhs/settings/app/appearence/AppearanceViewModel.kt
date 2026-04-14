package br.com.brunocarvalhs.settings.app.appearence

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.domain.services.ThemeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
internal class AppearanceViewModel @Inject constructor(
    private val themeService: ThemeService,
) : ViewModel() {

    private val _state = MutableStateFlow(
        value = AppearanceState(
            themeSelected = themeService.theme.value.type,
            isDynamicThemeEnabled = themeService.isDynamicThemeEnabled.value
        )
    )
    val state: StateFlow<AppearanceState> = _state.asStateFlow()

    fun handleIntent(intent: AppearanceIntent) {
        when (intent) {
            is AppearanceIntent.SetTheme -> setTheme(intent.theme)
            is AppearanceIntent.SetDynamicThemeEnabled -> setDynamicThemeEnabled(intent.enabled)
        }
    }

    private fun setTheme(theme: String) {
        viewModelScope.launch {
            themeService.setTheme(ThemeService.Theme.valueOf(theme.uppercase()))
            _state.update { it.copy(themeSelected = theme) }
        }
    }

    private fun setDynamicThemeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            themeService.setDynamicThemeEnabled(enabled)
            _state.update { it.copy(isDynamicThemeEnabled = enabled) }
        }
    }
}
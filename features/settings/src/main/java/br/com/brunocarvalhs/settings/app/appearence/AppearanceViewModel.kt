package br.com.brunocarvalhs.settings.app.appearence

import AnalyticsParam
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.core.analytics.AnalyticsService
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import br.com.brunocarvalhs.core.remote.domain.ThemeService
import br.com.brunocarvalhs.core.ui.theme.AppPalette
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
internal class AppearanceViewModel @Inject constructor(
    private val themeService: ThemeService,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val _state = MutableStateFlow(
        value = AppearanceState(
            themeSelected = themeService.theme.value.type,
            isDynamicThemeEnabled = themeService.isDynamicThemeEnabled.value,
            paletteSelected = themeService.palette.value,
            customPrimaryColor = themeService.customPrimaryColor.value,
            customSecondaryColor = themeService.customSecondaryColor.value
        )
    )
    val state: StateFlow<AppearanceState> = _state.asStateFlow()

    init {
        initializer()
    }

    @AddTrace(name = "AppearanceViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: AppearanceIntent) {
        when (intent) {
            is AppearanceIntent.SetTheme -> setTheme(intent.theme)
            is AppearanceIntent.SetDynamicThemeEnabled -> setDynamicThemeEnabled(intent.enabled)
            is AppearanceIntent.SetPalette -> setPalette(intent.paletteId)
            is AppearanceIntent.SetCustomColors ->
                setCustomColors(intent.primaryColor, intent.secondaryColor)
            is AppearanceIntent.SetCustomThemeEnabled -> setCustomThemeEnabled(intent.enabled)
        }
    }

    @AddTrace(name = "AppearanceViewModel.initializer", enabled = true)
    private fun initializer() {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "initializer"
            )
        )
        viewModelScope.launch { themeService.initialize() }
    }

    @AddTrace(name = "AppearanceViewModel.setTheme", enabled = true)
    private fun setTheme(theme: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.CLICK,
            params = mapOf(
                AnalyticsParam.ACTION to "set_theme",
                AnalyticsParam.PARAM to theme
            )
        )
        viewModelScope.launch {
            themeService.setTheme(ThemeService.Theme.valueOf(theme.uppercase()))
            _state.update { it.copy(themeSelected = theme) }
        }
    }

    @AddTrace(name = "AppearanceViewModel.setDynamicThemeEnabled", enabled = true)
    private fun setDynamicThemeEnabled(enabled: Boolean) {
        analyticsService.logEvent(
            name = AnalyticsEvent.CLICK,
            params = mapOf(
                AnalyticsParam.ACTION to "set_dynamic_theme_enabled",
                AnalyticsParam.PARAM to enabled.toString()
            )
        )
        viewModelScope.launch {
            themeService.setDynamicThemeEnabled(enabled)
            val turnOffCustomTheme = enabled && _state.value.paletteSelected == AppPalette.CUSTOM.id
            if (turnOffCustomTheme) {
                themeService.setPalette(AppPalette.Default.id)
            }
            _state.update {
                it.copy(
                    isDynamicThemeEnabled = enabled,
                    paletteSelected = if (turnOffCustomTheme) AppPalette.Default.id else it.paletteSelected
                )
            }
        }
    }

    @AddTrace(name = "AppearanceViewModel.setPalette", enabled = true)
    private fun setPalette(paletteId: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.CLICK,
            params = mapOf(
                AnalyticsParam.ACTION to "set_palette",
                AnalyticsParam.PARAM to paletteId
            )
        )
        viewModelScope.launch {
            themeService.setPalette(paletteId)
            _state.update { it.copy(paletteSelected = paletteId) }
        }
    }

    @AddTrace(name = "AppearanceViewModel.setCustomColors", enabled = true)
    private fun setCustomColors(primaryColor: Int, secondaryColor: Int) {
        analyticsService.logEvent(
            name = AnalyticsEvent.CLICK,
            params = mapOf(
                AnalyticsParam.ACTION to "set_custom_colors"
            )
        )
        viewModelScope.launch {
            themeService.setCustomColors(primaryColor, secondaryColor)
            _state.update {
                it.copy(customPrimaryColor = primaryColor, customSecondaryColor = secondaryColor)
            }
        }
    }

    @AddTrace(name = "AppearanceViewModel.setCustomThemeEnabled", enabled = true)
    private fun setCustomThemeEnabled(enabled: Boolean) {
        analyticsService.logEvent(
            name = AnalyticsEvent.CLICK,
            params = mapOf(
                AnalyticsParam.ACTION to "set_custom_theme_enabled",
                AnalyticsParam.PARAM to enabled.toString()
            )
        )
        val paletteId = if (enabled) AppPalette.CUSTOM.id else AppPalette.Default.id
        viewModelScope.launch {
            themeService.setPalette(paletteId)
            if (enabled && _state.value.isDynamicThemeEnabled) {
                themeService.setDynamicThemeEnabled(false)
            }
            _state.update {
                it.copy(
                    paletteSelected = paletteId,
                    isDynamicThemeEnabled = if (enabled) false else it.isDynamicThemeEnabled
                )
            }
        }
    }
}

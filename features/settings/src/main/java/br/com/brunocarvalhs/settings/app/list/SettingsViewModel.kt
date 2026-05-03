package br.com.brunocarvalhs.settings.app.list

import AnalyticsParam
import android.content.Context
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.biometric.BiometricService
import br.com.brunocarvalhs.core.analytics.AnalyticsService
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val biometricService: BiometricService,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        checkBiometricSupport()
        observeBiometricStatus()
        loadVersionInfo()
    }

    private fun loadVersionInfo() {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionName = packageInfo.versionName ?: ""
        val versionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toString()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toString()
        }
        _state.update { it.copy(versionName = versionName, versionCode = versionCode) }
    }

    @AddTrace(name = "SettingsViewModel.checkBiometricSupport", enabled = true)
    private fun checkBiometricSupport() {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "check_biometric_support"
            )
        )
        val isSupported = biometricService.canAuthenticate()
        _state.update { it.copy(isBiometricSupported = isSupported) }
    }

    @AddTrace(name = "SettingsViewModel.observeBiometricStatus", enabled = true)
    private fun observeBiometricStatus() {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "observe_biometric_status"
            )
        )
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
        analyticsService.logEvent(
            name = AnalyticsEvent.CLICK,
            params = mapOf(
                AnalyticsParam.ACTION to "set_biometric_prompt_enabled",
                AnalyticsParam.PARAM to enabled.toString()
            )
        )
        viewModelScope.launch {
            biometricService.setBiometricPromptEnabled(enabled)
        }
    }
}

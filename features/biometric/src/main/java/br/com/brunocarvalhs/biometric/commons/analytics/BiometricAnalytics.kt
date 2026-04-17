package br.com.brunocarvalhs.biometric.commons.analytics

internal interface BiometricAnalytics {
    fun trackScreenView()
    fun trackAuthenticationResult(success: Boolean, error: String? = null)
}


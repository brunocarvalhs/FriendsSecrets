package br.com.brunocarvalhs.biometric.commons.analytics

interface BiometricAnalytics {
    fun trackScreenView()
    fun trackAuthenticationResult(success: Boolean, error: String? = null)
}


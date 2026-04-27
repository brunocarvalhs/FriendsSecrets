package br.com.brunocarvalhs.biometric.app.presentation

internal data class BiometricUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val failedAttemptMessage: String? = null,
    val isAuthenticated: Boolean = false,
    val canAuthenticate: Boolean = true
)

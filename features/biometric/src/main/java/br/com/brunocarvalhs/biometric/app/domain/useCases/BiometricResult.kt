package br.com.brunocarvalhs.biometric.app.domain.useCases

sealed class BiometricResult {
    object Success : BiometricResult()
    object FailedAttempt : BiometricResult()
    data class Error(val code: Int, val message: String) : BiometricResult()
}

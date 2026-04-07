package br.com.brunocarvalhs.friendssecrets.domain.services

interface BiometricService {
    fun isBiometricPromptEnabled(): Boolean
    suspend fun setBiometricPromptEnabled(state: Boolean)
}
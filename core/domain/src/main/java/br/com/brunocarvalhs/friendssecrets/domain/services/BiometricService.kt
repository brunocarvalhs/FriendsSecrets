package br.com.brunocarvalhs.friendssecrets.domain.services

import kotlinx.coroutines.flow.StateFlow

interface BiometricService {
    val isBiometricSupported: StateFlow<Boolean>
    val isBiometricPromptEnabled: StateFlow<Boolean>
    suspend fun setBiometricPromptEnabled(state: Boolean)
    fun canAuthenticate(): Boolean
    fun getAuthenticatorTypes(): Int
}
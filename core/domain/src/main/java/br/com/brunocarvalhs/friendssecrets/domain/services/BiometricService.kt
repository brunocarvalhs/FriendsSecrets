package br.com.brunocarvalhs.friendssecrets.domain.services

import kotlinx.coroutines.flow.StateFlow

interface BiometricService {
    val isBiometricPromptEnabled: StateFlow<Boolean>
    suspend fun setBiometricPromptEnabled(state: Boolean)
}
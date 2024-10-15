package br.com.brunocarvalhs.friendssecrets.commons.security

import br.com.brunocarvalhs.friendssecrets.data.service.StorageService

object BiometricManager {

    private const val BIOMETRIC_KEY = "biometric_key"
    private val storage: StorageService = StorageService()
    private var isBiometricPromptEnabled: Boolean = storage.load<Boolean>(BIOMETRIC_KEY) ?: false

    fun isBiometricPromptEnabled(): Boolean {
        return isBiometricPromptEnabled
    }

    fun setBiometricPromptEnabled(enabled: Boolean) {
        storage.save(BIOMETRIC_KEY, enabled)
        isBiometricPromptEnabled = enabled
    }

}
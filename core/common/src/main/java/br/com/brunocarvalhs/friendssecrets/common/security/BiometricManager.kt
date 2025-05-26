package br.com.brunocarvalhs.friendssecrets.common.security

import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager

class BiometricManager(
    private val storage: StorageManager
) {

    private var isBiometricPromptEnabled: Boolean =
        storage.load(BIOMETRIC_KEY, Boolean::class.java) ?: false

    fun isBiometricPromptEnabled(): Boolean {
        return isBiometricPromptEnabled
    }

    fun setBiometricPromptEnabled(enabled: Boolean) {
        storage.save(BIOMETRIC_KEY, enabled)
        isBiometricPromptEnabled = enabled
    }

    companion object {
        private const val BIOMETRIC_KEY = "biometric_key"
    }
}
package br.com.brunocarvalhs.friendssecrets.common.security

import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager

class BiometricManager(
    private val storage: StorageManager
) {

    suspend fun isBiometricPromptEnabled(): Boolean {
        return storage.load(key = BIOMETRIC_KEY, clazz = Boolean::class.java) ?: false
    }

    suspend fun setBiometricPromptEnabled(enabled: Boolean) {
        storage.save(BIOMETRIC_KEY, enabled)
    }

    companion object {
        private const val BIOMETRIC_KEY = "biometric_key"
    }
}
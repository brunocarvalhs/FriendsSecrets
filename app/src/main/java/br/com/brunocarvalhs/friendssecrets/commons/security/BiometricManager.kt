package br.com.brunocarvalhs.friendssecrets.commons.security

import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import com.google.firebase.perf.metrics.AddTrace

object BiometricManager {

    private const val BIOMETRIC_KEY = "biometric_key"
    private val storage: StorageService = StorageService()
    private var isBiometricPromptEnabled: Boolean = storage.load<Boolean>(BIOMETRIC_KEY) ?: false

    /**
     * This method is used to check if the biometric prompt is enabled or not.
     * @return true if the biometric prompt is enabled, false otherwise.
     */
    @JvmName("isBiometricPromptEnabled")
    @AddTrace(name = "BiometricManager.isBiometricPromptEnabled")
    fun isBiometricPromptEnabled(): Boolean {
        return isBiometricPromptEnabled
    }

    /**
     * This method is used to set the biometric prompt enabled or not.
     * @param enabled true if the biometric prompt is enabled, false otherwise.
     */
    @JvmName("setBiometricPromptEnabled")
    @AddTrace(name = "BiometricManager.setBiometricPromptEnabled")
    fun setBiometricPromptEnabled(enabled: Boolean) {
        storage.save(BIOMETRIC_KEY, enabled)
        isBiometricPromptEnabled = enabled
    }

}
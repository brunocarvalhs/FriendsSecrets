package br.com.brunocarvalhs.friendssecrets.common.security

import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BiometricManager(
    private val storage: StorageManager
) {
    private val _isBiometricPromptEnabled = MutableStateFlow(false)
    private val isBiometricPromptEnabled: StateFlow<Boolean> = _isBiometricPromptEnabled

    init {
        CoroutineScope(Dispatchers.IO).launch {
            init()
        }
    }

    private suspend fun init() {
        val biometric = storage.load(key = BIOMETRIC_KEY, clazz = Boolean::class.java) ?: false
        _isBiometricPromptEnabled.value = biometric
    }

    fun isBiometricPromptEnabled(): Boolean {
        return isBiometricPromptEnabled.value
    }

    suspend fun setBiometricPromptEnabled(enabled: Boolean) {
        storage.save(BIOMETRIC_KEY, enabled)
        _isBiometricPromptEnabled.value = enabled
    }

    companion object {
        private const val BIOMETRIC_KEY = "biometric_key"
    }
}
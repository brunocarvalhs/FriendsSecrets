package br.com.brunocarvalhs.friendssecrets.common.security

import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BiometricManager(
    private val storage: StorageManager,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val scope = CoroutineScope(dispatcher)
    private val _isBiometricPromptEnabled = MutableStateFlow(false)
    private val isBiometricPromptEnabled: StateFlow<Boolean> = _isBiometricPromptEnabled

    init {
        scope.launch {
            init()
        }
    }

    private suspend fun init() {
        val biometric = storage.load(key = BIOMETRIC_KEY, clazz = Boolean::class.java) ?: false
        _isBiometricPromptEnabled.value = biometric
    }

    fun isBiometricPromptEnabled(): Boolean = _isBiometricPromptEnabled.value

    suspend fun setBiometricPromptEnabled(enabled: Boolean) {
        storage.save(BIOMETRIC_KEY, enabled)
        _isBiometricPromptEnabled.value = enabled
    }

    companion object {
        const val BIOMETRIC_KEY = "biometric_key"
    }
}
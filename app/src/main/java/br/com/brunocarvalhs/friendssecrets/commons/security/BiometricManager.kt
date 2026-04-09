package br.com.brunocarvalhs.friendssecrets.commons.security

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import br.com.brunocarvalhs.friendssecrets.domain.services.BiometricService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BiometricManager @Inject constructor(
    private val storage: StorageService,
): BiometricService {
    private val _isBiometricPromptEnabled = MutableStateFlow(false)

    init {
        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.Default) {
            init()
        }
    }

    private suspend fun init() {
        val biometric = storage.load(key = BIOMETRIC_KEY, value = Boolean::class) ?: false
        _isBiometricPromptEnabled.value = biometric
    }

    override fun isBiometricPromptEnabled(): Boolean = _isBiometricPromptEnabled.value

    override suspend fun setBiometricPromptEnabled(state: Boolean) {
        storage.save(BIOMETRIC_KEY, state)
        _isBiometricPromptEnabled.value = state
    }

    companion object {
        const val BIOMETRIC_KEY = "biometric_key"
    }
}
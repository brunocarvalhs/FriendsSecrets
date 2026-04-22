package br.com.brunocarvalhs.biometric

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BiometricManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val storage: StorageService,
) : BiometricService {

    private val _isBiometricSupported = MutableStateFlow(canAuthenticate())
    override val isBiometricSupported: StateFlow<Boolean> = _isBiometricSupported.asStateFlow()

    private val _isBiometricPromptEnabled = MutableStateFlow(false)
    override val isBiometricPromptEnabled: StateFlow<Boolean> = _isBiometricPromptEnabled.asStateFlow()

    init {
        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.Default) {
            init()
        }
    }

    private suspend fun init() {
        val isSupported = canAuthenticate()
        _isBiometricSupported.value = isSupported

        val biometricSaved = storage.load(key = BIOMETRIC_KEY, value = Boolean::class) ?: false
        _isBiometricPromptEnabled.value = biometricSaved && isSupported
    }

    override suspend fun setBiometricPromptEnabled(state: Boolean) {
        val isSupported = canAuthenticate()
        _isBiometricSupported.value = isSupported

        storage.save(BIOMETRIC_KEY, state)
        _isBiometricPromptEnabled.value = state && isSupported
    }

    override fun canAuthenticate(): Boolean {
        val biometricManager = androidx.biometric.BiometricManager.from(context)
        return biometricManager
            .canAuthenticate(getAuthenticatorTypes()) == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
    }

    override fun getAuthenticatorTypes(): Int =
        BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL

    companion object {
        const val BIOMETRIC_KEY = "biometric_key"
    }
}
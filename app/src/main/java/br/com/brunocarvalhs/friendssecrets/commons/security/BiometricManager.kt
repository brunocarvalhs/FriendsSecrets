package br.com.brunocarvalhs.friendssecrets.commons.security

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BiometricManager(
    @param:ApplicationContext private val context: Context,
    activity: ComponentActivity,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _isBiometricPromptEnabled = MutableStateFlow(false)
    private val isBiometricPromptEnabled: StateFlow<Boolean> = _isBiometricPromptEnabled

    init {
        activity.lifecycleScope.launch(dispatcher) {
            init()
        }
    }

    private suspend fun init() {
//        val biometric = storage.load(key = BIOMETRIC_KEY, clazz = Boolean::class.java) ?: false
//        _isBiometricPromptEnabled.value = biometric
    }

    fun isBiometricPromptEnabled(): Boolean = _isBiometricPromptEnabled.value

    suspend fun setBiometricPromptEnabled(enabled: Boolean) {
//        storage.save(BIOMETRIC_KEY, enabled)
//        _isBiometricPromptEnabled.value = enabled
    }

    companion object {
        const val BIOMETRIC_KEY = "biometric_key"
    }
}
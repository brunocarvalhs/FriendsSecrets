package br.com.brunocarvalhs.biometric.app.domain.useCases

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import br.com.brunocarvalhs.biometric.BiometricService
import br.com.brunocarvalhs.biometric.R
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BiometricUseCase @Inject constructor(
    private val biometricManager: BiometricService
) {

    @AddTrace(name = "BiometricUseCase.canAuthenticate", enabled = true)
    fun canAuthenticate(): Boolean {
        return biometricManager.canAuthenticate()
    }

    @AddTrace(name = "BiometricUseCase.authenticate", enabled = true)
    fun authenticate(activity: FragmentActivity): Flow<BiometricResult> = callbackFlow {
        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    trySend(BiometricResult.Error(errorCode, errString.toString()))
                    close()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    trySend(BiometricResult.Success)
                    close()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    trySend(BiometricResult.FailedAttempt)
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getString(R.string.title_biometric))
            .setSubtitle(activity.getString(R.string.subtitle_biometric))
            .setAllowedAuthenticators(biometricManager.getAuthenticatorTypes())
            .build()

        biometricPrompt.authenticate(promptInfo)

        awaitClose {
            biometricPrompt.cancelAuthentication()
        }
    }
}

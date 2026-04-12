package br.com.brunocarvalhs.biometric.app.domain.useCases

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import br.com.brunocarvalhs.friendssecrets.domain.services.BiometricService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BiometricUseCase @Inject constructor(
    private val biometricManager: BiometricService
) {

    fun canAuthenticate(): Boolean {
        return biometricManager.canAuthenticate()
    }

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
            .setTitle("Autenticação Segura")
            .setSubtitle("Acesse usando biometria ou a senha do dispositivo")
            .setAllowedAuthenticators(biometricManager.getAuthenticatorTypes())
            .build()

        biometricPrompt.authenticate(promptInfo)

        awaitClose {
            biometricPrompt.cancelAuthentication()
        }
    }
}

package br.com.brunocarvalhs.biometric.app.domain.useCases

import br.com.brunocarvalhs.biometric.BiometricService
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class IsBiometricEnabledUseCase @Inject constructor(
    private val service: BiometricService
) {
    @AddTrace(name = "IsBiometricEnabledUseCase.invoke", enabled = true)
    operator fun invoke(): StateFlow<Boolean> = service.isBiometricPromptEnabled
}

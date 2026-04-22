package br.com.brunocarvalhs.biometric.app.domain.useCases

import br.com.brunocarvalhs.biometric.BiometricService
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class IsBiometricEnabledUseCase @Inject constructor(
    private val service: BiometricService
) {
    operator fun invoke(): StateFlow<Boolean> = service.isBiometricPromptEnabled
}

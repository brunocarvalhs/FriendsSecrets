package br.com.brunocarvalhs.biometric.commons.flags

import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.FeatureFlagService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BiometricFeatureFlags @Inject constructor(
    private val service: FeatureFlagService
) {
    fun isBiometric(): Boolean = service.validate(FEATURE_BIOMETRIC, true)

    companion object {
        private const val FEATURE_BIOMETRIC = "feature_biometric_enabled"
    }
}

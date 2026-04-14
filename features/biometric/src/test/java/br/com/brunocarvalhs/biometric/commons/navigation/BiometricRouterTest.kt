package br.com.brunocarvalhs.biometric.commons.navigation

import org.junit.Assert.assertNotNull
import org.junit.Test

class BiometricRouterTest {

    @Test
    fun `router should be serializable`() {
        assertNotNull(BiometricRouter)
    }
}

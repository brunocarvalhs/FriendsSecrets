package br.com.brunocarvalhs.friendssecrets.common.security

import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BiometricManagerTest {

    private lateinit var storage: StorageManager
    private lateinit var biometricManager: BiometricManager

    @Before
    fun setUp() {
        storage = mockk()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should return false when biometric prompt is not enabled`() {
        every { storage.load<Boolean>("biometric_key") } returns null

        biometricManager = BiometricManager(storage)

        assertFalse(biometricManager.isBiometricPromptEnabled())
        verify { storage.load<Boolean>("biometric_key") }
    }

    @Test
    fun `should return true when biometric prompt is enabled`() {
        every { storage.load<Boolean>("biometric_key") } returns true

        biometricManager = BiometricManager(storage)

        assertTrue(biometricManager.isBiometricPromptEnabled())
        verify { storage.load<Boolean>("biometric_key") }
    }

    @Test
    fun `should save biometric preference and update internal state`() {
        every { storage.load<Boolean>("biometric_key") } returns false
        every { storage.save("biometric_key", true) } just Runs

        biometricManager = BiometricManager(storage)

        biometricManager.setBiometricPromptEnabled(true)

        assertTrue(biometricManager.isBiometricPromptEnabled())
        verify { storage.save("biometric_key", true) }
    }

    @Test
    fun `should save false and update internal state`() {
        every { storage.load<Boolean>("biometric_key") } returns true
        every { storage.save("biometric_key", false) } just Runs

        biometricManager = BiometricManager(storage)

        biometricManager.setBiometricPromptEnabled(false)

        assertFalse(biometricManager.isBiometricPromptEnabled())
        verify { storage.save("biometric_key", false) }
    }
}

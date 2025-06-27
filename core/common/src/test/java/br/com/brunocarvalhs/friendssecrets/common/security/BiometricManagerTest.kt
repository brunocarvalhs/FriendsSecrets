package br.com.brunocarvalhs.friendssecrets.common.security

import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
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
    fun `should return false when biometric prompt is not enabled`() = runTest {
        coEvery { storage.load("biometric_key", Boolean::class.java) } returns null

        biometricManager = BiometricManager(storage)

        assertFalse(biometricManager.isBiometricPromptEnabled())
        coVerify { storage.load("biometric_key", Boolean::class.java) }
    }

    @Test
    fun `should return true when biometric prompt is enabled`() = runTest {
        coEvery { storage.load("biometric_key", Boolean::class.java) } returns true

        biometricManager = BiometricManager(storage)

        assertTrue(biometricManager.isBiometricPromptEnabled())
        coVerify { storage.load("biometric_key", Boolean::class.java) }
    }

    @Test
    fun `should save biometric preference and update internal state`() = runTest {
        coEvery { storage.load("biometric_key", Boolean::class.java) } returns false
        coEvery { storage.save("biometric_key", true) } just Runs

        biometricManager = BiometricManager(storage)

        biometricManager.setBiometricPromptEnabled(true)

        assertTrue(biometricManager.isBiometricPromptEnabled())
        coVerify { storage.save("biometric_key", true) }
    }

    @Test
    fun `should save false and update internal state`() = runTest {
        coEvery { storage.load("biometric_key", Boolean::class.java) } returns true
        coEvery { storage.save("biometric_key", false) } just Runs

        biometricManager = BiometricManager(storage)

        biometricManager.setBiometricPromptEnabled(false)

        assertFalse(biometricManager.isBiometricPromptEnabled())
        coVerify { storage.save("biometric_key", false) }
    }
}

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
import kotlinx.coroutines.test.StandardTestDispatcher
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
        coEvery { storage.load(BiometricManager.BIOMETRIC_KEY, Boolean::class.java) } returns null

        biometricManager = BiometricManager(storage)

        assertFalse(biometricManager.isBiometricPromptEnabled())
        coVerify { storage.load(BiometricManager.BIOMETRIC_KEY, Boolean::class.java) }
    }

    @Test
    fun `should return true when biometric prompt is enabled`() = runTest {
        coEvery { storage.load(BiometricManager.BIOMETRIC_KEY, Boolean::class.java) } returns true

        biometricManager = BiometricManager(storage, dispatcher = StandardTestDispatcher(testScheduler))
        testScheduler.advanceUntilIdle()

        assertTrue(biometricManager.isBiometricPromptEnabled())
        coVerify { storage.load(BiometricManager.BIOMETRIC_KEY, Boolean::class.java) }
    }


    @Test
    fun `should save biometric preference and update internal state`() = runTest {
        coEvery { storage.load(BiometricManager.BIOMETRIC_KEY, Boolean::class.java) } returns false
        coEvery { storage.save(BiometricManager.BIOMETRIC_KEY, true) } just Runs

        biometricManager = BiometricManager(storage)

        biometricManager.setBiometricPromptEnabled(true)

        assertTrue(biometricManager.isBiometricPromptEnabled())
        coVerify { storage.save(BiometricManager.BIOMETRIC_KEY, true) }
    }

    @Test
    fun `should save false and update internal state`() = runTest {
        coEvery { storage.load(BiometricManager.BIOMETRIC_KEY, Boolean::class.java) } returns true
        coEvery { storage.save(BiometricManager.BIOMETRIC_KEY, false) } just Runs

        biometricManager = BiometricManager(storage)

        biometricManager.setBiometricPromptEnabled(false)

        assertFalse(biometricManager.isBiometricPromptEnabled())
        coVerify { storage.save(BiometricManager.BIOMETRIC_KEY, false) }
    }
}

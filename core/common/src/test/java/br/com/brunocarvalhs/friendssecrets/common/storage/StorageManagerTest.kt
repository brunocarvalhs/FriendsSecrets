package br.com.brunocarvalhs.friendssecrets.common.storage

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class StorageManagerTest {

    private lateinit var storageEvent: StorageManager.StorageEvent
    private lateinit var storageManager: StorageManager

    @Before
    fun setUp() {
        storageEvent = mockk(relaxed = true)
        storageManager = StorageManager(storageEvent)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should save value`() = runTest {
        val key = "theme_key"
        val value = "DARK"

        storageManager.save(key, value)

        coVerify { storageEvent.save(eq(key), eq(value)) }
    }

    @Test
    fun `should load value`() = runTest {
        val key = "theme_key"
        val value = "LIGHT"

        coEvery { storageEvent.load(key, String::class.java) } returns value

        val result = storageManager.load(key, String::class.java)

        assertEquals(value, result)
        coVerify { storageEvent.load(key, String::class.java) }
    }

    @Test
    fun `should return null when value not found`() = runTest {
        val key = "non_existing_key"

        coEvery { storageEvent.load(key, String::class.java) } returns null

        val result = storageManager.load(key, String::class.java)

        assertNull(result)
        coVerify { storageEvent.load(key, String::class.java) }
    }

    @Test
    fun `should remove value`() = runTest {
        val key = "theme_key"

        storageManager.remove(key)

        coVerify { storageEvent.remove(eq(key)) }
    }
}

package br.com.brunocarvalhs.friendssecrets.common.storage

import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
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
    fun `should save value`() {
        val key = "theme_key"
        val value = "DARK"

        storageManager.save(key, value)

        verify { storageEvent.save(eq(key), eq(value)) }
    }

    @Test
    fun `should load value`() {
        val key = "theme_key"
        val value = "LIGHT"

        every { storageEvent.load<String>(key) } returns value

        val result = storageManager.load<String>(key)

        assertEquals(value, result)
        verify { storageEvent.load<String>(key) }
    }

    @Test
    fun `should return null when value not found`() {
        val key = "non_existing_key"

        every { storageEvent.load<String>(key) } returns null

        val result = storageManager.load<String>(key)

        assertNull(result)
        verify { storageEvent.load<String>(key) }
    }

    @Test
    fun `should remove value`() {
        val key = "theme_key"

        storageManager.remove(key)

        verify { storageEvent.remove(eq(key)) }
    }
}

package br.com.brunocarvalhs.friendssecrets.data.service

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class StorageServiceTest {

    private lateinit var storageService: StorageService
    private lateinit var mockSharedPreferences: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var mockApplication: Application

    @Before
    fun setup() {
        mockApplication = mockk(relaxed = true)
        mockSharedPreferences = mockk(relaxed = true)
        mockEditor = mockk(relaxed = true)

        every { mockApplication.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockSharedPreferences.edit() } returns mockEditor

        storageService = StorageService(context = mockApplication)
    }

    @Test
    fun `test save method`() {
        val key = "test_key"
        val value = "test_value"

        storageService.save(key, value)

        verify { mockEditor.putString(key, Gson().toJson(value)) }
    }

    @Test
    fun `test load method with existing key`() {
        val key = "test_key"
        val value = "test_value"
        val jsonValue = Gson().toJson(value)

        every { mockSharedPreferences.getString(key, null) } returns jsonValue

        val loadedValue: String? = storageService.load(key)

        assertEquals(value, loadedValue)
    }

    @Test
    fun `test load method with non-existing key`() {
        val key = "non_existing_key"

        every { mockSharedPreferences.getString(key, null) } returns null

        val loadedValue: String? = storageService.load(key)

        assertEquals(null, loadedValue)
    }

    @Test
    fun `test remove method`() {
        val key = "test_key"

        storageService.remove(key)

        verify { mockEditor.remove(key) }
    }

    @Test
    fun `test clear method`() {
        storageService.clear()

        verify { mockEditor.clear() }
    }

    @Test
    fun `test contains method with existing key`() {
        val key = "test_key"

        every { mockSharedPreferences.contains(key) } returns true

        val contains = storageService.contains(key)

        assertTrue(contains)
    }

    @Test
    fun `test contains method with non-existing key`() {
        val key = "non_existing_key"

        every { mockSharedPreferences.contains(key) } returns false

        val contains = storageService.contains(key)

        assertFalse(contains)
    }
}

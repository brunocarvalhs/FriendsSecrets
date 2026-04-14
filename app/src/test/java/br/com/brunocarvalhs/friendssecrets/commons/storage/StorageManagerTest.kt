package br.com.brunocarvalhs.friendssecrets.commons.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@Serializable
data class TestStorageModel(val id: Int, val name: String)

class StorageManagerTest {

    private val dataStore: DataStore<Preferences> = mockk()
    private val json = Json { ignoreUnknownKeys = true }
    private lateinit var storageManager: StorageManager

    @Before
    fun setup() {
        mockkStatic("androidx.datastore.preferences.core.PreferencesKt")
        storageManager = StorageManager(dataStore, json)
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.datastore.preferences.core.PreferencesKt")
    }

    private fun mockEdit() {
        val preferences = mockk<MutablePreferences>(relaxed = true)
        coEvery { dataStore.edit(any()) } coAnswers {
            val transform = it.invocation.args[0] as suspend (MutablePreferences) -> Unit
            transform(preferences)
            preferences
        }
    }

    @Test
    fun `save should store string value`() = runTest {
        // Given
        val key = "test_key"
        val value = "test_value"
        mockEdit()

        // When
        storageManager.save(key, value)

        // Then
        coVerify { dataStore.edit(any()) }
    }

    @Test
    fun `save should store array as string set`() = runTest {
        // Given
        val key = "array_key"
        val value = arrayOf("item1", "item2")
        mockEdit()

        // When
        storageManager.save(key, value)

        // Then
        coVerify { dataStore.edit(any()) }
    }

    @Test
    fun `load should return string value`() = runTest {
        // Given
        val key = "test_key"
        val value = "test_value"
        val preferences = mockk<Preferences>()
        every { preferences[any<Preferences.Key<String>>()] } returns value
        coEvery { dataStore.data } returns flowOf(preferences)

        // When
        val result = storageManager.load(key, String::class)

        // Then
        assertEquals(value, result)
    }

    @Test
    fun `load should return custom object`() = runTest {
        // Given
        val key = "object_key"
        val model = TestStorageModel(1, "Bruno")
        val jsonValue = json.encodeToString(TestStorageModel.serializer(), model)
        val preferences = mockk<Preferences>()
        
        every { preferences[any<Preferences.Key<String>>()] } returns jsonValue
        coEvery { dataStore.data } returns flowOf(preferences)

        // When
        val result = storageManager.load(key, TestStorageModel::class)

        // Then
        assertEquals(model, result)
    }

    @Test
    fun `remove should call dataStore edit`() = runTest {
        // Given
        val key = "remove_key"
        mockEdit()

        // When
        storageManager.remove(key)

        // Then
        coVerify { dataStore.edit(any()) }
    }
}

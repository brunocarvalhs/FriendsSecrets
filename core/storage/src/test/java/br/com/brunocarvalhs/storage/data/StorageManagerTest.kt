package br.com.brunocarvalhs.storage.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import br.com.brunocarvalhs.storage.domain.StorageService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class StorageManagerTest {

    private lateinit var context: Context
    private lateinit var storage: StorageService
    private lateinit var file: File

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        file = context.preferencesDataStoreFile("test_prefs")
        file.delete()
        storage = StorageManager(context)
    }

    @After
    fun tearDown() {
        file.delete()
    }

    @Test
    fun shouldSaveAndLoadString() = runTest {
        storage.save("key_string", "value")

        val result = storage.load("key_string", String::class)

        assertEquals("value", result)
    }

    @Test
    fun shouldSaveAndLoadList() = runTest {
        val list = listOf("A", "B", "C")

        storage.save("key_list", list)

        val result = storage.load("key_list", List::class)

        assertEquals(list, result)
    }

    @Test
    fun shouldSaveAndLoadSet() = runTest {
        val set = setOf("A", "B")

        storage.save("key_set", set)

        val result = storage.load("key_set", Set::class)

        assertEquals(set, result)
    }

    @Test
    fun shouldSaveAndLoadArray() = runTest {
        val array = arrayOf("A", "B")

        storage.save("key_array", array)

        val result = storage.load("key_array", Array<String>::class)

        assertArrayEquals(array, result)
    }

    @Test
    fun shouldSaveAndLoadObject() = runTest {
        val user = TestUser(1, "Bruno")

        storage.save("key_obj", user)

        val result = storage.load("key_obj", TestUser::class)

        assertEquals(user, result)
    }

    @Test
    fun shouldRemoveValue() = runTest {
        storage.save("key_remove", "value")

        storage.remove("key_remove")

        val result = storage.load("key_remove", String::class)

        assertNull(result)
    }

    @Test
    fun shouldReturnNullWhenKeyNotExists() = runTest {
        val result = storage.load("unknown_key", String::class)

        assertNull(result)
    }

    @Serializable
    data class TestUser(
        val id: Int,
        val name: String
    )
}
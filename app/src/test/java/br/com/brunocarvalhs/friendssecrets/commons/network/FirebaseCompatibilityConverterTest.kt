package br.com.brunocarvalhs.friendssecrets.commons.network

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FirebaseCompatibilityConverterTest {

    private lateinit var converter: FirebaseCompatibilityConverter

    @Before
    fun setup() {
        converter = FirebaseCompatibilityConverter()
    }

    @Test
    fun `toJsonElement should return JsonNull for null input`() {
        val result = converter.toJsonElement(null)
        assertTrue(result is JsonNull)
    }

    @Test
    fun `toJsonElement should return JsonPrimitive for String input`() {
        val input = "test"
        val result = converter.toJsonElement(input) as JsonPrimitive
        assertEquals(input, result.content)
    }

    @Test
    fun `toJsonElement should return JsonPrimitive for Number input`() {
        val input = 123
        val result = converter.toJsonElement(input) as JsonPrimitive
        assertEquals(input.toString(), result.content)
    }

    @Test
    fun `toJsonElement should return JsonArray for List input`() {
        val input = listOf("a", "b")
        val result = converter.toJsonElement(input) as JsonArray
        assertEquals(2, result.size)
        assertEquals("a", (result[0] as JsonPrimitive).content)
    }

    @Test
    fun `toJsonElement should return JsonObject for regular Map input`() {
        val input = mapOf("key" to "value")
        val result = converter.toJsonElement(input) as JsonObject
        assertEquals(1, result.size)
        assertEquals("value", (result["key"] as JsonPrimitive).content)
    }

    @Test
    fun `toJsonElement should return JsonArray for numeric indexed Map (Firebase corrupted list)`() {
        val input = mapOf("0" to "item0", "1" to "item1")
        val result = converter.toJsonElement(input)
        assertTrue(result is JsonArray)
        assertEquals(2, (result as JsonArray).size)
        assertEquals("item0", (result[0] as JsonPrimitive).content)
        assertEquals("item1", (result[1] as JsonPrimitive).content)
    }

    @Test
    fun `toJsonElement should return JsonArray for Map with all values as objects`() {
        val input = mapOf(
            "user1" to mapOf("name" to "Bruno"),
            "user2" to mapOf("name" to "Alice")
        )
        val result = converter.toJsonElement(input)
        assertTrue(result is JsonArray)
        assertEquals(2, (result as JsonArray).size)
    }

    @Test
    fun `listToTypedArray should convert list to Array via reflection`() {
        val input = listOf("one", "two")
        val result = converter.listToTypedArray(input, Array<String>::class.java) as Array<*>
        
        assertEquals(2, result.size)
        assertEquals("one", result[0])
        assertEquals("two", result[1])
    }
}

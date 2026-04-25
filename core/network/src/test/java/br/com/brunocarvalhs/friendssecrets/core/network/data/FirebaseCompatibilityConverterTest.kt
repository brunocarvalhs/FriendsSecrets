package br.com.brunocarvalhs.friendssecrets.core.network.data

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
    fun shouldConvertNullToJsonNull() {
        val result = converter.toJsonElement(null)

        assertEquals(JsonNull, result)
    }

    @Test
    fun shouldConvertPrimitiveTypes() {
        assertEquals(JsonPrimitive(true), converter.toJsonElement(true))
        assertEquals(JsonPrimitive(10), converter.toJsonElement(10))
        assertEquals(JsonPrimitive("text"), converter.toJsonElement("text"))
    }

    @Test
    fun shouldConvertListToJsonArray() {
        val list = listOf(1, 2, 3)

        val result = converter.toJsonElement(list)

        assertTrue(result is JsonArray)
        assertEquals(3, (result as JsonArray).size)
    }

    @Test
    fun shouldConvertSimpleMapToJsonObject() {
        val map = mapOf("a" to 1, "b" to "text")

        val result = converter.toJsonElement(map)

        assertTrue(result is JsonObject)
        val json = result as JsonObject

        assertEquals(JsonPrimitive(1), json["a"])
        assertEquals(JsonPrimitive("text"), json["b"])
    }

    @Test
    fun shouldConvertNumericKeyMapToJsonArray() {
        val map = mapOf(
            "0" to "A",
            "1" to "B"
        )

        val result = converter.toJsonElement(map)

        assertTrue(result is JsonArray)
        val array = result as JsonArray

        assertEquals(JsonPrimitive("A"), array[0])
        assertEquals(JsonPrimitive("B"), array[1])
    }

    @Test
    fun shouldSortNumericKeysBeforeConvertingToArray() {
        val map = mapOf(
            "2" to "C",
            "0" to "A",
            "1" to "B"
        )

        val result = converter.toJsonElement(map)

        val array = result as JsonArray

        assertEquals(JsonPrimitive("A"), array[0])
        assertEquals(JsonPrimitive("B"), array[1])
        assertEquals(JsonPrimitive("C"), array[2])
    }

    @Test
    fun shouldConvertMapOfObjectsToJsonArray() {
        val map = mapOf(
            "a" to mapOf("name" to "A"),
            "b" to mapOf("name" to "B")
        )

        val result = converter.toJsonElement(map)

        assertTrue(result is JsonArray)
        val array = result as JsonArray

        assertEquals(2, array.size)
        assertTrue(array[0] is JsonObject)
    }

    @Test
    fun shouldFallbackToStringForUnknownType() {
        val obj = object {
            override fun toString() = "custom"
        }

        val result = converter.toJsonElement(obj)

        assertEquals(JsonPrimitive("custom"), result)
    }

    @Test
    fun shouldConvertListToTypedArray() {
        val list = listOf("A", "B")

        val result = converter.listToTypedArray(list, Array<String>::class.java)

        assertTrue(result is Array<*>)
        val array = result as Array<*>

        assertEquals("A", array[0])
        assertEquals("B", array[1])
    }

    @Test
    fun shouldConvertListToIntArray() {
        val list = listOf(1, 2, 3)

        val result = converter.listToTypedArray(list, Array<Int>::class.java)

        val array = result as Array<*>

        assertEquals(1, array[0])
        assertEquals(2, array[1])
        assertEquals(3, array[2])
    }

    @Test
    fun shouldHandleEmptyListToTypedArray() {
        val list = emptyList<String>()

        val result = converter.listToTypedArray(list, Array<String>::class.java)

        val array = result as Array<*>

        assertTrue(array.isEmpty())
    }
}

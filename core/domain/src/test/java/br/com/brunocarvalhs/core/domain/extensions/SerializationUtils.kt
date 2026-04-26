package br.com.brunocarvalhs.core.domain.extensions

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SerializationUtils {

    @Test
    fun `should convert JsonPrimitive string to String`() {
        val json: JsonElement = JsonPrimitive("hello")

        val result = json.toAny()

        assertEquals("hello", result)
    }

    @Test
    fun `should convert JsonPrimitive boolean to Boolean`() {
        val json: JsonElement = JsonPrimitive(true)

        val result = json.toAny()

        assertEquals(true, result)
    }

    @Test
    fun `should convert JsonPrimitive long to Long`() {
        val json: JsonElement = JsonPrimitive(123L)

        val result = json.toAny()

        assertEquals(123L, result)
    }

    @Test
    fun `should convert JsonPrimitive double to Double`() {
        val json: JsonElement = JsonPrimitive(12.34)

        val result = json.toAny()

        assertEquals(12.34, result)
    }

    @Test
    fun `should convert JsonNull to null`() {
        val json: JsonElement = JsonNull

        val result = json.toAny()

        assertNull(result)
    }

    @Test
    fun `should convert JsonArray to List`() {
        val json: JsonElement = JsonArray(
            listOf(
                JsonPrimitive("text"),
                JsonPrimitive(10),
                JsonPrimitive(true)
            )
        )

        val result = json.toAny()

        assertTrue(result is List<*>)
        val list = result as List<*>

        assertEquals("text", list[0])
        assertEquals(10L, list[1])
        assertEquals(true, list[2])
    }

    @Test
    fun `should convert JsonObject to Map`() {
        val json: JsonElement = buildJsonObject {
            put("name", "Bruno")
            put("age", 30)
            put("active", true)
        }

        val result = json.toAny()

        assertTrue(result is Map<*, *>)
        val map = result as Map<*, *>

        assertEquals("Bruno", map["name"])
        assertEquals(30L, map["age"])
        assertEquals(true, map["active"])
    }

    @Test
    fun `should convert nested JsonObject correctly`() {
        val json = buildJsonObject {
            put("user", buildJsonObject {
                put("name", "Bruno")
                put("age", 30)
            })
        }

        val result = json.toAny() as Map<*, *>
        val user = result["user"] as Map<*, *>

        assertEquals("Bruno", user["name"])
        assertEquals(30L, user["age"])
    }

    @Test
    fun `should convert complex JsonObject using toVanillaMap`() {
        val json = buildJsonObject {
            put("name", "Bruno")
            put("scores", buildJsonArray {
                add(10)
                add(20)
            })
            put("meta", buildJsonObject {
                put("active", true)
            })
            put("nullable", JsonNull)
        }

        val result = json.toVanillaMap()

        assertEquals("Bruno", result["name"])

        val scores = result["scores"] as List<*>
        assertEquals(listOf(10L, 20L), scores)

        val meta = result["meta"] as Map<*, *>
        assertEquals(true, meta["active"])

        assertNull(result["nullable"])
    }
}

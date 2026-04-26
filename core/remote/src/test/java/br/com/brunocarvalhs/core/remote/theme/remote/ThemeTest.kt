package br.com.brunocarvalhs.core.remote.theme.remote

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `should serialize using SerialName keys`() {
        val theme = Theme(
            primary = "#000000",
            onPrimary = "#ffffff"
        )

        val result = json.encodeToString(Theme.serializer(), theme)

        assertEquals(true, result.contains("\"primary\":\"#000000\""))
        assertEquals(true, result.contains("\"onPrimary\":\"#ffffff\""))
    }

    @Test
    fun `should deserialize using SerialName keys`() {
        val jsonString = """
            {
                "primary": "#111111",
                "onPrimary": "#ffffff",
                "secondary": "#222222"
            }
        """.trimIndent()

        val result = json.decodeFromString(Theme.serializer(), jsonString)

        assertEquals("#111111", result.primary)
        assertEquals("#ffffff", result.onPrimary)
        assertEquals("#222222", result.secondary)
    }

    @Test
    fun `should use default values when field is missing`() {
        val jsonString = """
            {
                "primary": "#111111"
            }
        """.trimIndent()

        val result = json.decodeFromString(Theme.serializer(), jsonString)

        assertEquals("#111111", result.primary)
        assertEquals("", result.onPrimary)
        assertEquals("", result.secondary)
    }

    @Test
    fun `should ignore unknown keys`() {
        val jsonString = """
            {
                "primary": "#111111",
                "unknownField": "shouldBeIgnored"
            }
        """.trimIndent()

        val result = json.decodeFromString(Theme.serializer(), jsonString)

        assertEquals("#111111", result.primary)
    }

    @Test
    fun `should serialize all fields using correct json keys`() {
        val theme = Theme(
            primary = "a",
            onPrimary = "b",
            primaryContainer = "c",
            onPrimaryContainer = "d"
        )

        val jsonString = json.encodeToString(Theme.serializer(), theme)

        assert(jsonString.contains("\"primary\":\"a\""))
        assert(jsonString.contains("\"onPrimary\":\"b\""))
        assert(jsonString.contains("\"primaryContainer\":\"c\""))
        assert(jsonString.contains("\"onPrimaryContainer\":\"d\""))
    }
}

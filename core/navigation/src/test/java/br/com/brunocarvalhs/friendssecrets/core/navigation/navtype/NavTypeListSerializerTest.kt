package br.com.brunocarvalhs.friendssecrets.core.navigation.navtype

import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NavTypeListSerializerTest {

    @Serializable
    data class TestModel(
        val id: Int,
        val name: String
    )

    @Test
    fun shouldSerializeAndParseList() {
        val navType = navTypeListSerializer<TestModel>()
        val list = listOf(
            TestModel(1, "A"),
            TestModel(2, "B")
        )

        val serialized = navType.serializeAsValue(list)
        val parsed = navType.parseValue(serialized)

        assertEquals(list, parsed)
    }

    @Test
    fun shouldHandleEmptyList() {
        val navType = navTypeListSerializer<TestModel>()
        val emptyList = emptyList<TestModel>()

        val serialized = navType.serializeAsValue(emptyList)
        val parsed = navType.parseValue(serialized)

        assertTrue(parsed.isEmpty())
    }

    @Test
    fun shouldEncodeAndDecodeSpecialCharacters() {
        val navType = navTypeListSerializer<TestModel>()
        val list = listOf(
            TestModel(1, "Bruno & Maria / Test")
        )

        val serialized = navType.serializeAsValue(list)

        // Ensure it's actually URL encoded
        assertTrue(serialized.contains("%"))

        val parsed = navType.parseValue(serialized)

        assertEquals(list, parsed)
    }
}
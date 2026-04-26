package br.com.brunocarvalhs.core.navigation.navtype

import android.os.Bundle
import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class NavTypeSerializerTest {

    @Serializable
    data class TestModel(
        val id: Int,
        val name: String
    )

    @Test
    fun shouldSerializeAndParseObject() {
        val navType = navTypeSerializer<TestModel>()
        val model = TestModel(1, "Bruno")

        val serialized = navType.serializeAsValue(model)
        val parsed = navType.parseValue(serialized)

        assertEquals(model, parsed)
    }

    @Test
    fun shouldEncodeAndDecodeSpecialCharacters() {
        val navType = navTypeSerializer<TestModel>()
        val model = TestModel(3, "Bruno & Maria / Test")

        val serialized = navType.serializeAsValue(model)

        // ensure it's URL encoded
        assertTrue(serialized.contains("%"))

        val parsed = navType.parseValue(serialized)

        assertEquals(model, parsed)
    }

    @Test
    fun shouldIgnoreUnknownKeys() {
        val navType = navTypeSerializer<TestModel>()

        val jsonWithExtraField = """{"id":1,"name":"Bruno","extra":"ignored"}"""
        val encoded = URLEncoder.encode(jsonWithExtraField, StandardCharsets.UTF_8.name())

        val parsed = navType.parseValue(encoded)

        assertEquals(TestModel(1, "Bruno"), parsed)
    }

    @Test
    fun shouldReturnNullWhenBundleDoesNotContainKey() {
        val navType = navTypeSerializer<TestModel>()
        val bundle = Bundle()

        val result = navType.get(bundle, "missing")

        assertNull(result)
    }
}

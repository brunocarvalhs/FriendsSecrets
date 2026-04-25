package br.com.brunocarvalhs.friendssecrets.core.navigation.navtype

import android.os.Bundle
import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NavTypeSerializerNullableTest {

    @Serializable
    data class TestModel(
        val id: Int,
        val name: String
    )

    @Test
    fun shouldSerializeAndParseNullValue() {
        val navType = navTypeSerializerNullable<TestModel>()

        val serialized = navType.serializeAsValue(null)
        val parsed = navType.parseValue(serialized)

        assertEquals("null", serialized)
        assertNull(parsed)
    }

    @Test
    fun shouldSerializeAndParseNonNullValue() {
        val navType = navTypeSerializerNullable<TestModel>()
        val model = TestModel(1, "Bruno")

        val serialized = navType.serializeAsValue(model)
        val parsed = navType.parseValue(serialized)

        assertEquals(model, parsed)
    }

    @Test
    fun shouldPutAndGetNullValueFromBundle() {
        val navType = navTypeSerializerNullable<TestModel>()
        val bundle = Bundle()

        navType.put(bundle, "key", null)
        val result = navType.get(bundle, "key")

        assertNull(result)
    }

    @Test
    fun shouldHandleEncodedValueCorrectly() {
        val navType = navTypeSerializerNullable<TestModel>()
        val model = TestModel(3, "Bruno & Maria / Test")

        val serialized = navType.serializeAsValue(model)

        // Ensure URL encoding happened
        assertTrue(serialized.contains("%"))

        val parsed = navType.parseValue(serialized)

        assertEquals(model, parsed)
    }

    @Test
    fun shouldReturnNullWhenParsingLiteralNull() {
        val navType = navTypeSerializerNullable<TestModel>()

        val parsed = navType.parseValue("null")

        assertNull(parsed)
    }
}

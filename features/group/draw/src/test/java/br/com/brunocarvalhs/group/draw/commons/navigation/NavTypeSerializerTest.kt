package br.com.brunocarvalhs.group.draw.commons.navigation

import android.os.Bundle
import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Serializable
data class TestDrawModel(val id: String, val value: String)

@RunWith(RobolectricTestRunner::class)
class NavTypeSerializerTest {

    private val serializer = navTypeSerializer<TestDrawModel>()

    @Test
    fun `serializeAsValue should encode to JSON and URL encode`() {
        val model = TestDrawModel("1", "Value 1")
        val result = serializer.serializeAsValue(model)
        
        val decoded = URLDecoder.decode(result, StandardCharsets.UTF_8.name())
        assertEquals("{\"id\":\"1\",\"value\":\"Value 1\"}", decoded)
    }

    @Test
    fun `parseValue should decode URL and JSON`() {
        val json = "{\"id\":\"2\",\"value\":\"Value 2\"}"
        val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.name())
        
        val result = serializer.parseValue(encoded)
        
        assertEquals("2", result.id)
        assertEquals("Value 2", result.value)
    }

    @Test
    fun `put and get should work with Bundle`() {
        val bundle = Bundle()
        val model = TestDrawModel("3", "Value 3")
        
        serializer.put(bundle, "key", model)
        val result = serializer.get(bundle, "key")
        
        assertEquals(model, result)
    }
}

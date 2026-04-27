package br.com.brunocarvalhs.chat.commons.navigation

import android.os.Bundle
import br.com.brunocarvalhs.core.navigation.navtype.navTypeSerializer
import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Serializable
data class TestModel(val id: String, val name: String)

@RunWith(RobolectricTestRunner::class)
class NavTypeSerializerTest {

    private val serializer = navTypeSerializer<TestModel>()

    @Test
    fun `serializeAsValue should encode to JSON and URL encode`() {
        val model = TestModel("1", "Bruno Test")
        val result = serializer.serializeAsValue(model)
        
        val decoded = URLDecoder.decode(result, StandardCharsets.UTF_8.name())
        assertEquals("{\"id\":\"1\",\"name\":\"Bruno Test\"}", decoded)
    }

    @Test
    fun `parseValue should decode URL and JSON`() {
        val json = "{\"id\":\"2\",\"name\":\"Chat\"}"
        val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.name())
        
        val result = serializer.parseValue(encoded)
        
        assertEquals("2", result.id)
        assertEquals("Chat", result.name)
    }

    @Test
    fun `put and get should work with Bundle`() {
        val bundle = Bundle()
        val model = TestModel("3", "Bundle")
        
        serializer.put(bundle, "key", model)
        val result = serializer.get(bundle, "key")
        
        assertEquals(model, result)
    }
}

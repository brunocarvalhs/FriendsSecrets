package br.com.brunocarvalhs.group.details.commons.navigation

import android.os.Bundle
import br.com.brunocarvalhs.friendssecrets.core.navigation.navtype.navTypeSerializer
import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Serializable
data class TestDetailModel(val id: String, val name: String)

@RunWith(RobolectricTestRunner::class)
class NavTypeSerializerTest {

    private val serializer = navTypeSerializer<TestDetailModel>()

    @Test
    fun `serializeAsValue should encode to JSON and URL encode`() {
        val model = TestDetailModel("1", "Detail Test")
        val result = serializer.serializeAsValue(model)
        
        val decoded = URLDecoder.decode(result, StandardCharsets.UTF_8.name())
        assertEquals("{\"id\":\"1\",\"name\":\"Detail Test\"}", decoded)
    }

    @Test
    fun `parseValue should decode URL and JSON`() {
        val json = "{\"id\":\"2\",\"name\":\"Details\"}"
        val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.name())
        
        val result = serializer.parseValue(encoded)
        
        assertEquals("2", result.id)
        assertEquals("Details", result.name)
    }

    @Test
    fun `put and get should work with Bundle`() {
        val bundle = Bundle()
        val model = TestDetailModel("3", "Bundle")
        
        serializer.put(bundle, "key", model)
        val result = serializer.get(bundle, "key")
        
        assertEquals(model, result)
    }
}

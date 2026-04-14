package br.com.brunocarvalhs.group.details.commons.navigation

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
data class TestDetailItem(val id: Int, val value: String)

@RunWith(RobolectricTestRunner::class)
class NavTypeListSerializerTest {

    private val serializer = navTypeListSerializer<TestDetailItem>()

    @Test
    fun `serializeAsValue should encode list to JSON and URL encode`() {
        val list = listOf(TestDetailItem(1, "A"), TestDetailItem(2, "B"))
        val result = serializer.serializeAsValue(list)
        
        val decoded = URLDecoder.decode(result, StandardCharsets.UTF_8.name())
        assertEquals("[{\"id\":1,\"value\":\"A\"},{\"id\":2,\"value\":\"B\"}]", decoded)
    }

    @Test
    fun `parseValue should decode URL and JSON list`() {
        val json = "[{\"id\":10,\"value\":\"X\"}]"
        val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.name())
        
        val result = serializer.parseValue(encoded)
        
        assertEquals(1, result.size)
        assertEquals(10, result[0].id)
        assertEquals("X", result[0].value)
    }

    @Test
    fun `put and get should work with Bundle for lists`() {
        val bundle = Bundle()
        val list = listOf(TestDetailItem(3, "C"))
        
        serializer.put(bundle, "items", list)
        val result = serializer.get(bundle, "items")
        
        assertEquals(list, result)
    }
}

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
data class TestDrawItem(val id: Int, val name: String)

@RunWith(RobolectricTestRunner::class)
class NavTypeListSerializerTest {

    private val serializer = navTypeListSerializer<TestDrawItem>()

    @Test
    fun `serializeAsValue should encode list to JSON and URL encode`() {
        val list = listOf(TestDrawItem(1, "Member A"), TestDrawItem(2, "Member B"))
        val result = serializer.serializeAsValue(list)
        
        val decoded = URLDecoder.decode(result, StandardCharsets.UTF_8.name())
        assertEquals("[{\"id\":1,\"name\":\"Member A\"},{\"id\":2,\"name\":\"Member B\"}]", decoded)
    }

    @Test
    fun `parseValue should decode URL and JSON list`() {
        val json = "[{\"id\":10,\"name\":\"Secret Member\"}]"
        val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.name())
        
        val result = serializer.parseValue(encoded)
        
        assertEquals(1, result.size)
        assertEquals(10, result[0].id)
        assertEquals("Secret Member", result[0].name)
    }

    @Test
    fun `put and get should work with Bundle for lists`() {
        val bundle = Bundle()
        val list = listOf(TestDrawItem(3, "Item C"))
        
        serializer.put(bundle, "items", list)
        val result = serializer.get(bundle, "items")
        
        assertEquals(list, result)
    }
}

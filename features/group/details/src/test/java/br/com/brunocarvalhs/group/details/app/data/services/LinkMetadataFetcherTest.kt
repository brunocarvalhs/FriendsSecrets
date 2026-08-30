package br.com.brunocarvalhs.group.details.app.data.services

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class LinkMetadataFetcherTest {

    private lateinit var fetcher: LinkMetadataFetcher

    @Before
    fun setup() {
        fetcher = LinkMetadataFetcher()
    }

    @Test
    fun `parseMetadata should extract og title and image`() {
        // Given
        val html = """
            <html><head>
            <meta property="og:title" content="Nice Board Game" />
            <meta property="og:image" content="https://cdn.example.com/game.png" />
            </head></html>
        """.trimIndent()

        // When
        val metadata = fetcher.parseMetadata(html)

        // Then
        assertEquals("Nice Board Game", metadata.title)
        assertEquals("https://cdn.example.com/game.png", metadata.imageUrl)
    }

    @Test
    fun `parseMetadata should fall back to title tag when og title is missing`() {
        // Given
        val html = "<html><head><title>Fallback Title</title></head></html>"

        // When
        val metadata = fetcher.parseMetadata(html)

        // Then
        assertEquals("Fallback Title", metadata.title)
        assertNull(metadata.imageUrl)
    }

    @Test
    fun `parseMetadata should decode common html entities`() {
        // Given
        val html = """<meta property="og:title" content="Rock &amp; Roll" />"""

        // When
        val metadata = fetcher.parseMetadata(html)

        // Then
        assertEquals("Rock & Roll", metadata.title)
    }

    @Test
    fun `parseMetadata should return nulls for html without metadata`() {
        // Given
        val html = "<html><body>Nothing here</body></html>"

        // When
        val metadata = fetcher.parseMetadata(html)

        // Then
        assertNull(metadata.title)
        assertNull(metadata.imageUrl)
    }
}

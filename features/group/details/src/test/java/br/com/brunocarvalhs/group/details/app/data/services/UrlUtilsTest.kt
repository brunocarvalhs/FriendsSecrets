package br.com.brunocarvalhs.group.details.app.data.services

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UrlUtilsTest {

    @Test
    fun `isLikelyUrl should return true for http and https urls`() {
        assertTrue(isLikelyUrl("https://www.example.com/product/123"))
        assertTrue(isLikelyUrl("http://example.com"))
        assertTrue(isLikelyUrl("  https://example.com  "))
    }

    @Test
    fun `isLikelyUrl should return false for plain text`() {
        assertFalse(isLikelyUrl("Books"))
        assertFalse(isLikelyUrl("Games and coffee"))
        assertFalse(isLikelyUrl(""))
    }

    @Test
    fun `isLikelyUrl should return false for non http schemes`() {
        assertFalse(isLikelyUrl("ftp://example.com/file"))
        assertFalse(isLikelyUrl("javascript:alert(1)"))
    }
}

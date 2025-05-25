package br.com.brunocarvalhs.friendssecrets.common.extensions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class RandomExtensionsTest {

    @Test
    fun `token generates string of default size`() {
        val result = Random.Default.token()
        assertEquals(6, result.length)
    }

    @Test
    fun `token generates string of custom size`() {
        val result = Random.Default.token(10)
        assertEquals(10, result.length)
    }

    @Test
    fun `token generates alphanumeric string`() {
        val result = Random.Default.token(20)
        assertTrue(result.all { it.isLetterOrDigit() })
    }

    @Test
    fun `token generates different results`() {
        val result1 = Random.Default.token()
        val result2 = Random.Default.token()
        // Alta chance de serem diferentes
        assertTrue(result1 != result2)
    }
}

package br.com.brunocarvalhs.group.create.app.data.exceptions

import org.junit.Assert.assertEquals
import org.junit.Test

class FailedCreateGroupExceptionTest {

    @Test
    fun `exception should have default message`() {
        val exception = FailedCreateGroupException()
        assertEquals("Failed to create group", exception.message)
    }

    @Test
    fun `exception should have custom message`() {
        val customMessage = "Custom error message"
        val exception = FailedCreateGroupException(customMessage)
        assertEquals(customMessage, exception.message)
    }
}

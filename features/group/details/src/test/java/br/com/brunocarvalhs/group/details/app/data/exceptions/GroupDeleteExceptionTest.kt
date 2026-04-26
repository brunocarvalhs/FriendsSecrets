package br.com.brunocarvalhs.group.details.app.data.exceptions

import org.junit.Assert.assertEquals
import org.junit.Test

class GroupDeleteExceptionTest {

    @Test
    fun `exception should have default message`() {
        val exception = GroupDeleteException()
        assertEquals("Error deleting group", exception.message)
    }

    @Test
    fun `exception should have custom message`() {
        val customMessage = "Failed to delete secret santa group"
        val exception = GroupDeleteException(customMessage)
        assertEquals(customMessage, exception.message)
    }
}

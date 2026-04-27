package br.com.brunocarvalhs.group.details.app.data.exceptions

import org.junit.Assert.assertEquals
import org.junit.Test

class GroupNotFoundExceptionTest {

    @Test
    fun `exception should have default message`() {
        val exception = GroupNotFoundException()
        assertEquals("Group not found", exception.message)
    }

    @Test
    fun `exception should have custom message`() {
        val customMessage = "Could not find the specified group"
        val exception = GroupNotFoundException(customMessage)
        assertEquals(customMessage, exception.message)
    }
}

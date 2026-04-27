package br.com.brunocarvalhs.group.list.app.data.exceptions

import org.junit.Assert.assertEquals
import org.junit.Test

class GroupAlreadyExistExceptionTest {

    @Test
    fun `exception should have default message`() {
        val exception = GroupAlreadyExistException()
        assertEquals("Group already exist", exception.message)
    }

    @Test
    fun `exception should have custom message`() {
        val customMessage = "Group with this token is already in your list"
        val exception = GroupAlreadyExistException(customMessage)
        assertEquals(customMessage, exception.message)
    }
}

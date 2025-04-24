package br.com.brunocarvalhs.friendssecrets.data.exceptions

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GroupNotFoundExceptionTest {

    @Before
    fun setUp() {
        val customApplication = mockk<CustomApplication>()
        every { customApplication.getString(R.string.exception_group_not_found) } returns "Group not found"

        mockkObject(CustomApplication.Companion)
        every { CustomApplication.getInstance() } returns customApplication
    }

    @Test
    fun messageIsCorrect() {
        val exception = GroupNotFoundException()
        assertEquals("Group not found", exception.message)
    }

    @Test
    fun customMessageIsCorrect() {
        val customMessage = "Custom error message"
        val exception = GroupNotFoundException(customMessage)
        assertEquals(customMessage, exception.message)
    }
}
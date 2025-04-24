package br.com.brunocarvalhs.friendssecrets.data.exceptions

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GroupAlreadyExistExceptionTest {

    @Before
    fun setUp() {
        val customApplication = mockk<CustomApplication>()
        every { customApplication.getString(R.string.exception_group_already_exist) } returns "Group already exists"

        mockkObject(CustomApplication.Companion)
        every { CustomApplication.getInstance() } returns customApplication
    }

    @Test
    fun messageIsCorrect() {
        val exception = GroupAlreadyExistException()
        assertEquals("Group already exists", exception.message)
    }

    @Test
    fun customMessageIsCorrect() {
        val customMessage = "Custom error message"
        val exception = GroupAlreadyExistException(customMessage)
        assertEquals(customMessage, exception.message)
    }
}
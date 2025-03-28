package br.com.brunocarvalhs.friendssecrets.data.exceptions

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MinimumsMembersOfDrawExceptionTest {

    @Before
    fun setUp() {
        val customApplication = mockk<CustomApplication>()
        every { customApplication.getString(R.string.exception_minimums_members_of_draw) } returns "Group already exists"

        mockkObject(CustomApplication.Companion)
        every { CustomApplication.getInstance() } returns customApplication
    }

    @Test
    fun messageIsCorrect() {
        val exception = MinimumsMembersOfDrawException()
        assertEquals("The group must have at least 3 participants to hold the draw.", exception.message)
    }

    @Test
    fun customMessageIsCorrect() {
        val customMessage = "Custom error message"
        val exception = MinimumsMembersOfDrawException(customMessage)
        assertEquals(customMessage, exception.message)
    }
}
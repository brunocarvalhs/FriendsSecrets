package br.com.brunocarvalhs.group.create.app.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ContactModelTest {

    @Test
    fun `should convert ContactModel to UserModel correctly`() {
        // given
        val contact = ContactModel(
            id = "1",
            name = "Bruno",
            phoneNumber = "999999999",
            photoUrl = "http://photo.com/image.png",
            email = "bruno@email.com",
            isSelected = true
        )

        // when
        val user = contact.toUserModel()

        // then
        assertEquals("1", user.id)
        assertEquals("Bruno", user.name)
        assertEquals("999999999", user.phoneNumber)
        assertEquals("http://photo.com/image.png", user.photoUrl)
        assertEquals(emptyList<Any>(), user.likes)
    }

    @Test
    fun `should use default values when ContactModel is empty`() {
        // given
        val contact = ContactModel()

        // when
        val user = contact.toUserModel()

        // then
        assertEquals("", user.id)
        assertEquals("", user.name)
        assertEquals("", user.phoneNumber)
        assertNull(user.photoUrl)
        assertEquals(emptyList<Any>(), user.likes)
    }
}

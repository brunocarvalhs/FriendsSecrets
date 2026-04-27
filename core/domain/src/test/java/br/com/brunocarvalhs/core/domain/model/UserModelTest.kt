package br.com.brunocarvalhs.core.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserModelTest {

    @Test
    fun shouldCreateUserWithDefaultValues() {
        val user = UserModel()

        assertNotNull(user.id)
        assertTrue(user.id.isNotBlank())

        assertEquals("", user.name)
        assertEquals("", user.phoneNumber)
        assertNull(user.photoUrl)
        assertTrue(user.likes.isEmpty())
    }

    @Test
    fun shouldGenerateUniqueIds() {
        val user1 = UserModel()
        val user2 = UserModel()

        assertNotEquals(user1.id, user2.id)
    }

    @Test
    fun shouldCreateUserWithCustomValues() {
        val likes = listOf("Books", "Games")

        val user = UserModel(
            id = "user-1",
            name = "Bruno",
            phoneNumber = "123456789",
            photoUrl = "http://photo.url",
            likes = likes
        )

        assertEquals("user-1", user.id)
        assertEquals("Bruno", user.name)
        assertEquals("123456789", user.phoneNumber)
        assertEquals("http://photo.url", user.photoUrl)
        assertEquals(likes, user.likes)
    }

    @Test
    fun shouldCopyUserCorrectly() {
        val original = UserModel(
            id = "1",
            name = "Bruno",
            likes = listOf("Music")
        )

        val updated = original.copy(
            name = "Carlos",
            likes = listOf("Movies")
        )

        assertEquals("1", updated.id)
        assertEquals("Carlos", updated.name)
        assertEquals(listOf("Movies"), updated.likes)

        assertEquals("Bruno", original.name)
        assertEquals(listOf("Music"), original.likes)
    }

    @Test
    fun shouldExposeCorrectConstants() {
        assertEquals("id", UserModel.ID)
        assertEquals("name", UserModel.NAME)
        assertEquals("phoneNumber", UserModel.PHONE_NUMBER)
        assertEquals("photoUrl", UserModel.PHOTO_URL)
        assertEquals("likes", UserModel.LIKES)
    }
}

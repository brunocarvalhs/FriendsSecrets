package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import org.junit.Assert.assertEquals
import org.junit.Test

class UserModelTest {

    @Test
    fun `test toMap returns correct map`() {
        val userModel = UserModel(
            id = "123",
            name = "John Doe",
            photoUrl = "http://example.com/photo.jpg",
            phoneNumber = "123456789",
            isPhoneNumberVerified = true,
            likes = listOf("Music", "Movies")
        )

        val expectedMap = mapOf(
            UserEntities.ID to "123",
            UserEntities.NAME to "John Doe",
            UserEntities.PHOTO_URL to "http://example.com/photo.jpg",
            UserEntities.PHONE_NUMBER to "123456789",
            UserEntities.IS_PHONE_NUMBER_VERIFIED to true,
            UserEntities.LIKES to listOf("Music", "Movies")
        )

        assertEquals(expectedMap, userModel.toMap())
    }

    @Test
    fun `test toCopy creates a new instance with updated values`() {
        val userModel = UserModel(
            id = "123",
            name = "John Doe",
            photoUrl = "http://example.com/photo.jpg",
            phoneNumber = "123456789",
            isPhoneNumberVerified = true,
            likes = listOf("Music", "Movies")
        )

        val updatedUser = userModel.toCopy(
            id = "456",
            name = "Jane Doe",
            photoUrl = "http://example.com/newphoto.jpg",
            phoneNumber = "987654321",
            isPhoneNumberVerified = false,
            likes = listOf("Books", "Travel")
        )

        assertEquals("456", updatedUser.id)
        assertEquals("Jane Doe", updatedUser.name)
        assertEquals("http://example.com/newphoto.jpg", updatedUser.photoUrl)
        assertEquals("987654321", updatedUser.phoneNumber)
        assertEquals(false, updatedUser.isPhoneNumberVerified)
        assertEquals(listOf("Books", "Travel"), updatedUser.likes)
    }

    @Test
    fun `test fromMap creates UserModel from map`() {
        val map = mapOf(
            UserEntities.ID to "123",
            UserEntities.NAME to "John Doe",
            UserEntities.PHOTO_URL to "http://example.com/photo.jpg",
            UserEntities.PHONE_NUMBER to "123456789",
            UserEntities.IS_PHONE_NUMBER_VERIFIED to true,
            UserEntities.LIKES to listOf("Music", "Movies")
        )

        val userModel = UserModel.fromMap(map)

        assertEquals("123", userModel.id)
        assertEquals("John Doe", userModel.name)
        assertEquals("http://example.com/photo.jpg", userModel.photoUrl)
        assertEquals("123456789", userModel.phoneNumber)
        assertEquals(true, userModel.isPhoneNumberVerified)
        assertEquals(listOf("Music", "Movies"), userModel.likes)
    }
}
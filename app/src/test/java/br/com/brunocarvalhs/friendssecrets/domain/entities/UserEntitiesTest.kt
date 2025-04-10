package br.com.brunocarvalhs.friendssecrets.domain.entities

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class UserEntitiesTest {

    @Test
    fun `test toMap`() {
        // Mock da interface UserEntities
        val userMock = mockk<UserEntities>()

        // Configurando o comportamento do mock
        every { userMock.id } returns "123"
        every { userMock.name } returns "John Doe"
        every { userMock.photoUrl } returns "http://example.com/photo.jpg"
        every { userMock.phoneNumber } returns "123456789"
        every { userMock.isPhoneNumberVerified } returns true
        every { userMock.likes } returns listOf("Music", "Movies")
        every { userMock.toMap() } returns mapOf(
            "id" to "123",
            "name" to "John Doe",
            "photoUrl" to "http://example.com/photo.jpg",
            "phoneNumber" to "123456789",
            "isPhoneNumberVerified" to true,
            "likes" to listOf("Music", "Movies")
        )

        // Verificando o metodo toMap
        val result = userMock.toMap()
        assertEquals("123", result["id"])
        assertEquals("John Doe", result["name"])
        assertEquals("http://example.com/photo.jpg", result["photoUrl"])
        assertEquals("123456789", result["phoneNumber"])
        assertEquals(true, result["isPhoneNumberVerified"])
        assertEquals(listOf("Music", "Movies"), result["likes"])
    }

    @Test
    fun `test toCopy`() {
        // Mock da interface UserEntities
        val userMock = mockk<UserEntities>()

        // Configurando o comportamento do mock
        every { userMock.id } returns "123"
        every { userMock.name } returns "John Doe"
        every { userMock.photoUrl } returns "http://example.com/photo.jpg"
        every { userMock.phoneNumber } returns "123456789"
        every { userMock.isPhoneNumberVerified } returns true
        every { userMock.likes } returns listOf("Music", "Movies")
        every {
            userMock.toCopy(
                id = "456",
                name = "Jane Doe",
                photoUrl = "http://example.com/newphoto.jpg",
                phoneNumber = "987654321",
                isPhoneNumberVerified = false,
                likes = listOf("Books", "Travel")
            )
        } returns mockk {
            every { id } returns "456"
            every { name } returns "Jane Doe"
            every { photoUrl } returns "http://example.com/newphoto.jpg"
            every { phoneNumber } returns "987654321"
            every { isPhoneNumberVerified } returns false
            every { likes } returns listOf("Books", "Travel")
        }

        // Verificando o metodo toCopy
        val copiedUser = userMock.toCopy(
            id = "456",
            name = "Jane Doe",
            photoUrl = "http://example.com/newphoto.jpg",
            phoneNumber = "987654321",
            isPhoneNumberVerified = false,
            likes = listOf("Books", "Travel")
        )

        assertEquals("456", copiedUser.id)
        assertEquals("Jane Doe", copiedUser.name)
        assertEquals("http://example.com/newphoto.jpg", copiedUser.photoUrl)
        assertEquals("987654321", copiedUser.phoneNumber)
        assertEquals(false, copiedUser.isPhoneNumberVerified)
        assertEquals(listOf("Books", "Travel"), copiedUser.likes)
    }
}
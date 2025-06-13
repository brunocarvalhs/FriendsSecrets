package br.com.brunocarvalhs.friendssecrets.data.mappers

import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.data.repository.dto.UserDTO
import org.junit.Assert.assertEquals
import org.junit.Test

class UserEntitiesMappersTest {
    @Test
    fun `toEntities should map UserDTO to UserEntities correctly`() {
        val dto = UserDTO(
            id = "1",
            name = "Test User",
            photoUrl = "photoUrl",
            phoneNumber = "123456789",
            isPhoneNumberVerified = true,
            likes = listOf("like1", "like2")
        )
        val entity = dto.toEntities()
        assertEquals(dto.id, entity.id)
        assertEquals(dto.name, entity.name)
        assertEquals(dto.photoUrl, entity.photoUrl)
        assertEquals(dto.phoneNumber, entity.phoneNumber)
        assertEquals(dto.isPhoneNumberVerified, entity.isPhoneNumberVerified)
        assertEquals(dto.likes, entity.likes)
    }

    @Test
    fun `factory should create UserEntities with default and custom values`() {
        val base = UserModel(
            id = "1",
            name = "Base User",
            photoUrl = "basePhoto",
            phoneNumber = "987654321",
            isPhoneNumberVerified = false,
            likes = listOf("likeA")
        )
        val custom = base.factory(
            id = "2",
            name = "Custom User",
            photoUrl = "customPhoto",
            phoneNumber = "000000000",
            isPhoneNumberVerified = true,
            likes = listOf("likeB", "likeC")
        )
        assertEquals("2", custom.id)
        assertEquals("Custom User", custom.name)
        assertEquals("customPhoto", custom.photoUrl)
        assertEquals("000000000", custom.phoneNumber)
        assertEquals(true, custom.isPhoneNumberVerified)
        assertEquals(listOf("likeB", "likeC"), custom.likes)
    }

    @Test
    fun `factory should use default values from UserEntities when not overridden`() {
        val base = UserModel(
            id = "1",
            name = "Base User",
            photoUrl = "basePhoto",
            phoneNumber = "987654321",
            isPhoneNumberVerified = false,
            likes = listOf("likeA")
        )
        val result = base.factory()
        assertEquals(base.id, result.id)
        assertEquals(base.name, result.name)
        assertEquals(base.photoUrl, result.photoUrl)
        assertEquals(base.phoneNumber, result.phoneNumber)
        assertEquals(base.isPhoneNumberVerified, result.isPhoneNumberVerified)
        assertEquals(base.likes, result.likes)
    }
}


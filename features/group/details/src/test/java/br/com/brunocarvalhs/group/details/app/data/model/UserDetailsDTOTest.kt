package br.com.brunocarvalhs.group.details.app.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class UserDetailsDTOTest {

    @Test
    fun `toDomain should map DTO to Domain model correctly`() {
        // Given
        val dto = UserDetailsDTO(
            id = "user1",
            name = "Bruno",
            phoneNumber = "123456",
            photoUrl = "http://photo.com",
            likes = listOf("Coding")
        )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.name, domain.name)
        assertEquals(dto.phoneNumber, domain.phoneNumber)
        assertEquals(dto.photoUrl, domain.photoUrl)
        assertEquals(dto.likes, domain.likes)
    }
}

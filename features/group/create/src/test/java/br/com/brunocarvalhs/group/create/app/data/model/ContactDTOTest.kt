package br.com.brunocarvalhs.group.create.app.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ContactDTOTest {

    @Test
    fun `toDomain should map DTO to Domain model correctly`() {
        // Given
        val dto = ContactDTO(
            id = "1",
            displayName = "Bruno",
            phoneNumber = "999999999",
            photoUri = "http://photo.com",
            email = "bruno@test.com"
        )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.displayName, domain.name)
        assertEquals(dto.phoneNumber, domain.phoneNumber)
        assertEquals(dto.photoUri, domain.photoUrl)
        assertEquals(dto.email, domain.email)
        assertEquals(false, domain.isSelected)
    }

    @Test
    fun `toDomain should handle null phone number`() {
        // Given
        val dto = ContactDTO(
            id = "1",
            displayName = "Bruno",
            phoneNumber = null
        )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals("", domain.phoneNumber)
    }
}

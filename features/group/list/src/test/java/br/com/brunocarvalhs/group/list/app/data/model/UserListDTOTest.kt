package br.com.brunocarvalhs.group.list.app.data.model

import br.com.brunocarvalhs.core.domain.model.UserModel
import org.junit.Assert.assertEquals
import org.junit.Test

class UserListDTOTest {

    @Test
    fun `toDomain should map DTO to Domain model correctly`() {
        // Given
        val dto = UserListDTO(
            id = "user1",
            name = "Bruno",
            photoUrl = "http://photo.com"
        )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.name, domain.name)
        assertEquals(dto.photoUrl, domain.photoUrl)
    }

    @Test
    fun `fromDomain should map Domain model to DTO correctly`() {
        // Given
        val domain = UserModel(
            id = "user1",
            name = "Bruno",
            photoUrl = "http://photo.com"
        )

        // When
        val dto = UserListDTO.fromDomain(domain)

        // Then
        assertEquals(domain.id, dto.id)
        assertEquals(domain.name, dto.name)
        assertEquals(domain.photoUrl, dto.photoUrl)
    }
}

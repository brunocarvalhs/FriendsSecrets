package br.com.brunocarvalhs.group.details.app.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class GroupDetailsDTOTest {

    @Test
    fun `toDomain should map DTO to Domain model correctly`() {
        // Given
        val dto = GroupDetailsDTO(
            id = "group1",
            name = "Secret Santa",
            description = "Fun times",
            token = "ABC123",
            date = "2023-12-25",
            minPrice = 10.0,
            maxPrice = 50.0,
            type = "Gift",
            photo_base64 = "base64String",
            members = listOf(UserDetailsDTO(id = "user1", name = "Bruno")),
            draws = mapOf("user1" to "user2"),
            owner_id = "owner123",
            is_owner = true,
            created_at = 123456789L
        )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.name, domain.name)
        assertEquals(dto.description, domain.description)
        assertEquals(dto.token, domain.token)
        assertEquals(dto.date, domain.date)
        assertEquals(dto.minPrice, domain.minPrice)
        assertEquals(dto.maxPrice, domain.maxPrice)
        assertEquals(dto.type, domain.type)
        assertEquals(dto.photo_base64, domain.photo)
        assertEquals(dto.members.size, domain.members.size)
        assertEquals(dto.draws, domain.draws)
        assertEquals(dto.owner_id, domain.ownerId)
        assertEquals(dto.is_owner, domain.isOwner)
        assertEquals(dto.created_at, domain.createdAt)
    }
}

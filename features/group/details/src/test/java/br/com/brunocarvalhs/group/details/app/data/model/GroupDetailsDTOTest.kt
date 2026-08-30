package br.com.brunocarvalhs.group.details.app.data.model

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.UserModel
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
            photoBase64 = "base64String",
            members = listOf(UserDetailsDTO(id = "user1", name = "Bruno")),
            draws = mapOf("user1" to "user2"),
            ownerId = "owner123",
            isOwner = true,
            createdAt = 123456789L
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
        assertEquals(dto.photoBase64, domain.photo)
        assertEquals(dto.members.size, domain.members.size)
        assertEquals(dto.draws, domain.draws)
        assertEquals(dto.ownerId, domain.ownerId)
        assertEquals(dto.isOwner, domain.isOwner)
        assertEquals(dto.createdAt, domain.createdAt)
    }

    @Test
    fun `fromDomain should map Domain model to DTO correctly`() {
        // Given
        val model = GroupModel(
            id = "group1",
            name = "Secret Santa",
            description = "Fun times",
            token = "ABC123",
            date = "2023-12-25",
            minPrice = 10.0,
            maxPrice = 50.0,
            type = "Gift",
            photo = "base64String",
            members = listOf(UserModel(id = "user1", name = "Bruno")),
            draws = mapOf("user1" to "user2"),
            ownerId = "owner123",
            isOwner = true,
            createdAt = 123456789L
        )

        // When
        val dto = GroupDetailsDTO.fromDomain(model)

        // Then
        assertEquals(model.id, dto.id)
        assertEquals(model.name, dto.name)
        assertEquals(model.description, dto.description)
        assertEquals(model.token, dto.token)
        assertEquals(model.date, dto.date)
        assertEquals(model.minPrice, dto.minPrice)
        assertEquals(model.maxPrice, dto.maxPrice)
        assertEquals(model.type, dto.type)
        assertEquals(model.photo, dto.photoBase64)
        assertEquals(model.members.size, dto.members.size)
        assertEquals(model.draws, dto.draws)
        assertEquals(model.ownerId, dto.ownerId)
        assertEquals(model.isOwner, dto.isOwner)
        assertEquals(model.createdAt, dto.createdAt)
    }

    @Test
    fun `toMap should serialize DTO fields`() {
        // Given
        val dto = GroupDetailsDTO(id = "group1", name = "Secret Santa", token = "ABC123")

        // When
        val map = dto.toMap()

        // Then
        assertEquals("group1", map[GroupModel.ID])
        assertEquals("Secret Santa", map[GroupModel.NAME])
        assertEquals("ABC123", map[GroupModel.TOKEN])
    }
}

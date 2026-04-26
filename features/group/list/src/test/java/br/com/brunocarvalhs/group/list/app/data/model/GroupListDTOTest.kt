package br.com.brunocarvalhs.group.list.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupListDTOTest {

    @Test
    fun `toDomain should map DTO to Domain model correctly`() {
        // Given
        val dto = GroupListDTO(
            id = "group1",
            name = "Test Group",
            description = "Description",
            token = "ABC123",
            date = "2023-12-25",
            photoBase64 = "photo",
            members = listOf(UserListDTO(id = "user1", name = "Bruno")),
            draws = mapOf("user1" to "user2"),
            ownerId = "owner1",
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
        val domain = GroupModel(
            id = "group1",
            name = "Test Group",
            token = "ABC123",
            ownerId = "owner1"
        )

        // When
        val dto = GroupListDTO.fromDomain(domain)

        // Then
        assertEquals(domain.id, dto.id)
        assertEquals(domain.name, dto.name)
        assertEquals(domain.token, dto.token)
        assertEquals(domain.ownerId, dto.ownerId)
    }

    @Test
    fun `toMap should return map with correct keys`() {
        // Given
        val dto = GroupListDTO(id = "group1", name = "Test Group")

        // When
        val map = dto.toMap()

        // Then
        assertEquals("group1", map[GroupModel.ID])
        assertEquals("Test Group", map[GroupModel.NAME])
    }
}

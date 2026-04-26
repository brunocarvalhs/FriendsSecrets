package br.com.brunocarvalhs.group.create.app.data.model

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.UserModel
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupCreateDTOTest {

    @Test
    fun `fromDomain should map GroupModel to DTO correctly`() {
        // Given
        val groupModel = GroupModel(
            id = "group1",
            name = "Secret Santa",
            description = "Fun group",
            token = "ABC123",
            date = "2023-12-25",
            minPrice = 10.0,
            maxPrice = 50.0,
            type = "SecretSanta",
            members = listOf(UserModel(id = "user1", name = "Bruno")),
            draws = mapOf("user1" to "user2"),
            isOwner = true,
            ownerId = "owner1",
            photo = "base64photo",
            createdAt = 123456789L
        )

        // When
        val dto = GroupCreateDTO.fromDomain(groupModel)

        // Then
        assertEquals(groupModel.id, dto.id)
        assertEquals(groupModel.name, dto.name)
        assertEquals(groupModel.description, dto.description)
        assertEquals(groupModel.token, dto.token)
        assertEquals(groupModel.date, dto.date)
        assertEquals(groupModel.minPrice, dto.minPrice)
        assertEquals(groupModel.maxPrice, dto.maxPrice)
        assertEquals(groupModel.type, dto.type)
        assertEquals(groupModel.members.size, dto.members.size)
        assertEquals(groupModel.draws, dto.draws)
        assertEquals(groupModel.isOwner, dto.isOwner)
        assertEquals(groupModel.ownerId, dto.ownerId)
        assertEquals(groupModel.photo, dto.photo)
        assertEquals(groupModel.createdAt, dto.createdAt)
    }

    @Test
    fun `toMap should return map with correct keys`() {
        // Given
        val dto = GroupCreateDTO(
            id = "group1",
            name = "Secret Santa",
            description = null,
            token = "ABC123",
            date = null,
            minPrice = null,
            maxPrice = null,
            type = null,
            members = emptyList(),
            draws = emptyMap(),
            isOwner = true,
            ownerId = "owner1",
            photo = null,
            createdAt = 123456789L
        )

        // When
        val map = dto.toMap()

        // Then
        assertEquals("group1", map[GroupModel.ID])
        assertEquals("Secret Santa", map[GroupModel.NAME])
        assertEquals("ABC123", map[GroupModel.TOKEN])
        assertEquals(true, map[GroupModel.IS_OWNER])
        assertEquals("owner1", map[GroupModel.OWNER_ID])
    }
}

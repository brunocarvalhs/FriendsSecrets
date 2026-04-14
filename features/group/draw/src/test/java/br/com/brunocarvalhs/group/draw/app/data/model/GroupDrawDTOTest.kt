package br.com.brunocarvalhs.group.draw.app.data.model

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupDrawDTOTest {

    @Test
    fun `fromDomain should map GroupModel to DTO correctly`() {
        // Given
        val groupModel = GroupModel(
            id = "group1",
            draws = mapOf("u1" to "u2")
        )

        // When
        val dto = GroupDrawDTO.fromDomain(groupModel)

        // Then
        assertEquals(groupModel.id, dto.id)
        assertEquals(groupModel.draws, dto.draws)
    }

    @Test
    fun `toMap should return map with correct keys`() {
        // Given
        val dto = GroupDrawDTO(
            id = "group1",
            draws = mapOf("u1" to "u2")
        )

        // When
        val map = dto.toMap()

        // Then
        assertEquals("group1", map[GroupModel.ID])
        assertEquals(mapOf("u1" to "u2"), map[GroupModel.DRAWS])
    }
}

package br.com.brunocarvalhs.friendssecrets.data.mappers

import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.data.repository.dto.GroupDTO
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GroupEntitiesMappersTest {
    @Test
    fun `toDTO should map GroupEntities to GroupDTO correctly`() {
        val user = UserModel(
            name = "User1",
            likes = listOf(
                "like1", "like2", "like3", "like4", "like5"
            ),
            photoUrl = "url1"
        )
        val group = GroupModel(
            id = "1",
            token = "token",
            name = "GroupName",
            description = "desc",
            members = listOf(user),
            draws = mapOf("User1" to "User2"),
            isOwner = true
        )
        val dto = group.toDTO()
        assertEquals(group.id, dto.id)
        assertEquals(group.token, dto.token)
        assertEquals(group.name, dto.name)
        assertEquals(group.description, dto.description)
        assertEquals(group.draws, dto.draws)
        assertEquals(group.isOwner, dto.isOwner)
        assertTrue(dto.members.containsKey("User1"))
        val member = dto.members["User1"]!!
        assertEquals("User1", member[GroupEntities.NAME])
        assertEquals(listOf(
            "like1", "like2", "like3", "like4", "like5"
        ), member[UserEntities.LIKES])
        assertEquals("url1", member[UserEntities.PHOTO_URL])
    }

    @Test
    fun `toEntities should map GroupDTO to GroupEntities correctly`() {
        val members = mapOf(
            "User1" to mapOf(
                GroupEntities.NAME to "User1",
                UserEntities.LIKES to 5,
                UserEntities.PHOTO_URL to "url1"
            )
        )
        val dto = GroupDTO(
            id = "1",
            token = "token",
            name = "GroupName",
            description = "desc",
            members = members,
            draws = mapOf("User1" to "User2"),
            isOwner = true
        )
        val entities = dto.toEntities()
        assertEquals(dto.id, entities.id)
        assertEquals(dto.token, entities.token)
        assertEquals(dto.name, entities.name)
        assertEquals(dto.description, entities.description)
        assertEquals(dto.draws, entities.draws)
        assertEquals(dto.isOwner, entities.isOwner)
        assertEquals(1, entities.members.size)
        val user = entities.members[0]
        assertEquals("User1", user.name)
        assertEquals(5, user.likes.size)
        assertEquals("url1", user.photoUrl)
    }

    @Test
    fun `toEntities should handle invalid member map gracefully`() {
        val members = mapOf(
            "User1" to mapOf(
                GroupEntities.NAME to "User1",
                UserEntities.LIKES to 5,
                UserEntities.PHOTO_URL to "url1"
            )
        )
        val dto = GroupDTO(
            id = "1",
            token = "token",
            name = "GroupName",
            description = "desc",
            members = members,
            draws = emptyMap(),
            isOwner = false
        )
        val entities = dto.toEntities()
        assertTrue(entities.members.isEmpty())
    }
}


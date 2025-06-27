package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import java.util.UUID

class GroupModelTest {
    @Test
    fun `toMap should return correct map`() {
        val user = mockk<UserEntities> {
            every { toMap() } returns mapOf("id" to "user1")
        }
        val group = GroupModel(
            id = "id1",
            token = "token1",
            name = "name1",
            description = "desc1",
            members = listOf(user),
            draws = mapOf("A" to "B"),
            isOwner = true
        )
        val map = group.toMap()
        assertEquals("id1", map[GroupEntities.ID])
        assertEquals("token1", map[GroupEntities.TOKEN])
        assertEquals("name1", map[GroupEntities.NAME])
        assertEquals("desc1", map[GroupEntities.DESCRIPTION])
        assertEquals(listOf(mapOf("id" to "user1")), map[GroupEntities.MEMBERS])
        assertEquals(mapOf("A" to "B"), map[GroupEntities.DRAWS])
        assertEquals(true, map[GroupEntities.IS_OWNER])
    }

    @Test
    fun `toCopy should copy with new values`() {
        val group = GroupModel(
            id = "id1",
            token = "token1",
            name = "name1",
            description = "desc1",
            members = emptyList(),
            draws = emptyMap(),
            isOwner = false
        )
        val copy = group.toCopy(
            token = "token2",
            name = "name2",
            description = "desc2",
            members = emptyList(),
            draws = mapOf("A" to "B"),
            isOwner = true
        )
        assertEquals("token2", copy.token)
        assertEquals("name2", copy.name)
        assertEquals("desc2", copy.description)
        assertEquals(mapOf("A" to "B"), copy.draws)
        assertEquals(true, copy.isOwner)
    }

    @Test
    fun `fromMap should parse all fields correctly`() {
        val memberMap = mapOf("id" to "user1", "name" to "User 1")
        val map = mapOf(
            GroupEntities.ID to "id1",
            GroupEntities.TOKEN to "token1",
            GroupEntities.NAME to "name1",
            GroupEntities.DESCRIPTION to "desc1",
            GroupEntities.MEMBERS to mapOf("0" to memberMap),
            GroupEntities.DRAWS to mapOf("A" to "B"),
            GroupEntities.IS_OWNER to true
        )
        val group = GroupModel.fromMap(map)
        assertEquals("id1", group.id)
        assertEquals("token1", group.token)
        assertEquals("name1", group.name)
        assertEquals("desc1", group.description)
        assertEquals(1, group.members.size)
        assertEquals("user1", group.members[0].id)
        assertEquals(mapOf("A" to "B"), group.draws)
        assertEquals(true, group.isOwner)
    }

    @Test
    fun `fromMap should handle missing fields and invalid members`() {
        val map = mapOf<String, Any>()
        val group = GroupModel.fromMap(map)
        assertNotNull(group.id)
        assertEquals("", group.token)
        assertEquals("", group.name)
        assertNull(group.description)
        assertTrue(group.members.isEmpty())
        assertTrue(group.draws.isEmpty())
        assertFalse(group.isOwner)
    }

    @Test
    fun `create extension should create GroupEntities with correct values`() {
        val group = GroupEntities.create(
            id = "id1",
            token = "token1",
            name = "name1",
            description = "desc1",
            members = emptyList(),
            draws = mapOf("A" to "B"),
            isOwner = true
        )
        assertEquals("id1", group.id)
        assertEquals("token1", group.token)
        assertEquals("name1", group.name)
        assertEquals("desc1", group.description)
        assertEquals(mapOf("A" to "B"), group.draws)
        assertEquals(true, group.isOwner)
    }
}


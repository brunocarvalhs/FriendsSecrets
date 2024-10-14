package br.com.brunocarvalhs.friendssecrets.data.model

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import org.junit.Assert.*
import org.junit.Test
import java.util.UUID

class GroupModelTest {

    @Test
    fun `test default values`() {
        val groupModel = GroupModel()

        assertNotNull(groupModel.id) // Ensure that a UUID is generated
        assertTrue(groupModel.token.isNotEmpty()) // Ensure that a token is generated
        assertEquals("", groupModel.name) // Default name is empty
        assertNull(groupModel.description) // Default description is null
        assertTrue(groupModel.members.isEmpty()) // Default members map is empty
        assertFalse(groupModel.isDraw) // Default isDraw is false
        assertTrue(groupModel.draws.isEmpty()) // Default draws map is empty
        assertFalse(groupModel.isOwner) // Default isOwner is false
    }

    @Test
    fun `test toMap method`() {
        val members = mapOf("user1" to "User One", "user2" to "User Two")
        val groupModel = GroupModel(
            id = UUID.randomUUID().toString(),
            token = "testtoken",
            name = "Test Group",
            description = "A group for testing",
            members = members,
            isDraw = true,
            draws = mapOf("user1" to "user2"),
            isOwner = true // This will be filtered out in toMap
        )

        val resultMap = groupModel.toMap()

        assertTrue(resultMap.containsKey(GroupEntities.ID))
        assertTrue(resultMap.containsKey(GroupEntities.TOKEN))
        assertTrue(resultMap.containsKey(GroupEntities.NAME))
        assertTrue(resultMap.containsKey(GroupEntities.DESCRIPTION))
        assertTrue(resultMap.containsKey(GroupEntities.MEMBERS))
        assertTrue(resultMap.containsKey(GroupEntities.IS_DRAW))
        assertFalse(resultMap.containsKey(GroupEntities.IS_OWNER)) // Ensure the owner key is filtered out
    }

    @Test
    fun `test toCopy method`() {
        val members = mapOf("user1" to "User One")

        val groupModel = GroupModel(
            id = UUID.randomUUID().toString(),
            token = "testtoken",
            name = "Test Group",
            description = "A group for testing",
            members = members,
            isDraw = true,
            draws = mapOf("user1" to "user2"),
            isOwner = false
        )

        val copiedGroupModel = groupModel.toCopy(
            token = "newtoken",
            name = "New Test Group",
            description = "New description",
            members = mapOf("user1" to "User One", "user2" to "User Two"),
            isDraw = false,
            draws = mapOf(),
            isOwner = true
        )

        // Verify that the copied group model has the new values
        assertEquals("newtoken", copiedGroupModel.token)
        assertEquals("New Test Group", copiedGroupModel.name)
        assertEquals("New description", copiedGroupModel.description)
        assertEquals(2, copiedGroupModel.members.size)
        assertFalse(copiedGroupModel.isDraw)
        assertTrue(copiedGroupModel.draws.isEmpty())
        assertTrue(copiedGroupModel.isOwner)
    }
}
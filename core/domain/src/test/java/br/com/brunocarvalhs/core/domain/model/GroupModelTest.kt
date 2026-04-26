package br.com.brunocarvalhs.core.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test


class GroupModelTest {

    @Test
    fun `should create GroupModel with default values`() {
        val group = GroupModel()

        assertNotNull(group.id)
        assertTrue(group.id.isNotBlank())

        assertNotNull(group.token)
        assertTrue(group.token.isNotBlank())

        assertEquals("", group.name)
        assertNull(group.description)
        assertNull(group.date)
        assertNull(group.minPrice)
        assertNull(group.maxPrice)
        assertNull(group.type)

        assertTrue(group.members.isEmpty())
        assertTrue(group.draws.isEmpty())

        assertFalse(group.isOwner)
        assertNull(group.ownerId)
        assertNull(group.photo)

        assertTrue(group.createdAt > 0)
    }

    @Test
    fun `should generate unique ids`() {
        val group1 = GroupModel()
        val group2 = GroupModel()

        assertNotEquals(group1.id, group2.id)
    }

    @Test
    fun `should generate token with correct size and format`() {
        val token = GroupModel.generateToken(10)

        assertEquals(10, token.length)

        // Only uppercase letters and numbers
        assertTrue(token.all { it.isUpperCase() || it.isDigit() })
    }

    @Test
    fun `should create GroupModel with custom values`() {
        val members = listOf(
            UserModel(id = "1", name = "User 1"),
            UserModel(id = "2", name = "User 2")
        )

        val draws = mapOf("1" to "2")

        val group = GroupModel(
            id = "custom-id",
            token = "ABC12345",
            name = "Test Group",
            description = "Description",
            date = "2026-01-01",
            minPrice = 10.0,
            maxPrice = 100.0,
            type = "SECRET",
            members = members,
            draws = draws,
            isOwner = true,
            ownerId = "1",
            photo = "base64string",
            createdAt = 123456789L
        )

        assertEquals("custom-id", group.id)
        assertEquals("ABC12345", group.token)
        assertEquals("Test Group", group.name)
        assertEquals("Description", group.description)
        assertEquals("2026-01-01", group.date)
        assertEquals(10.0, group.minPrice)
        assertEquals(100.0, group.maxPrice)
        assertEquals("SECRET", group.type)

        assertEquals(2, group.members.size)
        assertEquals(draws, group.draws)

        assertTrue(group.isOwner)
        assertEquals("1", group.ownerId)
        assertEquals("base64string", group.photo)
        assertEquals(123456789L, group.createdAt)
    }

    @Test
    fun `should expose correct constant values`() {
        assertEquals("groups", GroupModel.COLLECTION_NAME)
        assertEquals("admins", GroupModel.COLLECTION_NAME_ADMIN)

        assertEquals("id", GroupModel.ID)
        assertEquals("token", GroupModel.TOKEN)
        assertEquals("name", GroupModel.NAME)
        assertEquals("description", GroupModel.DESCRIPTION)
        assertEquals("date", GroupModel.DATE)
        assertEquals("min_price", GroupModel.MIN_PRICE)
        assertEquals("max_price", GroupModel.MAX_PRICE)
        assertEquals("type", GroupModel.TYPE)
        assertEquals("members", GroupModel.MEMBERS)
        assertEquals("draws", GroupModel.DRAWS)
        assertEquals("is_owner", GroupModel.IS_OWNER)
        assertEquals("owner_id", GroupModel.OWNER_ID)
        assertEquals("photo_base64", GroupModel.PHOTO)
        assertEquals("created_at", GroupModel.CREATED_AT)
    }
}

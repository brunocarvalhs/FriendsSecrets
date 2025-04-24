package br.com.brunocarvalhs.friendssecrets.domain.entities

import org.junit.Assert.*
import org.junit.Test

class GroupEntitiesTest {

    @Test
    fun toMap_returnsCorrectMap() {
        val group = object : GroupEntities {
            override val id = "1"
            override val token = "token123"
            override val name = "Group Name"
            override val description = "Group Description"
            override val members = mapOf("member1" to "Member One")
            override val draws = mapOf("draw1" to "Draw One")
            override val isOwner = true

            override fun toMap(): Map<String, Any> {
                return mapOf(
                    GroupEntities.ID to id,
                    GroupEntities.TOKEN to token,
                    GroupEntities.NAME to name,
                    GroupEntities.DESCRIPTION to description,
                    GroupEntities.MEMBERS to members,
                    GroupEntities.DRAWS to draws,
                    GroupEntities.IS_OWNER to isOwner
                )
            }

            override fun toCopy(
                token: String,
                name: String,
                description: String?,
                members: Map<String, String>,
                draws: Map<String, String>,
                isOwner: Boolean
            ): GroupEntities {
                return this
            }
        }

        val expectedMap = mapOf(
            GroupEntities.ID to "1",
            GroupEntities.TOKEN to "token123",
            GroupEntities.NAME to "Group Name",
            GroupEntities.DESCRIPTION to "Group Description",
            GroupEntities.MEMBERS to mapOf("member1" to "Member One"),
            GroupEntities.DRAWS to mapOf("draw1" to "Draw One"),
            GroupEntities.IS_OWNER to true
        )

        assertEquals(expectedMap, group.toMap())
    }
}
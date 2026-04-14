package br.com.brunocarvalhs.group.details.commons.navigation

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GroupDetailsRouterTest {

    @Test
    fun `router should store group correctly`() {
        // Given
        val group = GroupModel(id = "1", name = "Details Test")
        
        // When
        val router = GroupDetailsRouter(group)

        // Then
        assertEquals(group, router.group)
    }

    @Test
    fun `typeMap should contain GroupModel serializer`() {
        // Then
        assertNotNull(GroupDetailsRouter.typeMap)
        assertEquals(1, GroupDetailsRouter.typeMap.size)
    }
}

package br.com.brunocarvalhs.group.draw.commons.navigation

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class DrawGraphRouterTest {

    @Test
    fun `router should store group correctly`() {
        // Given
        val group = GroupModel(id = "1", name = "Draw Test")
        
        // When
        val router = DrawGraphRouter(group)

        // Then
        assertEquals(group, router.group)
    }

    @Test
    fun `typeMap should contain GroupModel serializer`() {
        // Then
        assertNotNull(DrawGraphRouter.typeMap)
        assertEquals(1, DrawGraphRouter.typeMap.size)
    }
}

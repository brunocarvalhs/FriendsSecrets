package br.com.brunocarvalhs.group.details.commons.navigation

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class DetailRouterTest {

    @Test
    fun `router should store group correctly`() {
        // Given
        val group = GroupModel(id = "1", name = "Test")
        
        // When
        val router = DetailRouter(group)

        // Then
        assertEquals(group, router.group)
    }

    @Test
    fun `typeMap should contain GroupModel serializer`() {
        // Then
        assertNotNull(DetailRouter.typeMap)
        assertEquals(1, DetailRouter.typeMap.size)
    }
}

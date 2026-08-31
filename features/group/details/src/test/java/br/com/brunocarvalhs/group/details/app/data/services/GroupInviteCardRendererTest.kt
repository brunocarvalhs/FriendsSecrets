package br.com.brunocarvalhs.group.details.app.data.services

import br.com.brunocarvalhs.core.domain.model.GroupModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GroupInviteCardRendererTest {

    private lateinit var renderer: GroupInviteCardRenderer

    @Before
    fun setup() {
        renderer = GroupInviteCardRenderer(QrCodeGenerator())
    }

    @Test
    fun `render should return a fixed size portrait bitmap`() {
        // Given
        val group = GroupModel(name = "Família", token = "ABC12345")

        // When
        val bitmap = renderer.render(group)

        // Then
        assertEquals(1080, bitmap.width)
        assertEquals(1350, bitmap.height)
    }

    @Test
    fun `render should not throw for a group without a name`() {
        // Given
        val group = GroupModel(name = "", token = "XYZ98765")

        // When
        val bitmap = renderer.render(group)

        // Then
        assertEquals(1080, bitmap.width)
    }
}

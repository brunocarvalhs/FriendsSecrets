package br.com.brunocarvalhs.group.details.app.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.core.domain.model.GroupModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GroupShareUseCaseTest {

    private val context: Context = mockk(relaxed = true)
    private lateinit var useCase: GroupShareUseCase

    @Before
    fun setup() {
        useCase = GroupShareUseCase(context)
    }

    @Test
    fun `invoke should start activity with share intent`() {
        // Given
        val group = GroupModel(name = "Secret Santa", description = "Fun", token = "ABC123")

        // When
        useCase(group)

        // Then
        verify { context.startActivity(any()) }
    }
}

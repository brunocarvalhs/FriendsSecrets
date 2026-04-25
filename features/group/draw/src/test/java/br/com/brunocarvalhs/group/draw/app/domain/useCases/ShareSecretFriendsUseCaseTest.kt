package br.com.brunocarvalhs.group.draw.app.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.core.security.domain.CryptoService
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ShareSecretFriendsUseCaseTest {

    private val context: Context = mockk(relaxed = true)
    private val cryptoManager: CryptoService = mockk(relaxed = true)
    private lateinit var useCase: ShareSecretFriendsUseCase

    @Before
    fun setup() {
        useCase = ShareSecretFriendsUseCase(context, cryptoManager)
    }

    @Test
    fun `invoke should start activity with share intent`() {
        // Given
        val group = GroupModel(name = "Secret Santa", description = "Fun", token = "ABC123")
        val secret = "token_secret"

        // When
        useCase(group, secret)

        // Then
        verify { context.startActivity(any()) }
    }
}

package br.com.brunocarvalhs.group.details.app.domain.useCases

import android.app.Application
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.data.services.QrCodeGenerator
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ShareGroupQrCodeUseCaseTest {

    @Test
    fun `invoke should fail when qr generator returns null`() {
        // Given
        val context: Application = org.robolectric.RuntimeEnvironment.getApplication()
        val failingGenerator: QrCodeGenerator = mockk()
        every { failingGenerator.generate(any(), any()) } returns null
        val useCase = ShareGroupQrCodeUseCase(context, failingGenerator)
        val group = GroupModel(id = "1", token = "ABC12345")

        // When
        val result = useCase(group)

        // Then
        assertTrue(result.isFailure)
    }
}

package br.com.brunocarvalhs.core.review.data

import br.com.brunocarvalhs.storage.domain.StorageService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

private const val KEY_LAST_PROMPTED_AT = "review_prompt_last_shown_at"

class ReviewPromptManagerTest {

    private val storageService: StorageService = mockk()
    private lateinit var manager: ReviewPromptManager

    @Before
    fun setup() {
        manager = ReviewPromptManager(storageService)
    }

    @Test
    fun `shouldPrompt returns true when never prompted before`() = runTest {
        coEvery { storageService.load(KEY_LAST_PROMPTED_AT, Long::class) } returns null

        assertTrue(manager.shouldPrompt())
    }

    @Test
    fun `shouldPrompt returns false within the cooldown window`() = runTest {
        val fiveDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)
        coEvery { storageService.load(KEY_LAST_PROMPTED_AT, Long::class) } returns fiveDaysAgo

        assertFalse(manager.shouldPrompt())
    }

    @Test
    fun `shouldPrompt returns true once the cooldown window has elapsed`() = runTest {
        val fortyDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(40)
        coEvery { storageService.load(KEY_LAST_PROMPTED_AT, Long::class) } returns fortyDaysAgo

        assertTrue(manager.shouldPrompt())
    }

    @Test
    fun `recordPrompted saves the current timestamp`() = runTest {
        coEvery { storageService.save(KEY_LAST_PROMPTED_AT, any<Long>()) } returns Unit

        manager.recordPrompted()

        coVerify { storageService.save(KEY_LAST_PROMPTED_AT, any<Long>()) }
    }
}

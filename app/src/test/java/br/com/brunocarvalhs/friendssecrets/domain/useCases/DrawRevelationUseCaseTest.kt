package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class DrawRevelationUseCaseTest {

    private lateinit var mockContext: CustomApplication
    private lateinit var storage: StorageService
    private lateinit var repository: GroupRepository
    private lateinit var performance: PerformanceManager
    private lateinit var cryptoService: CryptoService
    private lateinit var useCase: DrawRevelationUseCase

    @Before
    fun setup() {
        mockContext = mockk(relaxed = true)
        storage = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        performance = mockk(relaxed = true)
        cryptoService = mockk(relaxed = true)

        mockkObject(CustomApplication)
        every { CustomApplication.getInstance() } returns mockContext
        every { mockContext.getString(any()) } returns "Test String"
        every { performance.start(any()) } just runs
        every { performance.stop(any()) } just runs

        useCase = DrawRevelationUseCase(
            repository = repository,
            performance = performance,
            storage = storage,
            cryptoService = cryptoService
        )
    }

    @Test
    fun `should save code to storage when code is provided`() = runBlocking {
        val id = "test_id"
        val code = "test_code"
        val secretKey = "secret_key$id"

        every { storage.save(secretKey, code) } just runs

        val result = useCase(id, code).getOrNull()

        assertNotNull(result)
    }

    @Test
    fun `should load code from storage when code is not provided`() = runBlocking {
        val id = "test_id"
        val secretKey = "secret_key_$id"
        val savedCode = "saved_code"

        every { storage.load<String>(secretKey) } returns savedCode

        val result = useCase.invoke(id).getOrNull()

        assertNotNull(result)
    }

    @Test
    fun `should return group and decrypted map when secret is valid`() = runBlocking {
        val id = "test_id"
        val code = "test_code"
        val secretKey = "secret_key$id"
        val decryptedSecret = "decrypted_secret"
        val group = mockk<GroupEntities>(relaxed = true)

        every { storage.save(secretKey, code) } just runs
        every { cryptoService.decrypt(code) } returns decryptedSecret
        coEvery { repository.read(id) } returns group
        every { group.members } returns mapOf(decryptedSecret to "member_name")

        val result = useCase(id, code)

        verify { storage.save(secretKey, code) }
        verify { cryptoService.decrypt(code) }
        coVerify { repository.read(id) }
        assertEquals(
            Pair(group, mapOf(decryptedSecret to "member_name")),
            result.getOrNull()
        )
    }

    @Test
    fun `should handle null secret gracefully`() = runBlocking {
        val id = "test_id"
        val secretKey = "secret_key$id"

        every { storage.load<String>(secretKey) } returns null

        val result = useCase(id)

        verify { storage.load<String>(secretKey) }
        assertNull(result.getOrNull())
    }

    @Test
    fun `should start and stop performance tracking`() = runBlocking {
        val id = "test_id"
        val code = "test_code"

        every { performance.start(any()) } just runs
        every { performance.stop(any()) } just runs

        useCase(id, code)

        verify { performance.start(DrawRevelationUseCase::class.java.simpleName) }
        verify { performance.stop(DrawRevelationUseCase::class.java.simpleName) }
    }

    @Test
    fun `should handle exceptions and return failure`() = runBlocking {
        val id = "test_id"
        val exception = RuntimeException("Test exception")

        every { storage.load<String>(any()) } returns "key_value"
        coEvery { repository.read(id) } throws exception

        val result = useCase.invoke(id)

        assert(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
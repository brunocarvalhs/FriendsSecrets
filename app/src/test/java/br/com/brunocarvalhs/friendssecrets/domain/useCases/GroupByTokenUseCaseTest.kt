package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupAlreadyExistException
import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GroupByTokenUseCaseTest {

    private lateinit var repository: GroupRepository
    private lateinit var storage: StorageService
    private lateinit var performance: PerformanceManager
    private lateinit var useCase: GroupByTokenUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        storage = mockk(relaxed = true)
        performance = mockk(relaxed = true)

        every { performance.start(any()) } just runs
        every { performance.stop(any()) } just runs

        useCase = GroupByTokenUseCase(
            groupRepository = repository,
            storage = storage,
            performance = performance
        )
    }

    @Test
    fun `test invoke method`() = runBlocking {
        val token = "testToken"
        every { storage.load<List<String>>(any<String>()) } returns listOf("token")
        coEvery { repository.searchByToken(token) } returns mockk()
        every { storage.save(any<String>(), any<List<String>>()) } returns Unit

        val result = useCase.invoke(token)

        assert(result.isSuccess)
    }

    @Test
    fun `test invoke method with exception`() = runBlocking {
        val token = "testToken"
        every { storage.load<List<String>>(any<String>()) } returns listOf("token")
        coEvery { repository.searchByToken(token) } throws GroupNotFoundException(message = "Group not found")
        every { storage.save(any<String>(), any<List<String>>()) } returns Unit

        val result = useCase.invoke(token)

        assert(result.isFailure)
    }

    @Test
    fun `test invoke method with empty token`() = runBlocking {
        val token = ""
        every { storage.load<List<String>>(any<String>()) } returns listOf("token")
        coEvery { repository.searchByToken(token) } throws GroupNotFoundException(message = "Group not found")

        val result = useCase.invoke(token)

        assert(result.isFailure)
        assert(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `test invoke method with empty storage`() = runBlocking {
        val token = "testToken"
        every { storage.load<List<String>>(any<String>()) } returns emptyList()
        coEvery { repository.searchByToken(token) } returns mockk()
        every { storage.save(any<String>(), any<List<String>>()) } returns Unit

        val result = useCase.invoke(token)

        assert(result.isSuccess)
    }

    @Test
    fun `test invoke method with group already exist`() = runBlocking {
        mockkObject(CustomApplication.Companion)
        every { CustomApplication.getInstance() } returns mockk {
            every { getString(any()) } returns "Group already exist"
        }

        val token = "token"
        every { storage.load<List<String>>(any<String>()) } returns listOf("token")
        coEvery { repository.searchByToken(token) } returns mockk()
        every { storage.save(any<String>(), any<List<String>>()) } returns Unit

        val result = useCase.invoke(token)

        assert(result.isFailure)
        assert(result.exceptionOrNull() is GroupAlreadyExistException)
    }
}
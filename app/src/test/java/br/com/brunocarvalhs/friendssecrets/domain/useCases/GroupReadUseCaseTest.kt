package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GroupReadUseCaseTest {

    private lateinit var mockContext: CustomApplication
    private lateinit var repository: GroupRepository
    private lateinit var storage: StorageService
    private lateinit var performance: PerformanceManager
    private lateinit var useCase: GroupReadUseCase

    @Before
    fun setup() {
        mockContext = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        storage = mockk(relaxed = true)
        performance = mockk(relaxed = true)

        mockkObject(CustomApplication.Companion)
        every { CustomApplication.getInstance() } returns mockContext
        every { mockContext.getString(any()) } returns "Test String"
        every { performance.start(any()) } just runs
        every { performance.stop(any()) } just runs

        useCase = GroupReadUseCase(
            context = mockContext,
            groupRepository = repository,
            storage = storage,
            performance = performance
        )
    }

    @Test
    fun `test invoke method read admin group`() = runBlocking {
        val groupId = "group1"
        val group = mockk<GroupEntities>(relaxed = true) {
            every { token } returns "token"
            every { toCopy(any()) } answers { firstArg() }
        }

        every { storage.load<List<String>>(any<String>()) } returns listOf("token")
        coEvery { repository.read(groupId) } returns group

        val result = useCase.invoke(groupId)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `test invoke method read group`() = runBlocking {
        val groupId = "group1"
        val group = mockk<GroupEntities>(relaxed = true) {
            every { token } returns "token"
            every { toCopy(any()) } answers { firstArg() }
        }

        every { storage.load<List<String>>(any<String>()) } returns null
        coEvery { repository.read(groupId) } returns group

        val result = useCase.invoke(groupId)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `test invoke method with invalid group id`() = runBlocking {
        val groupId = ""
        val group = mockk<GroupEntities>(relaxed = true)
        every { storage.load<List<String>>(any<String>()) } returns null
        coEvery { repository.read(groupId) } returns group

        val result = useCase.invoke(groupId)

        assertTrue(result.isFailure)
    }
}
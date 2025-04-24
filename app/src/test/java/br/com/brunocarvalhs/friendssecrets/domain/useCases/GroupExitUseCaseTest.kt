package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupExitUseCaseTest {

    private lateinit var mockContext: CustomApplication
    private lateinit var storage: StorageService
    private lateinit var repository: GroupRepository
    private lateinit var performance: PerformanceManager
    private lateinit var useCase: GroupExitUseCase

    @Before
    fun setup() {
        mockContext = mockk(relaxed = true)
        storage = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        performance = mockk(relaxed = true)

        mockkObject(CustomApplication)
        every { CustomApplication.getInstance() } returns mockContext
        every { mockContext.getString(R.string.require_group_id_cannot_be_blank) } returns "Group ID cannot be blank"
        every { performance.start(any()) } just runs
        every { performance.stop(any()) } just runs

        useCase = GroupExitUseCase(
            context = mockContext,
            groupRepository = repository,
            performance = performance,
            storage = storage
        )
    }

    @Test
    fun `invoke should throw exception when groupId is blank`() = runBlocking {
        val result = useCase.invoke("")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        verify { mockContext.getString(R.string.require_group_id_cannot_be_blank) }
    }

    @Test
    fun `invoke should clear group and admin when groupId is valid`() = runBlocking {
        val groupId = "validGroupId"
        val group = mockk<GroupEntities> {
            every { token } returns "groupToken"
        }

        coEvery { repository.read(groupId) } returns group
        every { storage.load<List<String>>(GroupEntities.COLLECTION_NAME) } returns listOf("groupToken")
        every { storage.load<List<String>>(GroupEntities.COLLECTION_NAME_ADMINS) } returns listOf("groupToken")
        every { storage.save(any(), any<MutableList<String>>()) } just runs

        val result = useCase.invoke(groupId)

        assertTrue(result.isSuccess)
        coVerify { repository.read(groupId) }
        verify { storage.save(GroupEntities.COLLECTION_NAME, emptyList<String>()) }
        verify { storage.save(GroupEntities.COLLECTION_NAME_ADMINS, emptyList<String>()) }
    }

    @Test
    fun `invoke should handle exception when repository throws error`() = runBlocking {
        val groupId = "validGroupId"

        coEvery { repository.read(groupId) } throws RuntimeException("Repository error")

        val result = useCase.invoke(groupId)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
        verify { performance.start(GroupExitUseCase::class.java.simpleName) }
        verify { performance.stop(GroupExitUseCase::class.java.simpleName) }
    }
}
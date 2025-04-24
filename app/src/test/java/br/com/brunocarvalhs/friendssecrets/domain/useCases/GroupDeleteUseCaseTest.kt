package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupDeleteUseCaseTest {

    private lateinit var mockContext: CustomApplication
    private lateinit var storage: StorageService
    private lateinit var repository: GroupRepository
    private lateinit var performance: PerformanceManager
    private lateinit var useCase: GroupDeleteUseCase

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

        useCase = GroupDeleteUseCase(
            context = mockContext,
            groupRepository = repository,
            performance = performance,
            storage = storage
        )
    }

    @Test
    fun `invoke should delete group successfully`() = runBlocking {
        // Arrange
        val groupId = "validGroupId"
        val group = mockk<GroupEntities>(relaxed = true) {
            every { token } returns "groupToken"
        }
        coEvery { repository.read(groupId) } returns group
        coEvery { repository.delete(groupId) } just runs
        every { storage.load<List<String>>(GroupEntities.COLLECTION_NAME) } returns listOf("groupToken")
        every { storage.load<List<String>>(GroupEntities.COLLECTION_NAME_ADMINS) } returns listOf("groupToken")

        // Act
        val result = useCase.invoke(groupId)

        // Assert
        assertTrue(result.isSuccess)
        coVerify { repository.read(groupId) }
        coVerify { repository.delete(groupId) }
        verify { storage.save(GroupEntities.COLLECTION_NAME, emptyList<String>()) }
        verify { storage.save(GroupEntities.COLLECTION_NAME_ADMINS, emptyList<String>()) }
    }

    @Test()
    fun `invoke should throw exception when groupId is blank`() = runBlocking {
        // Arrange
        val groupId = ""

        // Act
        val result = useCase.invoke(groupId).isFailure

        // Assert
        assertTrue(result)
    }

    @Test
    fun `invoke should handle exception from repository`() = runBlocking {
        // Arrange
        val groupId = "validGroupId"
        coEvery { repository.read(groupId) } throws Exception("Repository error")

        // Act
        val result = useCase.invoke(groupId)

        // Assert
        assertTrue(result.isFailure)
        verify { performance.start(any()) }
        verify { performance.stop(any()) }
    }

    @Test
    fun `clearGroup should remove group token from storage`() = runBlocking {
        // Arrange
        val group = mockk<GroupEntities> {
            every { token } returns "groupToken"
        }
        every { storage.load<List<String>>(GroupEntities.COLLECTION_NAME) } returns listOf("groupToken")

        // Act
        useCase.clearGroup(group)

        // Assert
        verify { storage.save(GroupEntities.COLLECTION_NAME, emptyList<String>()) }
    }

    @Test
    fun `clearAdmin should remove admin token from storage`() {
        // Arrange
        val group = mockk<GroupEntities> {
            every { token } returns "adminToken"
        }
        every { storage.load<List<String>>(GroupEntities.COLLECTION_NAME_ADMINS) } returns listOf("adminToken")

        // Act
        useCase.clearAdmin(group)

        // Assert
        verify { storage.save(GroupEntities.COLLECTION_NAME_ADMINS, emptyList<String>()) }
    }
}
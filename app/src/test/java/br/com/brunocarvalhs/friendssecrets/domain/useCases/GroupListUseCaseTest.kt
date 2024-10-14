package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupListUseCaseTest {

    private lateinit var repository: GroupRepository
    private lateinit var storage: StorageService
    private lateinit var useCase: GroupListUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        storage = mockk(relaxed = true)
        useCase = GroupListUseCase(repository, storage)
    }

    @Test
    fun `test invoke method`() = runBlocking {
        val groupList = listOf("group1", "group2")
        every { storage.load<List<String>>(any()) } returns groupList
        coEvery { repository.list(groupList) } returns listOf(mockk(), mockk())

        val result = useCase.invoke()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `test invoke method with empty storage list`() = runBlocking {
        every { storage.load<List<String>>(any()) } returns emptyList()
        coEvery { repository.list(emptyList()) } returns emptyList()

        val result = useCase.invoke()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() ?: false)
    }

    @Test
    fun `test invoke method with empty group list`() = runBlocking {
        every { storage.load<List<String>>(any()) } returns null

        val result = useCase.invoke()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() ?: false)
    }

    @Test
    fun `test invoke method with repository exception`() = runBlocking {
        val groupList = listOf("group1", "group2")
        every { storage.load<List<String>>(any()) } returns groupList
        coEvery { repository.list(groupList) } throws Exception()

        val result = useCase.invoke()

        assertTrue(result.isFailure)
    }

    @Test
    fun `test invoke method with storage exception`() = runBlocking {
        every { storage.load<List<String>>(any()) } throws Exception()
        coEvery { repository.list(emptyList()) } returns emptyList()

        val result = useCase.invoke()

        assertTrue(result.isFailure)
    }
}
package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GroupCreateUseCaseTest {

    private lateinit var mockContext: CustomApplication
    private lateinit var repository: GroupRepository
    private lateinit var storage: StorageService

    private lateinit var useCase: GroupCreateUseCase

    @Before
    fun setup() {
        mockContext = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        storage = mockk(relaxed = true)

        mockkObject(CustomApplication)
        every { CustomApplication.getInstance() } returns mockContext
        every { mockContext.getString(any()) } returns "Test String"

        useCase = GroupCreateUseCase(
            context = mockContext,
            groupRepository = repository,
            storage = storage
        )
    }

    @Test
    fun `test invoke method with valid input`() = runBlocking {
        val name = "Test Group"
        val description = "This is a test group"
        val members = mapOf(
            "member1" to "Member 1",
            "member2" to "Member 2",
            "member3" to "Member 3",
            "member4" to "Member 4"
        )

        coEvery { repository.searchByToken(any()) } returns null
        every { storage.load<List<String>>(any<String>()) } returns listOf("token")
        every { storage.save(any<String>(), any<List<String>>()) } returns Unit

        val result = useCase.invoke(name, description, members)

        assert(result.isSuccess)
    }

    @Test
    fun `test invoke method with invalid name`() = runBlocking {
        val name = ""
        val description = "This is a test group"
        val members = mapOf(
            "member1" to "Member 1",
            "member2" to "Member 2",
            "member3" to "Member 3",
            "member4" to "Member 4"
        )

        coEvery { repository.searchByToken(any()) } returns null
        every { storage.load<List<String>>(any<String>()) } returns listOf("token")
        every { storage.save(any<String>(), any<List<String>>()) } returns Unit

        val result = useCase.invoke(name, description, members)

        assert(result.isFailure)
        assert(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `test invoke method with invalid members`() = runBlocking {
        val name = "Test Group"
        val description = "This is a test group"
        val members = emptyMap<String, String>()

        coEvery { repository.searchByToken(any()) } returns null
        every { storage.load<List<String>>(any<String>()) } returns listOf("token")
        every { storage.save(any<String>(), any<List<String>>()) } returns Unit

        val result = useCase.invoke(name, description, members)

        assert(result.isFailure)
        assert(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `test invoke method with valid token`() = runBlocking {
        val name = "Test Group"
        val description = "This is a test group"
        val members = mapOf(
            "member1" to "Member 1",
            "member2" to "Member 2",
            "member3" to "Member 3",
            "member4" to "Member 4"
        )

        coEvery { repository.searchByToken(any()) } returns null
        every { storage.load<List<String>>(any<String>()) } returns listOf("token")
        every { storage.save(any<String>(), any<List<String>>()) } returns Unit

        val result = useCase.invoke(name, description, members)

        assert(result.isSuccess)
    }
}
package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GroupDrawUseCaseTest {

    private lateinit var mockContext: CustomApplication
    private lateinit var repository: GroupRepository
    private lateinit var useCase: GroupDrawUseCase

    @Before
    fun setup() {
        mockContext = mockk(relaxed = true)
        repository = mockk(relaxed = true)

        mockkObject(CustomApplication)
        every { CustomApplication.getInstance() } returns mockContext
        every { mockContext.getString(any()) } returns "Test String"

        useCase = GroupDrawUseCase(
            context = mockContext,
            groupRepository = repository
        )
    }

    @Test
    fun `test invoke method with valid input`() = runBlocking {
        val group = mockk<GroupEntities> {
            every { name } returns "Test Group"
            every { description } returns "This is a test group"
            every { draws } returns emptyMap()
            every { members } returns mapOf(
                "member1" to "Member 1",
                "member2" to "Member 2",
                "member3" to "Member 3"
            )
        }

        val result = useCase.invoke(group)

        assert(result.isSuccess)
    }

    @Test
    fun `test invoke method with invalid members`() = runBlocking {
        val group = mockk<GroupEntities> {
            every { name } returns "Test Group"
            every { description } returns "This is a test group"
            every { draws } returns emptyMap()
            every { members } returns emptyMap()
        }

        val result = useCase.invoke(group)

        assert(result.isFailure)
        assert(result.exceptionOrNull() is IllegalArgumentException)
    }
}
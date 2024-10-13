package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.data.service.DrawService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Test

class GroupRepositoryImplTest {

    private val firestore: FirebaseFirestore = mockk(relaxed = true)
    private val drawService: DrawService = mockk(relaxed = true)
    private val groupRepository: GroupRepository = GroupRepositoryImpl(firestore, drawService)

    @Test
    fun `test create group`() = runBlocking {
        val group = mockk<GroupEntities>(relaxed = true) {
            every { id } returns "group_id"
            every { toMap() } returns mockk()
        }

        groupRepository.create(group)

        confirmVerified(firestore)
    }
}
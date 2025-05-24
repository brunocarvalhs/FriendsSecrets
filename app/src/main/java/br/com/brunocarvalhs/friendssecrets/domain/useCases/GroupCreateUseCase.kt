package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.extensions.token
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import kotlin.random.Random

class GroupCreateUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager,
) {

    suspend fun invoke(
        name: String,
        description: String,
        members: List<UserEntities>,
    ): Result<Unit> {
        performance.start(GroupCreateUseCase::class.java.simpleName)
        return try {
            runCatching {
                validateGroupData(name, members)
                val group = generateUniqueGroup(name, description, members)

                groupRepository.create(group)
                persistGroupToken(group.token)
                persistAdminToken(group.token)
            }
        } finally {
            performance.stop(GroupCreateUseCase::class.java.simpleName)
        }
    }

    // 1. Validação de entrada
    private fun validateGroupData(name: String, members: List<UserEntities>) {
        validateName(name)
        validateMembers(members)
    }

    private fun validateName(name: String) {
        require(name.isNotBlank()) {
            context.getString(R.string.require_name_cannot_be_blank)
        }
        require(name.length <= 255) {
            context.getString(R.string.require_name_cannot_be_longer_than_255_characters)
        }
    }

    private fun validateMembers(members: List<UserEntities>) {
        require(members.isNotEmpty()) {
            context.getString(R.string.require_group_must_have_at_least_one_member)
        }
        require(members.size >= 3) {
            context.getString(R.string.require_group_cannot_have_more_than_2_members)
        }
    }

    private suspend fun generateUniqueGroup(
        name: String,
        description: String,
        members: List<UserEntities>
    ): GroupEntities {
        var group: GroupEntities =
            GroupModel(name = name, description = description, members = members)
        do {
            val token = Random.token(size = 8)
            group = group.toCopy(token = token)
        } while (!isTokenUnique(token))
        return group
    }

    private suspend fun isTokenUnique(token: String): Boolean {
        return groupRepository.searchByToken(token) == null
    }

    // 3. Persistência do grupo e admin
    private fun persistGroupToken(token: String) {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME).orEmpty()
        storage.save(
            GroupEntities.COLLECTION_NAME,
            groupList.toMutableList().apply { add(token) }
        )
    }

    private fun persistAdminToken(token: String) {
        val adminList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME_ADMINS).orEmpty()
        storage.save(
            GroupEntities.COLLECTION_NAME_ADMINS,
            adminList.toMutableList().apply { add(token) }
        )
    }
}

package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.extensions.token
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import kotlin.random.Random

class GroupCreateUseCase(
    private val context: Context,
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager,
) {
    suspend fun invoke(
        name: String,
        description: String,
        members: Map<String, String>,
    ): Result<Unit> =
        runCatching {
            performance.start(GroupCreateUseCase::class.java.simpleName)
            validationName(name)
            validationMembers(members)
            var group: GroupEntities =
                GroupModel(name = name, description = description, members = members)
            do {
                group = group.toCopy(token = Random.token(size = 8))
            } while (!validationTokenIsUnique(group.token))
            groupRepository.create(group)
            defineAdmin(group)
            includeGroup(group)
        }.also {
            performance.stop(GroupCreateUseCase::class.java.simpleName)
        }

    private fun validationName(name: String) {
        require(value = name.isNotBlank()) { context.getString(R.string.require_name_cannot_be_blank) }
        require(value = name.length <= 255) { context.getString(R.string.require_name_cannot_be_longer_than_255_characters) }
    }

    private fun validationMembers(members: Map<String, String>) {
        require(value = members.isNotEmpty()) { context.getString(R.string.require_group_must_have_at_least_one_member) }
        require(value = members.size >= 3) { context.getString(R.string.require_group_cannot_have_more_than_2_members) }
    }

    private suspend fun validationTokenIsUnique(token: String): Boolean {
        val group = groupRepository.searchByToken(token)
        return group == null
    }

    private fun includeGroup(group: GroupEntities) {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME)
            ?: emptyList()

        storage.save(
            key = GroupEntities.COLLECTION_NAME,
            value = groupList.toMutableList().apply { add(group.token) }
        )
    }

    private fun defineAdmin(group: GroupEntities) {
        val admins = storage.load<List<String>>(GroupEntities.COLLECTION_NAME_ADMINS)
            ?: emptyList()

        storage.save(
            key = GroupEntities.COLLECTION_NAME_ADMINS,
            value = admins.toMutableList().apply { add(group.token) }
        )
    }
}
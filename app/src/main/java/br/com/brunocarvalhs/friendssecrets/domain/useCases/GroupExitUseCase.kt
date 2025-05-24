package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupExitUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager
) {
    suspend fun invoke(groupId: String): Result<Unit> {
        performance.start(GroupExitUseCase::class.java.simpleName)
        return try {
            runCatching {
                validationGroupId(groupId)
                val group = groupRepository.read(groupId)
                clearGroupToken(group.token)
                clearAdminToken(group.token)
            }
        } finally {
            performance.stop(GroupExitUseCase::class.java.simpleName)
        }
    }

    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank()) { context.getString(R.string.require_group_id_cannot_be_blank) }
    }

    private fun clearGroupToken(token: String) {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()
        if (groupList.contains(token)) {
            storage.save(
                GroupEntities.COLLECTION_NAME,
                groupList.toMutableList().apply { remove(token) }
            )
        }
    }

    private fun clearAdminToken(token: String) {
        val adminList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME_ADMINS) ?: emptyList()
        if (adminList.contains(token)) {
            storage.save(
                GroupEntities.COLLECTION_NAME_ADMINS,
                adminList.toMutableList().apply { remove(token) }
            )
        }
    }
}

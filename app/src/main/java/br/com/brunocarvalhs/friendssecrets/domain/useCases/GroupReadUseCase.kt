package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupReadUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager
) {
    suspend fun invoke(groupId: String): Result<GroupEntities> {
        performance.start(GroupReadUseCase::class.java.simpleName)
        return try {
            runCatching {

                validationGroupId(groupId)
                val group = groupRepository.read(groupId)
                defineAdmin(group)
            }
        } finally {
            performance.stop(GroupReadUseCase::class.java.simpleName)
        }
    }

    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank()) { context.getString(R.string.require_group_id_cannot_be_blank) }
    }

    private fun defineAdmin(group: GroupEntities): GroupEntities {
        val adminList = storage.load<List<String>>(key = GroupEntities.COLLECTION_NAME_ADMINS)
            ?: emptyList()
        return if (adminList.contains(group.token)) group.toCopy(isOwner = true) else group
    }
}
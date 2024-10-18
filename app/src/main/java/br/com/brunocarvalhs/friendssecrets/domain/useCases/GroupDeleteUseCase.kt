package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupDeleteUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
) {
    suspend fun invoke(groupId: String): Result<Unit> = runCatching {
        validationGroupId(groupId)
        val group = groupRepository.read(groupId)
        clearGroup(group)
        clearAdmin(group)
        groupRepository.delete(groupId)
    }

    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank()) { context.getString(R.string.require_group_id_cannot_be_blank) }
    }

    private fun clearGroup(group: GroupEntities) {
        val list = storage.load<List<String>>(key = GroupEntities.COLLECTION_NAME)
            ?: emptyList()

        if (list.contains(group.token)) {
            storage.save(GroupEntities.COLLECTION_NAME, list.toMutableList().apply { remove(group.token) })
        }
    }

    private fun clearAdmin(group: GroupEntities) {
        val adminList = storage.load<List<String>>(key = GroupEntities.COLLECTION_NAME_ADMINS)
            ?: emptyList()
        if (adminList.contains(group.token)) {
            storage.save(GroupEntities.COLLECTION_NAME_ADMINS, adminList.toMutableList().apply { remove(group.token) })
        }
    }
}
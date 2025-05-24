package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupDeleteUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager
) {
    suspend fun invoke(groupId: String): Result<Unit> {
        performance.start(GroupDeleteUseCase::class.java.simpleName)
        return try {
            runCatching {
                validateGroupId(groupId)
                val group = groupRepository.read(groupId)

                removeGroupFromStorage(group.token)
                removeAdminFromStorage(group.token)

                groupRepository.delete(groupId)
            }
        } finally {
            performance.stop(GroupDeleteUseCase::class.java.simpleName)
        }
    }

    // Validação de entrada
    private fun validateGroupId(groupId: String) {
        require(groupId.isNotBlank()) {
            context.getString(R.string.require_group_id_cannot_be_blank)
        }
    }

    // Limpeza do token do grupo do armazenamento local
    private fun removeGroupFromStorage(token: String) {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME).orEmpty()
        if (token in groupList) {
            storage.save(
                GroupEntities.COLLECTION_NAME,
                groupList.toMutableList().apply { remove(token) }
            )
        }
    }

    // Limpeza do token do grupo da lista de administradores
    private fun removeAdminFromStorage(token: String) {
        val adminList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME_ADMINS).orEmpty()
        if (token in adminList) {
            storage.save(
                GroupEntities.COLLECTION_NAME_ADMINS,
                adminList.toMutableList().apply { remove(token) }
            )
        }
    }
}

package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.SessionService
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupReadUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val session: SessionService<UserEntities>,
    private val performance: PerformanceManager
) {
    suspend fun invoke(groupId: String): Result<GroupEntities> = runCatching {
        performance.start(GroupReadUseCase::class.java.simpleName)

        // Valida o ID do grupo
        validationGroupId(groupId)

        // Busca o grupo no repositório
        val group = groupRepository.read(groupId)

        // Define se o usuário atual é administrador do grupo
        defineAdmin(group)
    }.also {
        performance.stop(GroupReadUseCase::class.java.simpleName)
    }

    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank()) { context.getString(R.string.require_group_id_cannot_be_blank) }
    }

    private fun defineAdmin(group: GroupEntities): GroupEntities {
        // Carrega a lista de administradores do armazenamento
        val storageAdminList =
            storage.load<List<String>>(key = GroupEntities.COLLECTION_NAME_ADMINS) ?: emptyList()

        // Combina os administradores do armazenamento com os do usuário atual
        val combinedAdminList = session.getCurrentUser()?.adminGroups?.let { userAdminGroups ->
            (storageAdminList + userAdminGroups).distinct() // Garante que não haverá duplicados
        } ?: storageAdminList

        // Verifica se o grupo atual está na lista de administradores
        return if (combinedAdminList.contains(group.token)) {
            group.toCopy(isOwner = true) // Marca como proprietário
        } else {
            group
        }
    }
}

package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.SessionService
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupListUseCase(
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val session: SessionService<UserEntities>,
    private val performance: PerformanceManager
) {
    suspend fun invoke(): Result<List<GroupEntities>> = runCatching {
        performance.start(GroupListUseCase::class.java.simpleName)

        // Carrega a lista de grupos do armazenamento
        val storageGroupList: List<String> =
            storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()

        // Adiciona os grupos do usuário atual
        val combinedGroupList = session.getCurrentUser()?.groups?.let { userGroups ->
            (storageGroupList + userGroups).distinct() // Garante que não haverá duplicados
        } ?: storageGroupList

        // Se a lista combinada estiver vazia, retorna uma lista vazia
        if (combinedGroupList.isEmpty()) return Result.success(emptyList())

        // Busca os grupos no repositório
        list(combinedGroupList)
    }.also {
        performance.stop(GroupListUseCase::class.java.simpleName)
    }

    private suspend fun list(data: List<String>): List<GroupEntities> {
        return groupRepository.list(data)
    }
}
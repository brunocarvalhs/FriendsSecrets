package br.com.brunocarvalhs.group.details.app.data.repository

import br.com.brunocarvalhs.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.core.network.domain.NetworkService
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.data.exceptions.GroupDeleteException
import br.com.brunocarvalhs.group.details.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.details.app.data.model.GroupDetailsDTO
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GroupDetailsRepositoryImpl @Inject constructor(
    private val network: NetworkService,
) : GroupDetailsRepository {

    override suspend fun read(groupId: String): GroupModel = withContext(Dispatchers.IO) {
        val response = network.make(
            request = NetworkRequest(
                endpoint = "${GroupModel.COLLECTION_NAME}/$groupId",
                method = NetworkService.Method.GET
            ),
            response = GroupDetailsDTO::class
        )
        return@withContext response?.toDomain() ?: throw GroupNotFoundException()
    }

    override suspend fun delete(group: GroupModel): Unit = withContext(Dispatchers.IO) {
        val response = network.make(
            request = NetworkRequest(
                endpoint = "${GroupModel.COLLECTION_NAME}/${group.id}",
                method = NetworkService.Method.DELETE
            ),
            response = Boolean::class
        ) ?: throw GroupNotFoundException()
        if (!response) throw GroupDeleteException()
        return@withContext
    }
}

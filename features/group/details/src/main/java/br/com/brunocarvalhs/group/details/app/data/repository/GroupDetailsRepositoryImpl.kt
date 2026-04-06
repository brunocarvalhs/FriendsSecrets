package br.com.brunocarvalhs.group.details.app.data.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import br.com.brunocarvalhs.group.details.app.data.exceptions.GroupDeleteException
import br.com.brunocarvalhs.group.details.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.details.app.data.model.GroupDetailsDTO
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupDetailsRepositoryImpl @Inject constructor(
    private val network: NetworkService,
) : GroupDetailsRepository {

    override suspend fun read(groupId: String): GroupModel = withContext(Dispatchers.IO) {
        val response = network.make(
            endpoint = "groups",
            method = NetworkService.Method.GET,
            query = mapOf("id" to groupId),
            clazz = GroupDetailsDTO::class
        )
        return@withContext response?.toDomain() ?: throw GroupNotFoundException()
    }

    override suspend fun delete(groupId: String): Unit = withContext(Dispatchers.IO) {
        val response = network.make(
            endpoint = "groups",
            method = NetworkService.Method.DELETE,
            query = mapOf("id" to groupId),
            clazz = Boolean::class
        )
        if (response == false) throw GroupDeleteException()
        return@withContext
    }
}

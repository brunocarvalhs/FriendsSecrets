package br.com.brunocarvalhs.group.list.app.data.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import br.com.brunocarvalhs.group.list.app.data.model.GroupListDTO
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupListRepositoryImpl @Inject constructor(
    private val network: NetworkService,
) : GroupListRepository {

    override suspend fun list(groupTokens: List<String>): List<GroupListDTO> {
        return withContext(Dispatchers.IO) {
            val response = network.make(
                endpoint = "groups",
                query = mapOf(GroupModel.TOKEN to groupTokens),
                method = NetworkService.Method.GET,
                clazz = Array<GroupListDTO>::class
            )

            return@withContext response?.toList() ?: emptyList()
        }
    }

    override suspend fun searchByToken(token: String): GroupListDTO? {
        return withContext(Dispatchers.IO) {
            val response = network.make(
                endpoint = "groups",
                query = mapOf(GroupModel.TOKEN to token),
                method = NetworkService.Method.GET,
                clazz = GroupListDTO::class
            )

            return@withContext response
        }
    }
}

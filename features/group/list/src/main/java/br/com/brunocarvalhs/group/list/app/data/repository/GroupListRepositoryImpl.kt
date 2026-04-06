package br.com.brunocarvalhs.group.list.app.data.repository

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities.Companion.TOKEN
import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.list.app.domain.model.GroupModel
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.list.app.domain.services.GroupDrawService
import br.com.brunocarvalhs.group.list.commons.providers.GroupListCrypto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupListRepositoryImpl @Inject constructor(
    private val network: NetworkService,
) : GroupListRepository {

    override suspend fun list(groupTokens: List<String>): List<GroupModel> {
        return withContext(Dispatchers.IO) {
            val response = network.make(
                endpoint = COLLECTION_NAME,
                query = mapOf(TOKEN to groupTokens),
                method = NetworkService.Method.GET,
                clazz = Array<GroupModel>::class
            )

            return@withContext response?.toList() ?: emptyList()
        }
    }

    override suspend fun searchByToken(token: String): GroupModel? {
        return withContext(Dispatchers.IO) {
            val response = network.make(
                endpoint = COLLECTION_NAME,
                query = mapOf(TOKEN to token),
                method = NetworkService.Method.GET,
                clazz = GroupModel::class
            )

            return@withContext response
        }
    }
}

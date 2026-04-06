package br.com.brunocarvalhs.group.create.app.data.repository

import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import br.com.brunocarvalhs.group.create.app.domain.model.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupCreateRepositoryImpl @Inject constructor(
    private val network: NetworkService,
) : GroupCreateRepository {

    override suspend fun create(group: GroupModel) = withContext(Dispatchers.IO) {
        return@withContext network.make(
            endpoint = COLLECTION_NAME,
            payload = group.toMap(),
            method = NetworkService.Method.POST,
            clazz = GroupModel::class
        )
    }

    companion object {
        const val COLLECTION_NAME = "groups"
    }
}
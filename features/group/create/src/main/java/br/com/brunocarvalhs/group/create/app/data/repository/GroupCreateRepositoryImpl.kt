package br.com.brunocarvalhs.group.create.app.data.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import br.com.brunocarvalhs.group.create.app.data.exceptions.FailedCreateGroupException
import br.com.brunocarvalhs.group.create.app.data.model.GroupCreateDTO
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GroupCreateRepositoryImpl @Inject constructor(
    private val network: NetworkService,
) : GroupCreateRepository {

    override suspend fun create(group: GroupModel): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val dto = GroupCreateDTO.fromDomain(group)

            network.make(
                endpoint = "groups",
                payload = dto.toMap(),
                method = NetworkService.Method.POST,
                clazz = GroupCreateDTO::class
            ) ?: throw FailedCreateGroupException()

            Unit
        }
    }

    override suspend fun update(group: GroupModel): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val dto = GroupCreateDTO.fromDomain(group)

            network.make(
                endpoint = "groups/" + group.id,
                payload = dto.toMap(),
                method = NetworkService.Method.PUT,
                clazz = GroupCreateDTO::class
            ) ?: throw FailedCreateGroupException()

            Unit
        }
    }
}

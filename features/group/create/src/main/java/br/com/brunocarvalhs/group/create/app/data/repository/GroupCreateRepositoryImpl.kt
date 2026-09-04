package br.com.brunocarvalhs.group.create.app.data.repository

import br.com.brunocarvalhs.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.core.network.domain.NetworkService
import br.com.brunocarvalhs.core.domain.model.GroupModel
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

            // Firestore's create returns the new document id, not the full
            // document body - decoding it as GroupCreateDTO always failed,
            // making every successful create look like a failure.
            network.make(
                request = NetworkRequest(
                    endpoint = "groups",
                    payload = dto.toMap(),
                    method = NetworkService.Method.POST
                ),
                response = String::class
            ) ?: throw FailedCreateGroupException()

            Unit
        }
    }

    override suspend fun update(group: GroupModel): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val dto = GroupCreateDTO.fromDomain(group)

            // Firestore's update returns a plain write confirmation, not the
            // full document body - decoding it as GroupCreateDTO always
            // failed, making every successful update look like a failure.
            network.make(
                request = NetworkRequest(
                    endpoint = "groups/" + group.id,
                    payload = dto.toMap(),
                    method = NetworkService.Method.PUT
                ),
                response = Boolean::class
            ) ?: throw FailedCreateGroupException()

            Unit
        }
    }
}

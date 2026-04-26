package br.com.brunocarvalhs.group.draw.app.data.repository

import br.com.brunocarvalhs.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.core.network.domain.NetworkService
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.draw.app.data.model.GroupDrawDTO
import br.com.brunocarvalhs.group.draw.app.data.services.DrawManager
import br.com.brunocarvalhs.group.draw.app.domain.repository.DrawRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DrawRepositoryImpl @Inject constructor(
    private val network: NetworkService,
    private val drawService: DrawManager,
) : DrawRepository {
    override suspend fun drawMembers(group: GroupModel): Map<String, String> =
        withContext(Dispatchers.IO) {
            val members = group.members.map { it.name }.toMutableList()
            val secretSantaMap = drawService.draw(members)

            val payload = GroupDrawDTO
                .fromDomain(model = group)
                .copy(draws = secretSantaMap)

            network.make(
                request = NetworkRequest(
                    endpoint = "groups/${group.id}",
                    payload = payload.toMap(),
                    method = NetworkService.Method.PUT
                ),
                response = GroupDrawDTO::class
            )

            return@withContext payload.draws
        }
}

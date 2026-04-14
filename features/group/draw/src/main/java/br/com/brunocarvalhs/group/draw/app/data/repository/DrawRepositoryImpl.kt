package br.com.brunocarvalhs.group.draw.app.data.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
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
                endpoint = "groups/${group.id}",
                payload = payload.toMap(),
                method = NetworkService.Method.PUT,
                clazz = GroupDrawDTO::class
            )

            return@withContext payload.draws
        }
}
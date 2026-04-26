package br.com.brunocarvalhs.group.list.app.data.repository

import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.core.network.domain.NetworkService
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.GroupModel.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.group.list.app.data.model.GroupListDTO
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GroupListRepositoryImpl @Inject constructor(
    private val network: NetworkService,
    private val device: DeviceService
) : GroupListRepository {

    override suspend fun list(groupTokens: List<String>): List<GroupListDTO> {
        if (groupTokens.isEmpty()) return emptyList()
        val device = device.getDeviceId()
        return withContext(Dispatchers.IO) {
            val admin = network.make(
                request = NetworkRequest(
                    endpoint = COLLECTION_NAME,
                    query = mapOf(
                        GroupModel.OWNER_ID to device
                    ),
                    method = NetworkService.Method.GET
                ),
                response = Array<GroupListDTO>::class
            )?.toMutableList().orEmpty()

            val list = network.make(
                request = NetworkRequest(
                    endpoint = COLLECTION_NAME,
                    query = mapOf(
                        GroupModel.TOKEN to groupTokens,
                    ),
                    method = NetworkService.Method.GET
                ),
                response = Array<GroupListDTO>::class
            )?.toMutableList().orEmpty()

            return@withContext (admin + list).distinctBy { it.id }
        }
    }

    override suspend fun searchByToken(token: String): GroupListDTO? {
        return withContext(Dispatchers.IO) {
            val response = network.make(
                request = NetworkRequest(
                    endpoint = COLLECTION_NAME,
                    query = mapOf(GroupModel.TOKEN to token),
                    method = NetworkService.Method.GET
                ),
                response = Array<GroupListDTO>::class
            )

            return@withContext response?.firstOrNull()
        }
    }
}

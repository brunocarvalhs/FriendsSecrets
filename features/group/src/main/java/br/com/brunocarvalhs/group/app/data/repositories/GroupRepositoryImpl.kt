package br.com.brunocarvalhs.group.app.data.repositories

import br.com.brunocarvalhs.friendssecrets.domain.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.app.data.extensions.toDTO
import br.com.brunocarvalhs.group.app.data.extensions.toEntities
import br.com.brunocarvalhs.group.app.data.model.GroupModel
import br.com.brunocarvalhs.group.app.data.repositories.dto.GroupDTO
import br.com.brunocarvalhs.group.app.domain.repositories.GroupRepository
import br.com.brunocarvalhs.group.app.domain.services.DrawService
import br.com.brunocarvalhs.group.commons.providers.datasources.GroupDataSource
import br.com.brunocarvalhs.group.commons.providers.security.CryptoService
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GroupRepositoryImpl @Inject constructor(
    private val dataSource: GroupDataSource,
    private val cryptoService: CryptoService,
    private val drawService: DrawService,
) : GroupRepository {

    @AddTrace(name = "GroupRepositoryImpl.create", enabled = true)
    override suspend fun create(group: GroupEntities): Unit = withContext(Dispatchers.IO) {
        val payload = group.toDTO()

        val data = cryptoService.encryptMap(
            inputMap = payload.toMap(),
            excludedKeys = setOf(GroupEntities.TOKEN, GroupEntities.ID)
        )

        dataSource.save(data)
    }

    @AddTrace(name = "GroupRepositoryImpl.read", enabled = true)
    override suspend fun read(groupId: String): GroupEntities = withContext(Dispatchers.IO) {
        val encryptedData = dataSource.findById(groupId) ?: throw GroupNotFoundException()
        val decryptedData = cryptoService.decryptMap(
            encryptedData,
            setOf(GroupEntities.TOKEN, GroupEntities.ID)
        )
        GroupDTO.fromMap(decryptedData).toEntities()
    }

    @AddTrace(name = "GroupRepositoryImpl.update", enabled = true)
    override suspend fun update(group: GroupEntities): Unit = withContext(Dispatchers.IO) {
        val payload = group.toDTO()

        val data = cryptoService.encryptMap(
            payload.toMap(),
            setOf(GroupEntities.TOKEN, GroupEntities.ID)
        )

        dataSource.save(data)
    }

    @AddTrace(name = "GroupRepositoryImpl.delete", enabled = true)
    override suspend fun delete(groupId: String): Unit = withContext(Dispatchers.IO) {
        dataSource.delete(groupId)
    }

    @AddTrace(name = "GroupRepositoryImpl.list", enabled = true)
    override suspend fun list(list: List<String>): List<GroupEntities> =
        withContext(Dispatchers.IO) {
            val encryptedGroups = dataSource.listByTokens(list)

            encryptedGroups.map { encryptedData ->
                val decryptedData = cryptoService.decryptMap(
                    encryptedData,
                    setOf(GroupEntities.TOKEN, GroupEntities.ID)
                )
                GroupDTO.fromMap(decryptedData).toEntities()
            }
        }

    @AddTrace(name = "GroupRepositoryImpl.searchByToken", enabled = true)
    override suspend fun searchByToken(token: String): GroupEntities? {
        val encryptedData = dataSource.findByToken(token) ?: return null

        val decryptedData = cryptoService.decryptMap(
            encryptedData,
            setOf(GroupEntities.TOKEN, GroupEntities.ID)
        )
        return GroupDTO.fromMap(decryptedData).toEntities()
    }

    @AddTrace(name = "GroupRepositoryImpl.drawMembers", enabled = true)
    override suspend fun drawMembers(group: GroupEntities) {
        dataSource.runTransaction { transaction ->
            val data = transaction.get(group.id) ?: return@runTransaction
            val currentGroupData = cryptoService.decryptMap(
                data,
                setOf(GroupEntities.TOKEN, GroupEntities.ID)
            )
            val currentGroup = GroupModel.fromMap(currentGroupData)

            val secretSantaMap =
                drawService.drawMembers(currentGroup.members.map { it.name }.toMutableList())

            transaction.update(group.id, mapOf(GroupEntities.DRAWS to secretSantaMap))
        }
    }
}

package br.com.brunocarvalhs.friendssecrets.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.response.GroupResponse

interface GroupRepository {
    suspend fun create(group: GroupEntities)
    suspend fun read(groupId: String): GroupResponse
    suspend fun update(group: GroupEntities)
    suspend fun delete(groupId: String)
    suspend fun list(list: List<String>): List<GroupResponse>
    suspend fun drawMembers(group: GroupEntities)
    suspend fun searchByToken(token: String): GroupResponse?
}
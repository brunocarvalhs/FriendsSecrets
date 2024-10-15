package br.com.brunocarvalhs.friendssecrets.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities

interface GroupRepository {
    suspend fun create(group: GroupEntities)
    suspend fun read(groupId: String): GroupEntities
    suspend fun update(group: GroupEntities)
    suspend fun delete(groupId: String)
    suspend fun list(list: List<String>): List<GroupEntities>
    suspend fun drawMembers(group: GroupEntities)
    suspend fun searchByToken(token: String): GroupEntities?
}
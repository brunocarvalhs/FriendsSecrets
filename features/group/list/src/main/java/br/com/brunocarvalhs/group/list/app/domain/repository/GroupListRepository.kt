package br.com.brunocarvalhs.group.list.app.domain.repository

import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel

interface GroupListRepository {
    suspend fun list(groupTokens: List<String>): List<GroupModel>
    suspend fun read(groupId: String): GroupModel
    suspend fun searchByToken(token: String): GroupModel?
    suspend fun delete(groupId: String)
}
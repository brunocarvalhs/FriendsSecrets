package br.com.brunocarvalhs.group.list.app.domain.repository

import br.com.brunocarvalhs.group.list.app.domain.model.GroupModel

interface GroupListRepository {
    suspend fun list(groupTokens: List<String>): List<GroupModel>
    suspend fun searchByToken(token: String): GroupModel?
}
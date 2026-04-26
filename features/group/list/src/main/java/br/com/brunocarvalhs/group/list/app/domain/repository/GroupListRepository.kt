package br.com.brunocarvalhs.group.list.app.domain.repository

import br.com.brunocarvalhs.group.list.app.data.model.GroupListDTO

internal interface GroupListRepository {
    suspend fun list(groupTokens: List<String>): List<GroupListDTO>
    suspend fun searchByToken(token: String): GroupListDTO?
}

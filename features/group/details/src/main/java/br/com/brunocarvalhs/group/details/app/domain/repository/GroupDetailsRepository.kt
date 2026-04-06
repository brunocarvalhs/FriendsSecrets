package br.com.brunocarvalhs.group.details.app.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel

interface GroupDetailsRepository {
    suspend fun read(groupId: String): GroupModel
    suspend fun delete(groupId: String)
}

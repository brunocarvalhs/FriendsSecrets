package br.com.brunocarvalhs.group.details.app.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel

internal interface GroupDetailsRepository {
    suspend fun read(groupId: String): GroupModel
    suspend fun delete(group: GroupModel)
}

package br.com.brunocarvalhs.group.details.app.domain.repository

import br.com.brunocarvalhs.core.domain.model.GroupModel

internal interface GroupDetailsRepository {
    suspend fun read(groupId: String): GroupModel
    suspend fun delete(group: GroupModel)
    suspend fun update(group: GroupModel): GroupModel
}

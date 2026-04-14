package br.com.brunocarvalhs.group.draw.app.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel

internal interface DrawRepository {
    suspend fun drawMembers(group: GroupModel): Map<String, String>
}

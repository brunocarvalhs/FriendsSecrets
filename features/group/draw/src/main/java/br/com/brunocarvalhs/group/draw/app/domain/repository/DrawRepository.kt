package br.com.brunocarvalhs.group.draw.app.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel

interface DrawRepository {
    suspend fun drawMembers(group: GroupModel)
}

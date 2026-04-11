package br.com.brunocarvalhs.group.edit.app.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel

interface GroupEditRepository {
    suspend fun update(group: GroupModel): GroupModel
}
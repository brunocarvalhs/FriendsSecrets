package br.com.brunocarvalhs.group.create.app.domain.repositories

import br.com.brunocarvalhs.group.create.app.domain.entities.GroupModel

interface GroupCreateRepository {
    suspend fun create(group: GroupModel)
}
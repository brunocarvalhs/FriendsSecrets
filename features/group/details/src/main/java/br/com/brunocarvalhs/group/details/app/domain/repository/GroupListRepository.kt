package br.com.brunocarvalhs.group.details.app.domain.repository

import br.com.brunocarvalhs.group.details.app.domain.entities.GroupModel

interface GroupDetailsRepository {
    suspend fun read(groupId: String): GroupModel
    suspend fun delete(groupId: String)
}
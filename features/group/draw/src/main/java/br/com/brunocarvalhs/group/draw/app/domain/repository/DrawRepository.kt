package br.com.brunocarvalhs.group.draw.app.domain.repository

import br.com.brunocarvalhs.core.domain.model.GroupModel

internal interface DrawRepository {
    suspend fun drawMembers(group: GroupModel): Map<String, String>
}

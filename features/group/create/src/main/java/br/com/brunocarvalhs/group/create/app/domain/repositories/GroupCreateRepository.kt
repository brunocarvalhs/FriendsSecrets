package br.com.brunocarvalhs.group.create.app.domain.repositories

import br.com.brunocarvalhs.core.domain.model.GroupModel

internal interface GroupCreateRepository {
    suspend fun create(group: GroupModel): Result<Unit>
    suspend fun update(group: GroupModel): Result<Unit>
}

package br.com.brunocarvalhs.group.draw.app.data.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.draw.app.domain.repository.DrawRepository
import javax.inject.Inject

class DrawRepositoryImpl @Inject constructor(): DrawRepository {
    override suspend fun drawMembers(group: GroupModel) {

    }
}
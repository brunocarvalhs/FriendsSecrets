package br.com.brunocarvalhs.group.details.app.domain.services

import br.com.brunocarvalhs.core.domain.model.GroupModel

internal interface GroupReminderService {
    fun schedule(group: GroupModel): Boolean
    fun cancel(group: GroupModel)
}

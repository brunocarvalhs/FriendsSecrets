package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.domain.services.GroupReminderService
import br.com.brunocarvalhs.storage.domain.StorageService
import javax.inject.Inject

internal class ToggleGroupReminderUseCase @Inject constructor(
    private val reminderService: GroupReminderService,
    private val storage: StorageService
) {
    suspend operator fun invoke(group: GroupModel, enabled: Boolean): Result<Boolean> =
        runCatching {
            if (enabled) {
                val scheduled = reminderService.schedule(group)
                storage.save(groupReminderStorageKey(group.id), scheduled)
                scheduled
            } else {
                reminderService.cancel(group)
                storage.save(groupReminderStorageKey(group.id), false)
                false
            }
        }
}

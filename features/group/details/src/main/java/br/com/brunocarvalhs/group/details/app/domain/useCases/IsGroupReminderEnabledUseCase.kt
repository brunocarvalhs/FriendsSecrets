package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.storage.domain.StorageService
import javax.inject.Inject

internal class IsGroupReminderEnabledUseCase @Inject constructor(
    private val storage: StorageService
) {
    suspend operator fun invoke(groupId: String): Boolean {
        return storage.load(groupReminderStorageKey(groupId), Boolean::class) ?: false
    }
}

package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.storage.domain.StorageService
import com.google.firebase.perf.metrics.AddTrace
import javax.inject.Inject

class IdentifyUserUseCase @Inject constructor(
    private val storageService: StorageService
) {
    companion object {
        private const val USER_NICKNAME_KEY = "user_nickname_cache"
    }

    @AddTrace(name = "IdentifyUserUseCase.saveNickname", enabled = true)
    suspend fun saveNickname(name: String) {
        storageService.save(USER_NICKNAME_KEY, name)
    }

    @AddTrace(name = "IdentifyUserUseCase.getNickname", enabled = true)
    suspend fun getNickname(): String? {
        return storageService.load(USER_NICKNAME_KEY, String::class)
    }
}

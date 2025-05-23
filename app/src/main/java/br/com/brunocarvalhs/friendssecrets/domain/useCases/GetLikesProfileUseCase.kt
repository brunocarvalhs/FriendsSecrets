package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import com.google.firebase.perf.metrics.AddTrace

class GetLikesProfileUseCase(
    private val sessionManager: SessionManager = SessionManager.getInstance(),
    private val userRepository: UserRepository
) {

    /**
     * This method is used to get the likes of the current user.
     * @return A [Result] object containing the result of the operation.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GetLikesProfileUseCase.invoke")
    suspend operator fun invoke(): Result<UserEntities> = kotlin.runCatching {
        val user = sessionManager.getCurrentUserModel() ?: throw Exception("User not found")
        userRepository.getUserById(user.id) ?: throw Exception("User not found")
    }
}
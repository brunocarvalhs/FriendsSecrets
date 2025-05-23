package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import com.google.firebase.perf.metrics.AddTrace

class DeleteAccountUseCase(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager = SessionManager.getInstance()
) {

    /**
     * This method is used to delete the current user account.
     * @return A [Result] object containing the result of the operation.
     */
    @Throws(Exception::class)
    @AddTrace(name = "DeleteAccountUseCase.invoke")
    suspend operator fun invoke(): Result<Unit> = kotlin.runCatching {
        val user = sessionManager.getCurrentUserModel() ?: throw Exception("User not found")
        userRepository.deleteUser(user.id)
        sessionManager.deleteAccount()
    }
}
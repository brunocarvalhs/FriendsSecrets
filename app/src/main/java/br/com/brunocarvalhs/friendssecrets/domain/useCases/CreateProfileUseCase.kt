package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import com.google.firebase.perf.metrics.AddTrace

class CreateProfileUseCase(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository
) {

    /**
     * This method is used to create a new profile.
     * @param name The name of the user.
     * @param photoUrl The photo url of the user.
     * @param likes The list of likes of the user.
     * @return A [Result] object containing the result of the operation.
     */
    @Throws(Exception::class)
    @AddTrace(name = "CreateProfileUseCase.invoke")
    suspend operator fun invoke(name: String, photoUrl: String, likes: List<String> = emptyList()): Result<Unit> {
        return try {
            sessionManager.updateUserProfile(name, photoUrl)
            sessionManager.getCurrentUserModel()?.let { user ->
                userRepository.updateUser(
                    user.toCopy(
                        name = name,
                        photoUrl = photoUrl,
                        likes = likes.isNotEmpty().let { likes }
                    )
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
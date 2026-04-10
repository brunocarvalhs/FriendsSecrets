package br.com.brunocarvalhs.group.edit.app.domain.useCases

internal class GroupEditUseCase(
    private val repository: GroupRepository
) {
    suspend fun invoke(group: GroupEntities): Result<GroupEntities> {
        return try {
            runCatching {
                repository.update(group)
                group
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
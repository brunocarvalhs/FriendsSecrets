package br.com.brunocarvalhs.group.app.domain.exceptions

data class GroupAlreadyExistException(
    override val message: String = "Group already exist"
) : Exception(message)

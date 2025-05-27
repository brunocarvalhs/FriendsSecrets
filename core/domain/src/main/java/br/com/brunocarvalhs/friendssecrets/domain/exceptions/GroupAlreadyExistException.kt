package br.com.brunocarvalhs.friendssecrets.domain.exceptions

data class GroupAlreadyExistException(
    override val message: String = "Group already exist"
) : Exception(message)

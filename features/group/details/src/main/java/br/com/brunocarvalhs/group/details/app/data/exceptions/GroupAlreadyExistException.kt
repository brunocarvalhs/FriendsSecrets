package br.com.brunocarvalhs.group.list.app.data.exceptions

data class GroupAlreadyExistException(
    override val message: String = "Group already exist"
): Exception(message)

package br.com.brunocarvalhs.friendssecrets.data.exceptions

data class GroupNotReadException(
    override val message: String = "Group not read",
): Exception(message)

package br.com.brunocarvalhs.friendssecrets.data.exceptions

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R

data class GroupAlreadyExistException(
    override val message: String = CustomApplication.instance.getString(R.string.exception_group_already_exist),
) : Exception()

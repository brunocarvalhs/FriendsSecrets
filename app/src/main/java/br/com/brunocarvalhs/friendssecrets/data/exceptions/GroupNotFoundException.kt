package br.com.brunocarvalhs.friendssecrets.data.exceptions

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R

data class GroupNotFoundException(
    override val message: String = CustomApplication.instance.getString(R.string.exception_group_not_found),
) : Exception()
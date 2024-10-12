package br.com.brunocarvalhs.friendssecrets.data.exceptions

import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R

data class MinimumsMembersOfDrawException(
    override val message: String = CustomApplication.instance.getString(R.string.exception_minimums_members_of_draw)
) : Exception()
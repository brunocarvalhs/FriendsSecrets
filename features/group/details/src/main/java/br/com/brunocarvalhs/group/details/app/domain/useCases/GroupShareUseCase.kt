package br.com.brunocarvalhs.group.details.app.domain.useCases

import android.content.Context
import android.content.Intent
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class GroupShareUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    operator fun invoke(group: GroupModel) {
        val message = context.getString(
            R.string.share_group_message,
            group.name,
            group.description,
            group.token,
            "$DEEP_LINK_SCHEME${group.token}",
            "$PLAY_STORE_URL${context.packageName}"
        )

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = TYPE
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val shareIntent = Intent.createChooser(
            sendIntent,
            context.getString(R.string.share_group_title)
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(shareIntent)
    }

    companion object {
        private const val TYPE = "text/plain"
        private const val DEEP_LINK_SCHEME = "friendssecrets://join?code="
        private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id="
    }
}

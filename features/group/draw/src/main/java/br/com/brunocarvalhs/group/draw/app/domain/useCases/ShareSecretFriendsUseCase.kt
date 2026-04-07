package br.com.brunocarvalhs.group.draw.app.domain.useCases

import android.content.Context
import android.content.Intent
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ShareSecretFriendsUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    operator fun invoke(
        group: GroupModel,
        secret: String
    ) = runCatching {
        val message = getUrl(secret)

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val shareIntent = Intent.createChooser(sendIntent, "Compartilhar Código").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(shareIntent)
    }

    private fun getUrl(secret: String) = URL.replace(
        oldValue = "{secret}",
        newValue = encryptor(secret)
    )

    @OptIn(ExperimentalEncodingApi::class)
    private fun encryptor(secret: String): String {
        return Base64.UrlSafe.encode(secret.toByteArray(Charsets.UTF_8))
    }

    companion object {
        private const val URL = "https://pontocomdesenvolvimento.net/apps/secretsanta/pt/{secret}"
    }
}

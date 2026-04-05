package br.com.brunocarvalhs.group.list.app.domain.useCases

import android.content.Context
import android.content.Intent
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GroupShareUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    operator fun invoke(group: GroupModel) {
        val message = """
            🎁 Você foi convidado para o Amigo Secreto: ${group.name}!
            
            📝 Descrição: ${group.description}
            🔑 Código do grupo: ${group.token}
            
            Baixe o app e entre agora!
        """.trimIndent()

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
}
package br.com.brunocarvalhs.friendssecrets.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.brunocarvalhs.friendssecrets.ui.R
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme

@Composable
fun MemberItem(
    participant: String = "",
    draws: Map<String, String>? = null,
    likes: List<String> = emptyList(),
    token: String = "",
    isAdministrator: Boolean = false,
    onShare: (String, String, String) -> Unit = { _, _, _ -> },
    onEdit: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
) {
    // Determina se a ação de compartilhar está disponível
    val canShare = isAdministrator && draws?.isNotEmpty() == true
    // Determina se o participante tem "likes" para exibir/ocultar
    val hasLikes = likes.any { it.isNotBlank() }


    ContactItem(
        name = participant,
        action = { _, isLiked ->

            // Ícone para expandir/recolher likes, visível apenas se houver likes
            if (hasLikes) {
                Icon(
                    imageVector = if (isLiked) Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown,
                    contentDescription = stringResource(
                        if (isLiked) R.string.collapse_likes_action else R.string.expand_likes_action
                    )
                )
            }

            // Botão de compartilhar, visível se 'canShare' for verdadeiro
            if (canShare) {
                draws[participant]?.let { secretFriendName ->
                    if (secretFriendName.isNotBlank()) {
                        IconButton(onClick = {
                            onShare(
                                participant,
                                secretFriendName,
                                token
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                // Usar stringResource para acessibilidade
                                contentDescription = stringResource(
                                    R.string.share_secret_friend_action,
                                    participant
                                )
                            )
                        }
                    }
                }
            }

            // Botão de editar, visível se 'onEdit' não for nulo
            onEdit?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(
                            R.string.edit_participant_action,
                            participant
                        )
                    )
                }
            }

            // Botão de remover, visível se for administrador e 'onRemove' não for nulo
            if (isAdministrator) {
                onRemove?.let {
                    IconButton(onClick = it) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(
                                R.string.remove_participant_action,
                                participant
                            )
                        )
                    }
                }
            }
        }
    )
}

@Composable
@Preview
private fun MemberItemPreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Produto de Teste",
            likes = listOf("Like 1", "Like 2", "Like 3"),
            draws = mapOf(
                "Participant 1" to "Secret Friend 1",
                "Participant 2" to "Secret Friend 2"
            ),
            isAdministrator = false,
            onEdit = {},
            onRemove = {}
        )
    }
}

@Composable
@Preview
private fun MemberItemEmptyPreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Produto de Teste",
            likes = listOf("Like 1", "Like 2", "Like 3"),
            draws = mapOf(
                "Participant 1" to "Secret Friend 1",
                "Participant 2" to "Secret Friend 2"
            ),
            isAdministrator = false,
            onEdit = {},
            onRemove = {}
        )
    }
}

@Composable
@Preview
private fun MemberItemBlankPreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Produto de Teste",
            likes = listOf("Like 1", "Like 2", "Like 3"),
            draws = mapOf(
                "Participant 1" to "Secret Friend 1",
                "Participant 2" to "Secret Friend 2"
            ),
            isAdministrator = false,
            onEdit = {},
            onRemove = {}
        )
    }
}

@Composable
@Preview
private fun MemberItemAdminPreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Produto de Teste",
            likes = listOf("Like 1", "Like 2", "Like 3"),
            draws = mapOf(
                "Participant 1" to "Secret Friend 1",
                "Participant 2" to "Secret Friend 2"
            ),
            isAdministrator = true,
            onEdit = {},
            onRemove = {}
        )
    }
}

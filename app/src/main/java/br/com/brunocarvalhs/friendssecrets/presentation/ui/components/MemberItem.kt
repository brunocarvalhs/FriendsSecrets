package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

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
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@Composable
fun MemberItem(
    participant: UserEntities,
    group: GroupEntities? = null,
    isAdministrator: Boolean = false,
    onShare: (UserEntities, String, String) -> Unit = { _, _, _ -> },
    onEdit: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
) {
    // Determina se a ação de compartilhar está disponível
    val canShare = isAdministrator && group?.draws?.isNotEmpty() == true
    // Determina se o participante tem "likes" para exibir/ocultar
    val hasLikes = participant.likes.any { it.isNotBlank() }


    ContactItem(
        contact = participant,
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
                group?.draws?.get(participant.name)?.let { secretFriendName ->
                    if (secretFriendName.isNotBlank()) {
                        IconButton(onClick = {
                            onShare(
                                participant,
                                secretFriendName,
                                group.token
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                // Usar stringResource para acessibilidade
                                contentDescription = stringResource(
                                    R.string.share_secret_friend_action,
                                    participant.name
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
                            participant.name
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
                                participant.name
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
            participant = UserModel(
                name = "Produto de Teste",
                id = "1",
                phoneNumber = "123456789",
                photoUrl = "",
                isPhoneNumberVerified = false,
                likes = listOf("Like 1", "Like 2", "Like 3")
            ),
            group = GroupModel(),
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
            participant = UserModel(
                name = "Produto de Teste",
                id = "1",
                phoneNumber = "123456789",
                photoUrl = "",
                isPhoneNumberVerified = false,
                likes = listOf()
            ),
            group = GroupModel(),
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
            participant = UserModel(
                name = "Produto de Teste",
                id = "1",
                phoneNumber = "123456789",
                photoUrl = "",
                isPhoneNumberVerified = false,
                likes = listOf(""),
            ),
            group = GroupModel(),
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
            participant = UserModel(
                name = "Produto de Teste",
                id = "1",
                phoneNumber = "123456789",
                photoUrl = "",
                isPhoneNumberVerified = false,
                likes = listOf(""),
            ),
            group = GroupModel(),
            isAdministrator = true,
            onEdit = {},
            onRemove = {}
        )
    }
}

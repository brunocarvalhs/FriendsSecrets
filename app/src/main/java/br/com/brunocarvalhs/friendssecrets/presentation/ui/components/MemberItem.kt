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
import androidx.compose.ui.tooling.preview.Preview
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
    val canShare = isAdministrator && group?.draws?.isNotEmpty() == true

    ContactItem(
        contact = participant,
        action = { _, isLiked ->

            if (participant.likes.isEmpty().not()) {
                Icon(
                    imageVector = if (isLiked) Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown,
                    contentDescription = "Toggle Likes"
                )
            }

            if (canShare) {
                group?.draws?.get(participant.name)?.let { secret ->
                    IconButton(onClick = { onShare(participant, secret, group.token) }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            }

            onEdit?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit"
                    )
                }
            }

            if (isAdministrator) {
                onRemove?.let {
                    IconButton(onClick = it) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete"
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

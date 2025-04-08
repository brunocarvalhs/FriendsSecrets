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
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@Composable
fun MemberItem(
    participant: String,
    likes: List<String> = emptyList(),
    group: GroupEntities? = null,
    isAdministrator: Boolean = false,
    onShare: (String, String, String) -> Unit = { _, _, _ -> },
    onEdit: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
) {
    val hasLikes = likes.any { it.isNotBlank() }
    val canShare = isAdministrator && group?.draws?.isNotEmpty() == true

    ContactItem(
        contact = UserModel(
            name = participant,
            likes = likes
        ),
        action = { _, isLiked ->

            if (hasLikes) {
                Icon(
                    imageVector = if (isLiked) Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown,
                    contentDescription = "Toggle Likes"
                )
            }

            if (canShare) {
                group?.draws?.get(participant)?.let { secret ->
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
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = false,
            likes = listOf("Like 1", "Like 2", "Like 3", "Like 4", "Like 5", "Like 6"),
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
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = false,
            likes = listOf(),
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
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = false,
            likes = listOf(""),
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
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = true,
            likes = listOf(""),
            onEdit = {},
            onRemove = {}
        )
    }
}

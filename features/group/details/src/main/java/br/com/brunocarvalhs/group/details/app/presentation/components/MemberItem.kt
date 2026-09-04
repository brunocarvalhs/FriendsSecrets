package br.com.brunocarvalhs.group.details.app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.brunocarvalhs.group.details.R

@Composable
internal fun MemberItem(
    participant: String = "",
    likes: List<String> = emptyList(),
    isAdministrator: Boolean = false,
    onEdit: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    onAddLike: (() -> Unit)? = null,
) {
    val hasLikes = likes.any { it.isNotBlank() }

    ContactItem(
        name = participant,
        likes = likes,
        action = { _, isLiked ->

            if (hasLikes) {
                Icon(
                    imageVector = if (isLiked) Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown,
                    contentDescription = stringResource(
                        if (isLiked) R.string.collapse_likes_action else R.string.expand_likes_action
                    )
                )
            }

            onAddLike?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Filled.AddCircleOutline,
                        contentDescription = stringResource(
                            R.string.add_like_action,
                            participant
                        )
                    )
                }
            }

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
    MemberItem(
        participant = "Produto de Teste",
        likes = listOf("Like 1", "Like 2", "Like 3"),
        isAdministrator = false,
        onEdit = {},
        onRemove = {}
    )
}

@Composable
@Preview
private fun MemberItemEmptyPreview() {
    MemberItem(
        participant = "Produto de Teste",
        likes = listOf("Like 1", "Like 2", "Like 3"),
        isAdministrator = false,
        onEdit = {},
        onRemove = {}
    )
}

@Composable
@Preview
private fun MemberItemBlankPreview() {
    MemberItem(
        participant = "Produto de Teste",
        likes = listOf("Like 1", "Like 2", "Like 3"),
        isAdministrator = false,
        onEdit = {},
        onRemove = {}
    )
}

@Composable
@Preview
private fun MemberItemAdminPreview() {
    MemberItem(
        participant = "Produto de Teste",
        likes = listOf("Like 1", "Like 2", "Like 3"),
        isAdministrator = true,
        onEdit = {},
        onRemove = {}
    )
}

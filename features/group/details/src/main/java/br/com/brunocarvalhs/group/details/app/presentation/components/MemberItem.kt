package br.com.brunocarvalhs.group.details.app.presentation.components

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
import br.com.brunocarvalhs.group.details.R

@Composable
fun MemberItem(
    participant: String = "",
    draws: Map<String, String>? = null,
    likes: List<String> = emptyList(),
    isAdministrator: Boolean = false,
    onShare: () -> Unit = { },
    onEdit: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
) {
    val canShare = isAdministrator && draws?.isNotEmpty() == true
    val hasLikes = likes.any { it.isNotBlank() }

    ContactItem(
        name = participant,
        action = { _, isLiked ->

            if (hasLikes) {
                Icon(
                    imageVector = if (isLiked) Icons.Sharp.KeyboardArrowUp else Icons.Sharp.KeyboardArrowDown,
                    contentDescription = stringResource(
                        if (isLiked) R.string.collapse_likes_action else R.string.expand_likes_action
                    )
                )
            }

            if (canShare) {
                draws[participant]?.let { secretFriendName ->
                    if (secretFriendName.isNotBlank()) {
                        IconButton(onClick = onShare) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = stringResource(
                                    R.string.share_secret_friend_action,
                                    participant
                                )
                            )
                        }
                    }
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
        draws = mapOf(
            "Participant 1" to "Secret Friend 1",
            "Participant 2" to "Secret Friend 2"
        ),
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
        draws = mapOf(
            "Participant 1" to "Secret Friend 1",
            "Participant 2" to "Secret Friend 2"
        ),
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
        draws = mapOf(
            "Participant 1" to "Secret Friend 1",
            "Participant 2" to "Secret Friend 2"
        ),
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
        draws = mapOf(
            "Participant 1" to "Secret Friend 1",
            "Participant 2" to "Secret Friend 2"
        ),
        isAdministrator = true,
        onEdit = {},
        onRemove = {}
    )
}

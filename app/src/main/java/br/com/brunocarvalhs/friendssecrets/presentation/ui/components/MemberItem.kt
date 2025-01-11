package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material.icons.sharp.MarkUnreadChatAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@Composable
fun MemberItem(
    participant: String,
    likes: List<String> = emptyList(),
    group: GroupEntities? = null,
    isAdministrator: Boolean = false,
    isVisibleLiked: Boolean = false,
    onShare: (String, String, String) -> Unit = { _, _, _ -> },
    onEdit: () -> Unit = {},
    onRemove: ((String) -> Unit)? = null,
    onAI: ((String, String, String) -> Unit)? = null,
) {
    var isLiked by remember { mutableStateOf(isVisibleLiked) }

    Column(
        modifier = Modifier
            .background(ListItemDefaults.colors().containerColor),
    ) {
        ListItem(
            modifier = Modifier.clickable(onClick = { isLiked = !isLiked }),
            headlineContent = {
                Text(text = participant)
            },
            trailingContent = {
                Row {
                    IconButton(
                        onClick = { isLiked = !isLiked }
                    ) {
                        Icon(
                            imageVector = if (isVisibleLiked || isLiked) Icons.Sharp.KeyboardArrowUp
                            else Icons.Sharp.KeyboardArrowDown,
                            contentDescription = "bottom"
                        )
                    }
                    if (isAdministrator && group?.draws?.isNotEmpty() == true) {
                        IconButton(onClick = {
                            group.draws[participant]?.let { secret ->
                                onShare.invoke(
                                    participant,
                                    secret,
                                    group.token
                                )
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "share"
                            )
                        }
                    }
                    onRemove?.let { onRemove ->
                        IconButton(onClick = { onRemove.invoke(participant) }) {
                            Icon(Icons.Filled.Delete, "Remove")
                        }
                    }
                }
            }
        )
        AnimatedVisibility(isLiked, modifier = Modifier.fillMaxWidth()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.padding(start = 16.dp))
                    if (isAdministrator) {
                        AssistChip(
                            onClick = onEdit,
                            label = { Text("Adicionar") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "add"
                                )
                            }
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                    }
                    group?.let { group ->
                        onAI?.let {
                            AssistChip(
                                onClick = {
                                    onAI.invoke(
                                        "${group.name}|${group.description}",
                                        participant,
                                        likes.joinToString(",")
                                    )
                                },
                                label = { Text("Sugestões de presentes") },
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier.size(AssistChipDefaults.IconSize),
                                        imageVector = Icons.Sharp.MarkUnreadChatAlt,
                                        contentDescription = "Chat AI"
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                        }
                    }
                }



                items(likes) { like ->
                    if (like.isNotBlank()) {
                        AssistChip(
                            onClick = {},
                            label = { Text(like) }
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@Composable
@Preview
private fun MemberItemPreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = false,
            isVisibleLiked = false,
            likes = listOf("Like 1", "Like 2", "Like 3", "Like 4", "Like 5", "Like 6")
        )
    }
}

@Composable
@Preview
private fun MemberItemAdministratorPreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = true,
            isVisibleLiked = false,
            likes = listOf("Like 1", "Like 2", "Like 3", "Like 4", "Like 5", "Like 6")
        )
    }
}

@Composable
@Preview
private fun MemberItemVisiblePreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = false,
            isVisibleLiked = true,
            likes = listOf("Like 1", "Like 2", "Like 3", "Like 4", "Like 5", "Like 6")
        )
    }
}

@Composable
@Preview
private fun MemberItemVisibleBlankPreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = false,
            isVisibleLiked = true,
            likes = listOf("", "", "", "", "", "Like 6")
        )
    }
}

@Composable
@Preview
private fun MemberItemVisibleAdministratorPreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = true,
            isVisibleLiked = true,
            likes = listOf("Like 1", "Like 2", "Like 3", "Like 4", "Like 5", "Like 6")
        )
    }
}

@Composable
@Preview
private fun MemberItemRemovePreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = false,
            isVisibleLiked = true,
            likes = listOf("Like 1", "Like 2", "Like 3", "Like 4", "Like 5", "Like 6"),
            onRemove = { }
        )
    }
}

@Composable
@Preview
private fun MemberItemVisibleRemovePreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = true,
            isVisibleLiked = true,
            likes = listOf("Like 1", "Like 2", "Like 3", "Like 4", "Like 5", "Like 6"),
            onRemove = { }
        )
    }
}

@Composable
@Preview
private fun MemberItemVisibleAIPreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = false,
            isVisibleLiked = true,
            likes = listOf("Like 1", "Like 2", "Like 3", "Like 4", "Like 5", "Like 6"),
            onAI = { _, _, _ -> }
        )
    }
}


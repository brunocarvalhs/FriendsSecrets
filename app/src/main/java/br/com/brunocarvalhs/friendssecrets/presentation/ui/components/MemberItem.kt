package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberItem(
    participant: String,
    likes: List<String> = emptyList(),
    group: GroupEntities? = null,
    isAdministrator: Boolean = false,
    isVisibleLiked: Boolean = false,
    onShare: (String, String, String) -> Unit = { _, _, _ -> },
    onEdit: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
) {
    var isLiked by remember { mutableStateOf(isVisibleLiked) }
    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = {
        when (it) {
            SwipeToDismissBoxValue.StartToEnd -> {
                onEdit?.invoke()
                false
            }

            SwipeToDismissBoxValue.EndToStart -> {
                onRemove?.invoke()
                false
            }

            SwipeToDismissBoxValue.Settled -> false
        }
    }, positionalThreshold = { it * 0.5f })



    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = { DismissBackground(dismissState, onEdit = onEdit, onRemove = onRemove) },
    ) {
        Column(
            modifier = Modifier.background(ListItemDefaults.colors().containerColor),
        ) {
            ListItem(modifier = Modifier.clickable(onClick = { isLiked = !isLiked }),
                headlineContent = {
                    Text(text = participant)
                },
                trailingContent = {
                    Row {
                        if (likes.isNotEmpty() && likes.any { it.isNotBlank() }) {
                            IconButton(onClick = { isLiked = !isLiked }) {
                                Icon(
                                    imageVector = if (isVisibleLiked || isLiked) Icons.Sharp.KeyboardArrowUp
                                    else Icons.Sharp.KeyboardArrowDown, contentDescription = "bottom"
                                )
                            }
                        }
                        if (isAdministrator && group?.draws?.isNotEmpty() == true) {
                            IconButton(onClick = {
                                group.draws[participant]?.let { secret ->
                                    onShare.invoke(
                                        participant, secret, group.token
                                    )
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Share, contentDescription = "share"
                                )
                            }
                        }
                    }
                })
            AnimatedVisibility(isLiked, modifier = Modifier.fillMaxWidth()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(likes) { like ->
                        if (like.isNotBlank()) {
                            AssistChip(onClick = {}, label = { Text(like) })
                            Spacer(modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DismissBackground(
    dismissState: SwipeToDismissBoxState,
    onEdit: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(
                when {
                    dismissState.progress > 0 -> Color(0xFF4CAF50)
                    dismissState.progress < 0 -> Color(0xFFF44336)
                    else -> Color.Transparent
                }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        onEdit?.let {
            AnimatedVisibility(
                modifier = Modifier.padding(16.dp),
                visible = dismissState.progress > 0,
                enter = fadeIn()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        onRemove?.let {
            AnimatedVisibility(
                modifier = Modifier.padding(16.dp),
                visible = dismissState.progress < 0,
                enter = fadeIn()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
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
            likes = listOf("Like 1", "Like 2", "Like 3", "Like 4", "Like 5", "Like 6"),
            onEdit = {},
            onRemove = {}
        )
    }
}

@Composable
@Preview
private fun MemberItemNotEmptyPreview() {
    FriendsSecretsTheme {
        MemberItem(
            participant = "Member 1",
            group = GroupModel(),
            isAdministrator = false,
            isVisibleLiked = false,
            likes = emptyList(),
            onEdit = {},
            onRemove = {}
        )
    }
}

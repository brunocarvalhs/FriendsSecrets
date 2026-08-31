package br.com.brunocarvalhs.group.details.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.details.R

@Composable
internal fun EditLikesDialog(
    initialLikes: List<String>,
    onDismiss: () -> Unit,
    onSave: (List<String>) -> Unit,
) {
    var likes by remember { mutableStateOf(initialLikes) }
    var input by remember { mutableStateOf("") }

    fun addCurrentInput() {
        val newLike = input.trim()
        if (newLike.isNotBlank() && likes.none { it.equals(newLike, ignoreCase = true) }) {
            likes = likes + newLike
        }
        input = ""
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_likes_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.edit_likes_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text(stringResource(R.string.edit_likes_add_hint)) },
                        singleLine = true
                    )
                    TextButton(onClick = { addCurrentInput() }, enabled = input.isNotBlank()) {
                        Text(stringResource(R.string.edit_likes_add_action))
                    }
                }

                if (likes.isEmpty()) {
                    Text(
                        text = stringResource(R.string.edit_likes_empty_state),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                    ) {
                        items(likes) { like ->
                            AssistChip(
                                onClick = {},
                                label = { Text(like) },
                                trailingIcon = {
                                    IconButton(
                                        modifier = Modifier.padding(0.dp),
                                        onClick = { likes = likes - like }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = stringResource(
                                                R.string.edit_likes_remove_action,
                                                like
                                            )
                                        )
                                    }
                                },
                                colors = AssistChipDefaults.assistChipColors(),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                addCurrentInput()
                onSave(likes)
            }) {
                Text(stringResource(R.string.edit_likes_save_action))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.edit_likes_cancel_action))
            }
        }
    )
}

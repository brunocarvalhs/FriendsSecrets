package br.com.brunocarvalhs.group.details.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.details.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditLikesBottomSheet(
    initialLikes: List<String>,
    onDismiss: () -> Unit,
    onSave: (List<String>) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var likes by remember { mutableStateOf(initialLikes) }
    var input by remember { mutableStateOf("") }

    fun addLike(value: String) {
        val newLike = value.trim()
        if (newLike.isNotBlank() && likes.none { it.equals(newLike, ignoreCase = true) }) {
            likes = likes + newLike
        }
        input = ""
    }

    fun dismiss() {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) onDismiss()
        }
    }

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.edit_likes_title),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.edit_likes_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(stringResource(R.string.edit_likes_add_hint)) },
                    singleLine = true
                )
                TextButton(onClick = { addLike(input) }, enabled = input.isNotBlank()) {
                    Text(stringResource(R.string.edit_likes_add_action))
                }
            }

            SuggestedLikesRow(alreadyAdded = likes, onPick = { addLike(it) })

            if (likes.isEmpty()) {
                Text(
                    text = stringResource(R.string.edit_likes_empty_state),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                            colors = AssistChipDefaults.assistChipColors()
                        )
                    }
                }
            }

            Button(
                onClick = {
                    addLike(input)
                    onSave(likes)
                    dismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.edit_likes_save_action))
            }
        }
    }
}

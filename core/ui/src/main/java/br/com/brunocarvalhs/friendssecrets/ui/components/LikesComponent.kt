package br.com.brunocarvalhs.friendssecrets.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.ui.R

@Composable
fun LikesComponent(
    modifier: Modifier = Modifier,
    name: TextFieldValue = TextFieldValue(""),
    likes: List<String> = emptyList(),
    onNameChange: (TextFieldValue) -> Unit = {},
    onAddLike: (String) -> Unit = {},
    onRemoveLike: (String) -> Unit = {},
) {
    Column {
        Row(modifier = modifier) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text(text = stringResource(R.string.add_member_bottom_sheet_input_liker)) },
                singleLine = true,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onAddLike.invoke(name.text) }
                ),
                suffix = {
                    IconButton(
                        modifier = Modifier.size(AssistChipDefaults.IconSize),
                        onClick = { onAddLike.invoke(name.text) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add_like_description)
                        )
                    }
                }
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.padding(start = 16.dp))
            }
            items(likes) { like ->
                AssistChip(
                    onClick = { },
                    label = {
                        Text(text = like)
                    },
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier
                                .size(AssistChipDefaults.IconSize)
                                .padding(end = 4.dp),
                            onClick = { onRemoveLike.invoke(like) }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(
                                    R.string.remove_like_description,
                                    like
                                )
                            )
                        }
                    },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LikesComponentPreview() {
    LikesComponent()
}

@Composable
@Preview(showBackground = true)
private fun LikesComponentListPreview() {
    LikesComponent(
        likes = listOf(
            "Like 1",
            "Like 2",
            "Like 3",
            "Like 4",
            "Like 5",
            "Like 6",
            "Like 7",
            "Like 8",
            "Like 9",
            "Like 10"
        )
    )
}
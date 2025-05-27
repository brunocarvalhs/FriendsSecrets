package br.com.brunocarvalhs.friendssecrets.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.ui.R
import br.com.brunocarvalhs.friendssecrets.ui.fake.toFake
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMemberBottomSheet(
    onDismiss: () -> Unit,
    member: UserEntities,
    onMemberAdded: (UserEntities) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier
            .imePadding(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        EditMemberContent(
            sheetState = sheetState,
            onDismiss = onDismiss,
            member = member,
            onMemberAdded = onMemberAdded
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditMemberContent(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    member: UserEntities,
    onMemberAdded: (UserEntities) -> Unit,
) {
    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }

    var name by remember { mutableStateOf(TextFieldValue(member.name, TextRange(0, 0))) }
    var isErrorName by remember { mutableStateOf(false) }
    var errorMessageName by remember { mutableStateOf("") }

    var likeName by remember { mutableStateOf(TextFieldValue("", TextRange(0, 0))) }
    val likes = remember { mutableStateListOf<String>() }

    val scope = rememberCoroutineScope()

    fun addLike() {
        if (likeName.text.isNotBlank()) {
            likes.add(likeName.text)
            likeName = TextFieldValue("", TextRange(0, 0))
        }
    }

    fun removeLike(like: String) {
        likes.remove(like)
    }

    fun dismiss() {
        scope.launch {
            if (name.text.isNotBlank()) {
                addLike()
                onMemberAdded.invoke(
                    member.toCopy(
                        name = name.text,
                        likes = likes
                    )
                )
            }
            name = TextFieldValue("", TextRange(0, 0))
            likes.clear()
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss.invoke()
            }
        }
    }

    LaunchedEffect(Unit) {
        member.likes.forEach { likes.add(it) }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.add_member_bottom_sheet_title_label))
        }
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { value ->
                    if ("^[a-zA-Z\\s]*$".toRegex().matches(value.text)) {
                        name = value
                        isErrorName = false
                        errorMessageName = ""
                    } else {
                        isErrorName = true
                        errorMessageName =
                            context.getString(R.string.add_member_bottom_sheet_input_new_member_error_message)
                    }
                },
                label = { Text(text = stringResource(R.string.add_member_bottom_sheet_input_new_member)) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                singleLine = true,
                isError = isErrorName,
                supportingText = {
                    if (isErrorName) {
                        Text(text = errorMessageName)
                    }
                },
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusRequester.requestFocus()
                    }
                )
            )
        }
        Column {
            Row(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = likeName,
                    onValueChange = { value -> likeName = value },
                    label = { Text(text = stringResource(R.string.add_member_bottom_sheet_input_liker)) },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { addLike() }
                    ),
                    suffix = {
                        IconButton(
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                            onClick = { addLike() }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "add"
                            )
                        }
                    }
                )
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.padding(start = 16.dp))
                }
                items(likes.filter { it.isNotBlank() }) { like ->
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
                                onClick = { removeLike(like) }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "remove"
                                )
                            }
                        },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {
            TextButton(
                onClick = { dismiss() }, modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_member_bottom_sheet_text_button_label))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun AddMemberBottomSheetPreview() {
    FriendsSecretsTheme {
        EditMemberContent(
            sheetState = rememberModalBottomSheetState(),
            member = UserEntities.toFake(
                name = "Bruno",
                likes = listOf("Like 1", "Like 2"),
            ),
            onDismiss = {},
            onMemberAdded = { }
        )
    }
}
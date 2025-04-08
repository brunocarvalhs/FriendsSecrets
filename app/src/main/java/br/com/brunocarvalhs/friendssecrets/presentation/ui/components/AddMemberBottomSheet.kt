package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberBottomSheet(
    onDismiss: () -> Unit,
    onMemberAdded: (String, List<String>) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier
            .imePadding(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        AddMemberContent(
            sheetState = sheetState,
            onDismiss = onDismiss,
            onMemberAdded = onMemberAdded
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMemberContent(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onMemberAdded: (String, List<String>) -> Unit,
) {
    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }

    var name by remember { mutableStateOf(TextFieldValue("", TextRange(0, 0))) }
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

    fun addMember() {
        scope.launch {
            onMemberAdded.invoke(name.text, likes)
            name = TextFieldValue("", TextRange(0, 0))
            addLike()
            likes.clear()
            focusRequester.requestFocus()
        }
    }

    fun dismiss() {
        scope.launch {
            sheetState.hide()
            if (name.text.isNotBlank()) onMemberAdded.invoke(name.text, likes)
            name = TextFieldValue("", TextRange(0, 0))
            addLike()
            likes.clear()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss.invoke()
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
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
        LikesComponent(
            modifier = Modifier.padding(16.dp),
            name = likeName,
            onNameChange = { value -> likeName = value },
            list = likes,
            onAddLike = { _ ->
                addLike()
            },
            onRemoveLike = { like ->
                likes.remove(like)
            }
        )
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {
            Button(
                onClick = { addMember() }, modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_member_bottom_sheet_button_label))
            }
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
        AddMemberContent(
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {},
            onMemberAdded = { _, _ ->

            }
        )
    }
}
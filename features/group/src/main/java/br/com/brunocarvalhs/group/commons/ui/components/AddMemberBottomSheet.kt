package br.com.brunocarvalhs.group.commons.ui.components

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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.ui.R
import br.com.brunocarvalhs.friendssecrets.ui.components.LikesComponent
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberBottomSheet(
    onDismiss: () -> Unit,
    onMemberAdded: (member: UserEntities) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier.imePadding(),
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
    onMemberAdded: (name: String, likes: List<String>) -> Unit,
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var nameValidationError by remember { mutableStateOf<String?>(null) }

    var likeName by remember { mutableStateOf("") }
    val likes = remember { mutableStateListOf<String>() }

    // Validation
    val validateName: (String) -> String? = remember(context) {
        { input ->
            if ("^[a-zA-Z\\s]*$".toRegex().matches(input)) null
            else context.getString(R.string.add_member_bottom_sheet_input_new_member_error_message)
        }
    }

    val addLike = remember(likeName, likes) {
        {
            if (likeName.isNotBlank()) {
                likes.add(likeName)
                likeName = ""
            }
        }
    }

    val addMember = remember(name, likes) {
        {
            scope.launch {
                onMemberAdded(name.trim(), likes.toList())
                name = ""
                likeName = ""
                likes.clear()
                focusRequester.requestFocus()
            }
        }
    }

    val dismiss = remember(name, likes) {
        {
            scope.launch {
                if (name.isNotBlank()) {
                    likes.add(likeName)
                    onMemberAdded(name.trim(), likes.toList())
                }
                name = ""
                likeName = ""
                likes.clear()
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) onDismiss()
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.add_member_bottom_sheet_title_label))
        }
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { value ->
                    name = value
                    nameValidationError = validateName(value)
                },
                label = { Text(stringResource(R.string.add_member_bottom_sheet_input_new_member)) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                singleLine = true,
                isError = nameValidationError != null,
                supportingText = {
                    nameValidationError?.let { Text(text = it) }
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
            name = TextFieldValue(likeName), // Convert to TextFieldValue for compatibility
            onNameChange = { value -> likeName = value.text },
            likes = likes,
            onAddLike = { addLike() },
            onRemoveLike = { like -> likes.remove(like) }
        )

        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {
            Button(
                onClick = { addMember.invoke() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_member_bottom_sheet_button_label))
            }
            TextButton(
                onClick = { dismiss.invoke() },
                modifier = Modifier.fillMaxWidth()
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
            onMemberAdded = { _, _ -> }
        )
    }
}
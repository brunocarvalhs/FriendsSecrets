package br.com.brunocarvalhs.group.app.list.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupToEnterBottomSheet(
    onDismiss: () -> Unit,
    onToEnter: (String) -> Unit,
) {
    var token by remember { mutableStateOf(TextFieldValue("", TextRange(0, 0))) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = Modifier.imePadding(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.group_to_enter_button_enter_to_group))
        }
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = token,
                onValueChange = { value -> token = value },
                label = { Text(text = stringResource(R.string.group_to_enter_textfield_token_to_grup)) },
                modifier = Modifier.weight(1f)
            )
        }
        Button(
            onClick = {
                scope.launch {
                    sheetState.hide()
                    onToEnter.invoke(token.text)
                    token = TextFieldValue("", TextRange(0, 0))
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismiss.invoke()
                    }
                }
            }, modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.group_to_enter_button_text))
        }
        Spacer(
            Modifier.windowInsetsBottomHeight(
                WindowInsets.systemBars
            )
        )
    }
}
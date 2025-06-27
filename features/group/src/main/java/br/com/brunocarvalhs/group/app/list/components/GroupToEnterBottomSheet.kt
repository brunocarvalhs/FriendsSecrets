package br.com.brunocarvalhs.group.app.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.brunocarvalhs.group.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupToEnterBottomSheet(
    onDismiss: () -> Unit,
    onToEnter: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val focusManager = LocalFocusManager.current

    val tokenValues = remember { List(8) { mutableStateOf("") } }
    val focusRequesters = remember { List(8) { FocusRequester() } }

    LaunchedEffect(sheetState.isVisible) {
        if (sheetState.isVisible) {
            focusRequesters[0].requestFocus()
        }
    }

    ModalBottomSheet(
        modifier = Modifier
            .imePadding()
            .fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.group_to_enter_title),
                style = MaterialTheme.typography.headlineSmall,
            )

            repeat(2) { rowIndex ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(4) { colIndex ->
                        val index = rowIndex * 4 + colIndex
                        val value = tokenValues[index].value
                        OutlinedTextField(
                            value = value,
                            onValueChange = { newChar ->
                                if (newChar.length <= 1 && newChar.matches(Regex("[a-zA-Z0-9]?"))) {
                                    tokenValues[index].value = newChar
                                }
                            },
                            singleLine = true,
                            enabled = if (index == 0) true else tokenValues[index - 1].value.isNotEmpty(),
                            modifier = Modifier
                                .weight(1f)
                                .widthIn(min = 56.dp)
                                .focusRequester(focusRequesters[index]),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                letterSpacing = 4.sp
                            ),
                            maxLines = 1,
                        )

                        LaunchedEffect(value) {
                            if (value.isNotEmpty() && index < 7) {
                                focusRequesters[index + 1].requestFocus()
                            } else if (value.isEmpty() && index > 0) {
                                focusRequesters[index - 1].requestFocus()
                            }
                            val token = tokenValues.joinToString("") { it.value }
                            if (token.length == 8 && token.all { it.isLetterOrDigit() }) {
                                focusManager.clearFocus(force = true)
                            }
                        }
                    }
                }
            }

            val token = tokenValues.joinToString("") { it.value }

            Button(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        onToEnter(token)
                        tokenValues.forEach { it.value = "" }
                        focusRequesters[0].requestFocus()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismiss()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = token.length == 8
            ) {
                Text(stringResource(R.string.group_to_enter_button_text))
            }
        }
    }
}

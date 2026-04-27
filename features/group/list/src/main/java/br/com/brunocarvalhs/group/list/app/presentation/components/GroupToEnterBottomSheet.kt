package br.com.brunocarvalhs.group.list.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.brunocarvalhs.group.list.R
import kotlinx.coroutines.launch

private const val TOKEN_MAX_LENGTH = 8
private const val SHEET_HEIGHT_FRACTION = 0.85f
private const val LETTER_SPACING = 8
private const val FONT_SIZE = 24
private const val TOKEN_DIVIDER_INDEX = 3
private const val TOKEN_DIVIDER_OFFSET = 4

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

    var token by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(sheetState.isVisible) {
        if (sheetState.isVisible) {
            focusRequester.requestFocus()
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
        GroupToEnterContent(
            token = token,
            onTokenChange = { newValue ->
                if (newValue.length <= TOKEN_MAX_LENGTH && newValue.all { it.isLetterOrDigit() }) {
                    token = newValue
                    if (token.length == TOKEN_MAX_LENGTH) {
                        focusManager.clearFocus()
                    }
                }
            },
            focusRequester = focusRequester,
            onActionClick = {
                scope.launch {
                    sheetState.hide()
                    onToEnter(token)
                    token = ""
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismiss()
                    }
                }
            }
        )
    }
}

@Composable
private fun GroupToEnterContent(
    token: String,
    onTokenChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onActionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(SHEET_HEIGHT_FRACTION)
            .padding(horizontal = 32.dp, vertical = 24.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = stringResource(R.string.group_to_enter_title),
            style = MaterialTheme.typography.headlineSmall,
        )

        OutlinedTextField(
            value = token,
            onValueChange = onTokenChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontSize = FONT_SIZE.sp,
                letterSpacing = LETTER_SPACING.sp
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None
            ),
            visualTransformation = TokenVisualTransformation()
        )

        Button(
            onClick = onActionClick,
            modifier = Modifier
                .fillMaxWidth(),
            enabled = token.length == TOKEN_MAX_LENGTH
        ) {
            Text(stringResource(R.string.group_to_enter_button_text))
        }
    }
}

private class TokenVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = StringBuilder()
        for (i in text.indices) {
            out.append(text[i])
            if (i == TOKEN_DIVIDER_INDEX) out.append(" ")
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return if (offset <= TOKEN_DIVIDER_INDEX) offset else offset + 1
            }

            override fun transformedToOriginal(offset: Int): Int {
                return if (offset <= TOKEN_DIVIDER_OFFSET) offset else offset - 1
            }
        }

        return TransformedText(AnnotatedString(out.toString()), offsetMapping)
    }
}

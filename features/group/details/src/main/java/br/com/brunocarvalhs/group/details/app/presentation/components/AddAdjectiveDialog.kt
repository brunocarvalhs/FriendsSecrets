package br.com.brunocarvalhs.group.details.app.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.brunocarvalhs.group.details.R

@Composable
internal fun AddAdjectiveDialog(
    participant: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var input by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_adjective_title, participant)) },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier,
                placeholder = { Text(stringResource(R.string.add_adjective_hint)) },
                singleLine = true,
                supportingText = {
                    Text(
                        text = stringResource(R.string.add_adjective_description),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(input) },
                enabled = input.isNotBlank()
            ) {
                Text(stringResource(R.string.add_adjective_save_action))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.add_adjective_cancel_action))
            }
        }
    )
}

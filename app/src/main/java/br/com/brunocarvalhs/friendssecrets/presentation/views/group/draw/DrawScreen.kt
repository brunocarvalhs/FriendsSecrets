package br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@Composable
fun DrawScreen(
    groupId: String,
    navController: NavController,
    viewModel: DrawViewModel = viewModel(
        factory = DrawViewModel.Factory
    ),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.eventIntent(DrawIntent.FetchDraw(group = groupId))
    }

    DrawContent(
        uiState = uiState.value,
        eventIntent = viewModel::eventIntent
    )
}

@Composable
private fun DrawContent(
    uiState: DrawUiState,
    eventIntent: (DrawIntent) -> Unit,
) {
    var secret by remember { mutableStateOf(TextFieldValue("", TextRange(0, 0))) }

    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            when (uiState) {
                is DrawUiState.Error -> {
                    ErrorComponent(message = uiState.error, onRefresh = {
                        eventIntent(DrawIntent.Refresh)
                    })
                }
                DrawUiState.Idle -> {
                    Row(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = secret,
                            onValueChange = { value -> secret = value },
                            label = { Text(text = "Código secreto") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(modifier = Modifier.padding(16.dp)) {
                        Button(onClick = {
                            eventIntent(DrawIntent.FetchDraw(secret.text))
                        }) {
                            Text(text = "Revelar meu amigo secreto")
                        }
                    }
                }
                DrawUiState.Loading -> {
                    LoadingProgress()
                }
                is DrawUiState.Success -> {
                    Text(text = uiState.draw)
                }
            }
        }
    }
}

private class DrawPreviewProvider : PreviewParameterProvider<DrawUiState> {
    override val values = sequenceOf(
        DrawUiState.Idle,
        DrawUiState.Loading,
        DrawUiState.Success(draw = ""),
        DrawUiState.Error(error = "Error")
    )
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun DrawContentPreview(
    @PreviewParameter(DrawPreviewProvider::class) state: DrawUiState,
) {
    FriendsSecretsTheme {
        DrawContent(
            uiState = state,
            eventIntent = {}
        )
    }
}
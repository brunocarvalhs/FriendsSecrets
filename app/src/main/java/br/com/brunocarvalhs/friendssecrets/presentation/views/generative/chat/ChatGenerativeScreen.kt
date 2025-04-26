package br.com.brunocarvalhs.friendssecrets.presentation.views.generative.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.extensions.textWithFormatting
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton

@Composable
fun ChatGenerativeScreen(
    data: String? = null,
    navController: NavHostController,
    viewModel: ChatGenerativeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        data?.let {
            viewModel.eventIntent(ChatGenerativeIntent.SendMessage(data))
        }
    }

    ChatGenerativeContent(
        uiState = uiState,
        onSendMessage = {
            viewModel.eventIntent(ChatGenerativeIntent.SendMessage(it))
        },
        onBack = {
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatGenerativeContent(
    uiState: ChatGenerativeUiState,
    onSendMessage: (String) -> Unit = {},
    onBack: () -> Unit = {},
) {
    var currentMessage by remember { mutableStateOf("") }

    fun sendMessage() {
        if (currentMessage.isNotEmpty()) {
            onSendMessage.invoke(currentMessage)
            currentMessage = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.chat_generative_title_chat)) },
                navigationIcon = { NavigationBackIconButton(onClick = onBack) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top
            ) {
                if (uiState is ChatGenerativeUiState.Chat) {
                    items(uiState.messages) { (type, message) ->
                        MessageBubble(message.textWithFormatting().orEmpty(), type)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = currentMessage,
                    onValueChange = { currentMessage = it },
                    label = { Text(text = stringResource(R.string.chat_generative_textfield_message)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            sendMessage()
                        }
                    )
                )

                Button(
                    onClick = { sendMessage() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(text = stringResource(R.string.chat_generative_button_send))
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: String, type: ChatGenerativeType) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        val alignment = if (type == ChatGenerativeType.HUMAN) Alignment.CenterStart else Alignment.CenterEnd
        val backgroundColor = if (type == ChatGenerativeType.HUMAN) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        val textColor = if (type == ChatGenerativeType.HUMAN) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary

        Surface(
            modifier = Modifier.align(alignment),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            color = backgroundColor
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp),
                color = textColor
            )
        }
    }
}

@Preview
@Composable
private fun ChatGenerativeContentPreview() {
    ChatGenerativeContent(
        uiState = ChatGenerativeUiState.Chat(
            listOf(
                ChatGenerativeType.HUMAN to "Hello!",
                ChatGenerativeType.AI to "Hi there!"
            )
        )
    )
}

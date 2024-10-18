package br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.commons.toggle.ToggleKeys
import br.com.brunocarvalhs.friendssecrets.commons.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@Composable
fun DrawScreen(
    groupId: String,
    navController: NavController,
    viewModel: DrawViewModel = viewModel(
        factory = DrawViewModel.Factory
    ),
    toggleManager: ToggleManager,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.eventIntent(DrawIntent.FetchDraw(group = groupId))
    }

    DrawContent(
        groupId = groupId,
        navController = navController,
        uiState = uiState.value,
        eventIntent = viewModel::eventIntent,
        isGenerativeEnabled = toggleManager
            .isFeatureEnabled(ToggleKeys.DRAW_IS_GENERATIVE_ENABLED),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawContent(
    groupId: String = "",
    navController: NavController,
    uiState: DrawUiState,
    eventIntent: (DrawIntent) -> Unit,
    isGenerativeEnabled: Boolean = true,
) {
    val context = LocalContext.current

    var secret by remember { mutableStateOf(TextFieldValue("", TextRange(0, 0))) }

    Scaffold(topBar = {
        LargeTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            Text("Seu amigo secreto é ...")
        }, navigationIcon = {
            NavigationBackIconButton(navController = navController)
        })
    }, floatingActionButton = {
        if (uiState is DrawUiState.Idle) {
            ExtendedFloatingActionButton(onClick = {
                eventIntent(
                    DrawIntent.FetchDraw(
                        group = groupId, code = secret.text
                    )
                )
            }) {
                Text(text = "Revelar meu amigo secreto")
            }
        }
        if (uiState is DrawUiState.Success) {
            if (isGenerativeEnabled) {
                ExtendedFloatingActionButton(onClick = {
                    eventIntent(
                        DrawIntent.GenerativeDraw(
                            context = context,
                            navigation = navController,
                            group = uiState.group,
                            secret = uiState.draw.keys.first(),
                            likes = uiState.draw.values.first().split("|").toList()
                        )
                    )
                }) {
                    Text(text = "Dicas de presentes")
                }
            }
        }
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
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
                }

                DrawUiState.Loading -> {
                    LoadingProgress()
                }

                is DrawUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Row {
                            Text(text = uiState.draw.keys.first())
                        }
                        Column {
                            Text(text = "Gostos")
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                            ) {
                                items(uiState.draw.values.first().split("|").toList()) { like ->
                                    AssistChip(
                                        onClick = { },
                                        label = { Text(like) },
                                    )
                                    Spacer(modifier = Modifier.padding(4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private class DrawPreviewProvider : PreviewParameterProvider<DrawUiState> {
    override val values = sequenceOf(
        DrawUiState.Idle,
        DrawUiState.Loading,
        DrawUiState.Success(
            draw = mapOf("Secret" to "Like 1 | Like 2 | Like 3"),
            group = GroupModel(name = "Group")
        ),
        DrawUiState.Error(error = "Error")
    )
}

@Preview(
    name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun DrawContentPreview(
    @PreviewParameter(DrawPreviewProvider::class) state: DrawUiState,
) {
    FriendsSecretsTheme {
        DrawContent(groupId = "",
            navController = rememberNavController(),
            uiState = state,
            eventIntent = {})
    }
}
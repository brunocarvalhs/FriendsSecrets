package br.com.brunocarvalhs.group.app.draw

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.ui.fake.toFake
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.group.R

@Composable
fun DrawScreen(
    groupId: String,
    code: String? = null,
    navController: NavController,
    viewModel: DrawViewModel,
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

    var secret by remember { mutableStateOf(TextFieldValue()) }

    Scaffold(topBar = {
        LargeTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ), title = {
            Text(text = stringResource(R.string.draw_screen_title))
        }, navigationIcon = {
            NavigationBackIconButton(navController = navController)
        })
    }, floatingActionButton = {
        if (uiState is DrawUiState.Idle) {
            ExtendedFloatingActionButton(onClick = {
                eventIntent(
                    DrawIntent.FetchDraw(
                        group = groupId,
                        code = secret.text
                    )
                )
            }) {
                Text(text = stringResource(R.string.draw_screen_action_title))
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
                    Text(text = stringResource(R.string.draw_screen_action_generative))
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
                            label = { Text(text = stringResource(R.string.draw_screen_code_secret)) },
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
                            Text(text = stringResource(R.string.draw_screen_title_like))
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
            group = GroupEntities.toFake(name = "Group")
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
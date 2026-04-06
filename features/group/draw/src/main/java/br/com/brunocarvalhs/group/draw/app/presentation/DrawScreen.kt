package br.com.brunocarvalhs.group.draw.app.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawScreen(
    viewModel: DrawViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) {
//        viewModel.eventIntent(DrawIntent.FetchDraw(group = groupId))
//    }
//
//    DrawNewLayout(
//        groupId = groupId,
//        navController = navController,
//        uiState = uiState,
//        eventIntent = viewModel::eventIntent,
//    )
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DrawNewLayout(
//    groupId: String,
//    navController: NavController,
//    uiState: DrawUiState,
//    eventIntent: (DrawIntent) -> Unit,
//) {
//    var inputCode by remember { mutableStateOf(TextFieldValue("")) }
//    var isFlipped by remember { mutableStateOf(false) }
//
//    val decodedSecret = remember(uiState) {
//        uiState.takeIf { it is DrawUiState.Success }?.let {
//            val drawSecret = (it as DrawUiState.Success).draw.keys.first()
//            return@let drawSecret
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            LargeTopAppBar(
//                title = { Text(stringResource(R.string.draw_screen_title)) },
//                navigationIcon = { NavigationBackIconButton(navController) }
//            )
//        },
//        content = { padding ->
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding),
//                contentAlignment = Alignment.Center
//            ) {
//                when (uiState) {
//                    is DrawUiState.Idle -> {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center,
//                            modifier = Modifier
//                                .padding(32.dp)
//                                .fillMaxWidth()
//                        ) {
//                            OutlinedTextField(
//                                value = inputCode,
//                                onValueChange = { inputCode = it },
//                                label = { Text(stringResource(R.string.draw_screen_code_secret)) },
//                                modifier = Modifier.fillMaxWidth()
//                            )
//                            Spacer(modifier = Modifier.height(24.dp))
//                            Button(
//                                onClick = {
//                                    eventIntent(
//                                        DrawIntent.FetchDraw(
//                                            group = groupId,
//                                            code = inputCode.text
//                                        )
//                                    )
//                                    isFlipped = false
//                                },
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Text(stringResource(R.string.draw_screen_action_title))
//                            }
//                        }
//                    }
//
//                    is DrawUiState.Loading -> {
//                        LoadingProgress()
//                    }
//
//                    is DrawUiState.Error -> {
//                        ErrorComponent(
//                            message = uiState.error,
//                            onRefresh = { eventIntent(DrawIntent.Refresh) }
//                        )
//                    }
//
//                    is DrawUiState.Success -> {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center,
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(32.dp)
//                        ) {
//                            FlipCard(
//                                frontContent = {
//                                    Box(
//                                        modifier = Modifier
//                                            .size(250.dp)
//                                            .clip(RoundedCornerShape(16.dp))
//                                            .background(MaterialTheme.colorScheme.primaryContainer),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text = stringResource(R.string.draw_card_front_hint),
//                                            style = MaterialTheme.typography.titleMedium,
//                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
//                                            textAlign = TextAlign.Center,
//                                            modifier = Modifier.padding(16.dp)
//                                        )
//                                    }
//                                },
//                                backContent = {
//                                    Box(
//                                        modifier = Modifier
//                                            .size(250.dp)
//                                            .clip(RoundedCornerShape(16.dp))
//                                            .background(MaterialTheme.colorScheme.primary),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text = decodedSecret
//                                                ?: stringResource(R.string.draw_error_decode),
//                                            style = MaterialTheme.typography.headlineMedium.copy(
//                                                fontWeight = FontWeight.ExtraBold
//                                            ),
//                                            color = MaterialTheme.colorScheme.onPrimary,
//                                            textAlign = TextAlign.Center,
//                                            modifier = Modifier.padding(24.dp)
//                                        )
//                                    }
//                                },
//                                isFlipped = isFlipped,
//                                onClick = { isFlipped = !isFlipped }
//                            )
//                            Spacer(modifier = Modifier.height(32.dp))
//
//                            Text(
//                                text = stringResource(R.string.draw_screen_title_like),
//                                style = MaterialTheme.typography.titleMedium
//                            )
//                            Spacer(modifier = Modifier.height(12.dp))
//
//                            val likes = uiState.draw.values.first().split("|")
//                                .map { it.trim() }
//                                .filter { it.isNotBlank() }
//
//                            if (likes.isNotEmpty()) {
//                                Text(
//                                    text = stringResource(R.string.draw_screen_title_like),
//                                    style = MaterialTheme.typography.titleMedium
//                                )
//                                Spacer(modifier = Modifier.height(12.dp))
//
//                                LazyRow(
//                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
//                                    modifier = Modifier.fillMaxWidth()
//                                ) {
//                                    items(likes) { like ->
//                                        AssistChip(
//                                            onClick = { /* opcional */ },
//                                            label = { Text(like) },
//                                            modifier = Modifier.height(36.dp)
//                                        )
//                                    }
//                                }
//
//                                Spacer(modifier = Modifier.height(48.dp))
//                            } else {
//                                Spacer(modifier = Modifier.height(48.dp)) // mantém espaço antes do botão
//                            }
//
//
//                            Spacer(modifier = Modifier.height(48.dp))
//
//                            Button(
//                                onClick = {
//                                    eventIntent(DrawIntent.Refresh)
//                                    isFlipped = false
//                                },
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Text(stringResource(R.string.draw_screen_action_reset))
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    )
//}
//
//@Composable
//fun FlipCard(
//    frontContent: @Composable () -> Unit,
//    backContent: @Composable () -> Unit,
//    isFlipped: Boolean,
//    onClick: () -> Unit,
//) {
//    val rotation by animateFloatAsState(
//        targetValue = if (isFlipped) 180f else 0f,
//        animationSpec = tween(durationMillis = 600)
//    )
//
//    Box(
//        modifier = Modifier
//            .clickable { onClick() }
//            .graphicsLayer {
//                cameraDistance = 12 * density
//                rotationY = rotation
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        if (rotation <= 90f) {
//            frontContent()
//        } else {
//            Box(
//                modifier = Modifier.graphicsLayer {
//                    rotationY = 180f
//                    rotationZ = 0f
//                }
//            ) {
//                backContent()
//            }
//        }
//    }
//}
//
//
//private class DrawPreviewProvider : PreviewParameterProvider<DrawUiState> {
//    override val values = sequenceOf(
//        DrawUiState.Idle,
//        DrawUiState.Loading,
//        DrawUiState.Success(
//            draw = mapOf(
//                Base64.encodeToString(
//                    "Maria".toByteArray(),
//                    Base64.NO_WRAP
//                ) to "Chocolate | Viagem | Café"
//            ),
//            group = GroupEntities.toFake("Grupo Amigo Secreto")
//        ),
//        DrawUiState.Error("Não foi possível carregar o amigo secreto")
//    )
//}
//
//@Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
//@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
//@Composable
//fun DrawNewLayoutPreview(@PreviewParameter(DrawPreviewProvider::class) state: DrawUiState) {
//    FriendsSecretsTheme {
//        DrawNewLayout(
//            groupId = "fake_id",
//            navController = rememberNavController(),
//            uiState = state,
//            eventIntent = {}
//        )
//    }
//}

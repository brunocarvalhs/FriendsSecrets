package br.com.brunocarvalhs.auth.app.create_profile

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.auth.commons.constants.SUGGESTION_LIKES
import br.com.brunocarvalhs.auth.commons.performance.LaunchPerformanceLifecycleTracing
import br.com.brunocarvalhs.friendssecrets.common.navigation.AuthGraphRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.GroupGraphRoute
import br.com.brunocarvalhs.friendssecrets.ui.components.LoadingComponent
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import coil.compose.AsyncImage
import com.yalantis.ucrop.UCrop
import java.io.File

@Composable
fun CreateProfileScreen(
    navController: NavController,
    viewModel: CreateProfileViewModel
) {
    LaunchPerformanceLifecycleTracing("profile")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(CreateProfileIntent.FetchData)
    }

    LaunchedEffect(uiState) {
        if (uiState is CreateProfileUiState.Success) {
            navController.navigate(GroupGraphRoute) {
                popUpTo(AuthGraphRoute) {
                    inclusive = true
                }
            }
        }
    }

    ProfileContent(
        uiState = uiState,
        navController = navController,
        handleIntent = viewModel::handleIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    uiState: CreateProfileUiState = CreateProfileUiState.Idle(),
    navController: NavController = rememberNavController(),
    handleIntent: (CreateProfileIntent) -> Unit = {},
    step: ProfileStep = ProfileStep.Likes
) {
    var currentStep by remember { mutableStateOf(step) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    NavigationBackIconButton {
                        if (currentStep == ProfileStep.PersonalInfo) {
                            currentStep = ProfileStep.Likes
                        } else {
                            navController.popBackStack()
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentStep == ProfileStep.Likes) {
                FloatingActionButton(onClick = { currentStep = ProfileStep.PersonalInfo }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Avançar"
                    )
                }
            }
        }
    ) { paddingValue ->
        when (uiState) {
            is CreateProfileUiState.Idle -> {
                ProfileForm(
                    modifier = Modifier.padding(paddingValue),
                    username = uiState.name.orEmpty(),
                    userPhoneNumber = uiState.phoneNumber.orEmpty(),
                    userPhotoUrl = uiState.photoUrl?.toUri()?.toString(),
                    userLikes = uiState.likes,
                    currentStep = currentStep,
                    handleIntent = handleIntent
                )
            }

            else -> {}
        }
    }

    if (uiState is CreateProfileUiState.Loading) LoadingComponent()
}

enum class ProfileStep {
    Likes, PersonalInfo
}

@Composable
private fun ProfileForm(
    modifier: Modifier = Modifier,
    username: String = "",
    userPhoneNumber: String = "",
    userPhotoUrl: String? = null,
    userLikes: List<String> = emptyList(),
    currentStep: ProfileStep = ProfileStep.Likes,
    handleIntent: (CreateProfileIntent) -> Unit = {},
) {
    val context = LocalContext.current
    val cacheDir = context.cacheDir

    var name by remember { mutableStateOf(username) }
    val phoneNumber by remember { mutableStateOf(userPhoneNumber) }
    var profileImageUri by remember { mutableStateOf(userPhotoUrl?.let { Uri.parse(it) }) }

    val suggestedLikes = SUGGESTION_LIKES
    val selectedLikes = remember { mutableStateListOf<String>().apply { addAll(userLikes) } }

    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            UCrop.getOutput(result.data ?: return@rememberLauncherForActivityResult)?.let {
                profileImageUri = it
            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val destinationUri =
                    Uri.fromFile(File(cacheDir, "cropped_${System.currentTimeMillis()}.jpg"))
                val uCrop =
                    UCrop.of(it, destinationUri).withAspectRatio(1f, 1f).withMaxResultSize(500, 500)
                cropLauncher.launch(uCrop.getIntent(context))
            }
        }
    )

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            when (currentStep) {
                ProfileStep.Likes -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        suggestedLikes.forEach { (category, likes) ->
                            item {
                                Column {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        likes.forEach { like ->
                                            val isSelected = selectedLikes.contains(like)
                                            val backgroundColor by animateColorAsState(
                                                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                            )
                                            val contentColor by animateColorAsState(
                                                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Surface(
                                                tonalElevation = if (isSelected) 4.dp else 0.dp,
                                                shape = RoundedCornerShape(16.dp),
                                                color = backgroundColor,
                                                modifier = Modifier
                                                    .clickable {
                                                        if (isSelected) selectedLikes.remove(like) else selectedLikes.add(
                                                            like
                                                        )
                                                    }
                                            ) {
                                                Text(
                                                    text = like,
                                                    color = contentColor,
                                                    modifier = Modifier.padding(
                                                        horizontal = 12.dp,
                                                        vertical = 6.dp
                                                    ),
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                ProfileStep.PersonalInfo -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfilePicture(photoUrl = profileImageUri?.toString()) {
                            galleryLauncher.launch("image/*")
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Seu nome") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = {},
                            label = { Text("Número de telefone") },
                            singleLine = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if (currentStep == ProfileStep.PersonalInfo) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        handleIntent(
                            CreateProfileIntent.SaveCreateProfile(
                                name = name,
                                photoUrl = profileImageUri.toString(),
                                likes = selectedLikes
                            )
                        )
                    },
                    enabled = name.isNotBlank()
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}

@Composable
fun SuggestionChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0xFF009688) else Color.LightGray)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = if (selected) Color.White else Color.Black)
    }
}


@Composable
fun ProfilePicture(photoUrl: String?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (photoUrl != null) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Foto do perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar padrão",
                modifier = Modifier.size(100.dp),
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
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
private fun ProfileScreenLikesPreview() {
    FriendsSecretsTheme {
        ProfileContent(
            uiState = CreateProfileUiState.Idle(),
            handleIntent = {},
            step = ProfileStep.Likes
        )
    }
}

@Composable
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
private fun ProfileScreenDataPreview() {
    FriendsSecretsTheme {
        ProfileContent(
            uiState = CreateProfileUiState.Idle(),
            handleIntent = {},
            step = ProfileStep.PersonalInfo
        )
    }
}
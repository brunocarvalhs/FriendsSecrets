package br.com.brunocarvalhs.auth.app.profile

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.com.brunocarvalhs.auth.commons.navigation.PhoneSendScreenRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.AuthGraphRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.HomeGraphRoute
import br.com.brunocarvalhs.friendssecrets.ui.components.LikesComponent
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import coil.compose.AsyncImage
import com.yalantis.ucrop.UCrop
import java.io.File

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProfileIntent.FetchData)
    }

    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Success) {
            navController.navigate(HomeGraphRoute) {
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
    uiState: ProfileUiState = ProfileUiState.Idle(),
    navController: NavController = rememberNavController(),
    handleIntent: (ProfileIntent) -> Unit = {},
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    val currentRoute =
                        navController.currentBackStackEntry?.toRoute<PhoneSendScreenRoute>()
                    if (currentRoute != PhoneSendScreenRoute) {
                        NavigationBackIconButton(navController = navController)
                    }
                }
            )
        }
    ) { paddingValue ->
        if (uiState is ProfileUiState.Idle) {
            ProfileForm(
                modifier = Modifier.padding(paddingValue),
                username = (uiState as? ProfileUiState.Idle)?.name.orEmpty(),
                userPhoneNumber = (uiState as? ProfileUiState.Idle)?.phoneNumber.orEmpty(),
                userPhotoUrl = (uiState as? ProfileUiState.Idle)?.photoUrl?.toUri().toString(),
                userLikes = (uiState as? ProfileUiState.Idle)?.likes.orEmpty(),
                handleIntent = handleIntent
            )
        }
    }
    if (uiState is ProfileUiState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x88000000))
                .clickable(enabled = false) {},
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.size(100.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun ProfileForm(
    modifier: Modifier = Modifier,
    username: String = "",
    userPhoneNumber: String = "",
    userPhotoUrl: String? = null,
    userLikes: List<String> = emptyList(),
    handleIntent: (ProfileIntent) -> Unit = {},
) {
    val context = LocalContext.current
    val cacheDir = context.cacheDir

    var name by remember { mutableStateOf(username) }
    val phoneNumber by remember { mutableStateOf(userPhoneNumber) }
    var profileImageUri by remember { mutableStateOf(userPhotoUrl?.let { Uri.parse(it) }) }

    var likeName by remember { mutableStateOf(TextFieldValue("", TextRange(0, 0))) }
    val likes = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        likes.addAll(userLikes)
    }

    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val uri = UCrop.getOutput(result.data ?: return@rememberLauncherForActivityResult)
            if (uri != null) {
                profileImageUri = uri
            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val destinationUri = Uri.fromFile(
                    File(cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
                )
                val uCrop = UCrop.of(it, destinationUri)
                    .withAspectRatio(1f, 1f) // 1:1 for profile
                    .withMaxResultSize(500, 500)

                cropLauncher.launch(uCrop.getIntent(context))
            }
        }
    )

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .clickable { galleryLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            prefix = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Gray
                )
            },
            onValueChange = { name = it },
            label = { Text("Your name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber,
            prefix = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Gray
                )
            },
            onValueChange = { },
            label = { Text("Phone number") },
            singleLine = true,
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LikesComponent(
            modifier = Modifier.fillMaxWidth(),
            name = likeName,
            onNameChange = { value -> likeName = value },
            likes = likes,
            onAddLike = { _ ->
                if (likeName.text.isNotEmpty()) {
                    likes.add(likeName.text)
                    likeName = TextFieldValue("", TextRange(0, 0))
                }
            },
            onRemoveLike = { like ->
                likes.remove(like)
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                handleIntent(
                    ProfileIntent.SaveProfile(
                        name = name,
                        photoUrl = profileImageUri.toString(),
                        likes = likes
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.weight(1f))

        Column {
            TextButton(
                onClick = {
                    handleIntent(ProfileIntent.DeleteAccount)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete account")
                }
            }
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
private fun ProfileScreenPreview() {
    FriendsSecretsTheme {
        ProfileContent(
            uiState = ProfileUiState.Idle(),
            handleIntent = {}
        )
    }
}
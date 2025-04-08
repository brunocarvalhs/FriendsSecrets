package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile

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
import androidx.compose.material.icons.filled.Download
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.commons.extensions.toBase64
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.presentation.Screen
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.LikesComponent
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.LoginNavigation
import coil.compose.AsyncImage
import com.yalantis.ucrop.UCrop
import java.io.File

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.Factory
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
       if (SessionManager.getInstance().isProfileComplete().not()) {
            navController.navigate(LoginNavigation.PhoneSend.route) {
                popUpTo(LoginNavigation.Profile.route) {
                    inclusive = true
                }
            }
       }
    }

    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Auth.route) {
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
                    if (navController.previousBackStackEntry?.destination?.route != LoginNavigation.PhoneVerification.route) {
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
    handleIntent: (ProfileIntent) -> Unit = {},
) {
    val context = LocalContext.current
    val cacheDir = context.cacheDir

    var name by remember { mutableStateOf(username) }
    val phoneNumber by remember { mutableStateOf(userPhoneNumber) }
    var profileImageUri by remember { mutableStateOf(userPhotoUrl?.let { Uri.parse(it) }) }

    var likeName by remember { mutableStateOf(TextFieldValue("", TextRange(0, 0))) }
    val likes = remember { mutableStateListOf<String>() }

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
            onValueChange = {  },
            label = { Text("Phone number") },
            singleLine = true,
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LikesComponent(
            name = likeName,
            onNameChange = { value -> likeName = value },
            likes = likes,
            onAddLike = { _ ->
                if (likeName.text.isNotBlank()) {
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
                        photoUrl = profileImageUri?.toBase64(context = context).orEmpty()
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

            TextButton(
                onClick = {
                    handleIntent(ProfileIntent.DownloadData)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Download data of account")
                }
            }
        }
    }
}

@Composable
@Preview
private fun ProfileScreenPreview() {
    ProfileContent(
        uiState = ProfileUiState.Idle(),
        handleIntent = {}
    )
}
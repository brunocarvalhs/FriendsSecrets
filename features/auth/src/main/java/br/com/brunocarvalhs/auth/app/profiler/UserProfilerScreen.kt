package br.com.brunocarvalhs.auth.app.profiler

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import br.com.brunocarvalhs.auth.commons.navigation.CreateProfileScreenRoute
import br.com.brunocarvalhs.auth.commons.navigation.PhoneSendScreenRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.GroupGraphRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.ProfileGraphRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.SettingsGraphRoute
import br.com.brunocarvalhs.friendssecrets.ui.components.BottomNavItem
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationComponent
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController, viewModel: UserProfileViewModel
) {
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Profile) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = {}, actions = {
                IconButton(onClick = {
                    navController.navigate(SettingsGraphRoute)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings"
                    )
                }
            })
        },
        bottomBar = {
            NavigationComponent(selectedItem = selectedItem, onItemSelected = { menu ->
                selectedItem = menu
                val route: Any = when (menu) {
                    is BottomNavItem.Groups -> GroupGraphRoute
                    is BottomNavItem.Profile -> ProfileGraphRoute
                }
                navController.navigate(route) {
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (val state = uiState) {
                is UserProfileUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UserProfileUiState.Idle -> {
                    UserProfileContent(name = state.data?.name,
                        phoneNumber = state.data?.phoneNumber,
                        photoUrl = state.data?.photoUrl,
                        likes = state.data?.likes.orEmpty(),
                        isAnonymous = state.isAnonymous,
                        onLogin = { navController.navigate(PhoneSendScreenRoute) },
                        onEdit = {
                            navController.navigate(CreateProfileScreenRoute) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onLogout = {
                            viewModel.handleIntent(UserProfileIntent.Logout)
                            navController.navigate(GroupGraphRoute) {
                                popUpTo(ProfileGraphRoute) {
                                    inclusive = true
                                }
                            }
                        },
                        onDeleteAccount = {
                            viewModel.handleIntent(UserProfileIntent.DeleteAccount)
                            navController.navigate(GroupGraphRoute) {
                                popUpTo(ProfileGraphRoute) {
                                    inclusive = true
                                }
                            }
                        })
                }

                is UserProfileUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = Color.Red)
                    }
                }

                is UserProfileUiState.Success -> {
                    // Ex: Navegar para outra tela ou exibir snackbar
                }
            }
        }
    }
}

@Composable
fun UserProfileContent(
    name: String?,
    phoneNumber: String?,
    photoUrl: String?,
    likes: List<String>,
    isAnonymous: Boolean,
    onLogin: () -> Unit,
    onEdit: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        ProfilePicture(photoUrl)

        Spacer(Modifier.height(16.dp))

        ProfileNameAndStatus(name, isAnonymous)

        Spacer(Modifier.height(24.dp))

        if (!isAnonymous && !phoneNumber.isNullOrEmpty()) {
            ProfilePhone(phoneNumber)
            Spacer(Modifier.height(16.dp))
        }

        if (likes.isNotEmpty()) {
            LikesSection(likes)
            Spacer(Modifier.height(32.dp))
        }

        ActionsSection(
            isAnonymous = isAnonymous,
            onLogin = onLogin,
            onEdit = onEdit,
            onLogout = onLogout,
            onDeleteAccount = onDeleteAccount
        )
    }
}

@Composable
fun ProfilePicture(photoUrl: String?) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.LightGray),
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
fun ProfileNameAndStatus(name: String?, isAnonymous: Boolean) {
    Text(
        text = name ?: "Usuário Anônimo", style = MaterialTheme.typography.titleLarge
    )
    Text(
        text = if (isAnonymous) "Modo Anônimo" else "Usuário Verificado",
        style = MaterialTheme.typography.labelMedium,
        color = Color.Gray
    )
}

@Composable
fun ProfilePhone(phone: String) {
    OutlinedTextField(
        value = phone,
        onValueChange = {},
        enabled = false,
        label = { Text("Telefone") },
        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun LikesSection(likes: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Seus gostos", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            likes.forEach { like ->
                AssistChip(onClick = {}, label = { Text(like) })
            }
        }
    }
}

@Composable
fun ActionsSection(
    isAnonymous: Boolean,
    onLogin: () -> Unit,
    onEdit: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    if (!isAnonymous) {
        Button(
            onClick = onEdit, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Editar Perfil")
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onLogout, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sair")
        }

        TextButton(
            onClick = onDeleteAccount, modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                Spacer(Modifier.width(4.dp))
                Text("Excluir Conta", color = Color.Red)
            }
        }
    } else {
        Button(
            onClick = onLogin,
        ) {
            Text(
                text = "Faça login para acessar recursos do perfil.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

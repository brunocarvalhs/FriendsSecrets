package br.com.brunocarvalhs.auth.app.profiler

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
    navController: NavController,
    viewModel: UserProfileViewModel
) {
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Profile) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Perfil", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { navController.navigate(SettingsGraphRoute) }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Configurações")
                    }
                }
            )
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is UserProfileUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UserProfileUiState.Idle -> {
                    UserProfileContent(
                        name = state.data?.name,
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
                                popUpTo(ProfileGraphRoute) { inclusive = true }
                            }
                        },
                        onDeleteAccount = {
                            viewModel.handleIntent(UserProfileIntent.DeleteAccount)
                            navController.navigate(GroupGraphRoute) {
                                popUpTo(ProfileGraphRoute) { inclusive = true }
                            }
                        }
                    )
                }

                is UserProfileUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                    }
                }

                is UserProfileUiState.Success -> {

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
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { ProfilePicture(photoUrl) }
        item { ProfileNameAndStatus(name, isAnonymous) }
        if (!isAnonymous && !phoneNumber.isNullOrEmpty()) {
            item { ProfilePhone(phoneNumber) }
        }
        if (likes.isNotEmpty()) {
            item { LikesSectionWithShowMore(likes) }
        }
        item {
            ActionsSection(
                isAnonymous = isAnonymous,
                onLogin = onLogin,
                onEdit = onEdit,
                onLogout = onLogout,
                onDeleteAccount = onDeleteAccount
            )
        }
    }
}

@Composable
fun LikesSectionWithShowMore(likes: List<String>) {
    var showAll by remember { mutableStateOf(false) }
    val displayLikes = if (showAll) likes else likes.take(5)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Text("Seus gostos", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            displayLikes.forEach { like ->
                AssistChip(onClick = {}, label = { Text(like) })
            }
        }
        if (likes.size > 5) {
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = { showAll = !showAll },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(if (showAll) "Mostrar menos" else "Mostrar mais")
            }
        }
    }
}

@Composable
fun ProfilePicture(photoUrl: String?) {
    Card(
        modifier = Modifier.size(130.dp),
        shape = CircleShape,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (photoUrl != null) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Foto do perfil",
                    modifier = Modifier.clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar padrão",
                    modifier = Modifier.size(110.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ProfileNameAndStatus(name: String?, isAnonymous: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name ?: "Usuário Anônimo",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = if (isAnonymous) "Modo Anônimo" else "Usuário Verificado",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun ProfilePhone(phone: String) {
    OutlinedTextField(
        value = phone,
        onValueChange = {},
        enabled = false,
        label = { Text("Telefone") },
        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun ActionsSection(
    isAnonymous: Boolean,
    onLogin: () -> Unit,
    onEdit: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (!isAnonymous) {
            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Editar Perfil")
            }

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Sair")
            }

            TextButton(
                onClick = onDeleteAccount,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Excluir Conta", color = MaterialTheme.colorScheme.error)
                }
            }
        } else {
            OutlinedButton(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Faça login para acessar recursos do perfil.",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

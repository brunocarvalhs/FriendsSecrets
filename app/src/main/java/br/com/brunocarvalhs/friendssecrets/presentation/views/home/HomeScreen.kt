package br.com.brunocarvalhs.friendssecrets.presentation.views.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.GroupNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory
    ),
) {
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.event(HomeIntent.FetchGroups)
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {

                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More"
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = { /* Handle edit! */ },
                            leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = { /* Handle settings! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Settings,
                                    contentDescription = null
                                )
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Send Feedback") },
                            onClick = { /* Handle send feedback! */ },
                            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                            trailingIcon = { Text("F11", textAlign = TextAlign.Center) }
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                navController.navigate(GroupNavigation.Create.route)
            }) {
                Icon(Icons.Filled.Add, "Add")
                Text(stringResource(R.string.action_create_group))
            }
        }
    ) {
        when (val state = uiState) {
            is HomeUiState.Error -> {
                Column(
                    modifier = Modifier.padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.errorMessage)
                }
            }

            HomeUiState.Loading -> {
                Row(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(state.list) { item ->
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            onClick = {
                                navController.navigate(
                                    route = GroupNavigation.Read.createRoute(item.id)
                                )
                            }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = item.name)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    FriendsSecretsTheme {
        HomeScreen(
            navController = rememberNavController(),
            viewModel = viewModel(
                factory = HomeViewModel.Factory
            )
        )
    }
}

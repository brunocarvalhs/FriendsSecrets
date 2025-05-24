@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.MembersAvatar
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsPreviewProvider
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderComponent(
    uiState: GroupDetailsUiState,
    navController: NavController = rememberNavController(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    exitGroup: (GroupEntities) -> Unit = {},
    deleteGroup: (GroupEntities) -> Unit = {},
    onDraw: (GroupEntities) -> Unit = {},
    onShareGroup: (GroupEntities) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    LargeTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = {
            if (uiState is GroupDetailsUiState.Success) {
                Text(
                    text = uiState.group.name,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                )
            }
        },
        actions = {
            if (uiState is GroupDetailsUiState.Success) {
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    MembersAvatar(
                        members = uiState.group.members,
                        size = 32
                    )
                }
            }
        },
        navigationIcon = {
            NavigationBackIconButton(navController = navController)
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
@Preview
private fun HeaderComponentPreview(
    @PreviewParameter(GroupDetailsPreviewProvider::class) state: GroupDetailsUiState
) {
    FriendsSecretsTheme {
        HeaderComponent(
            uiState = state,
            exitGroup = {},
            deleteGroup = {},
            onDraw = {},
            onShareGroup = {}
        )
    }
}
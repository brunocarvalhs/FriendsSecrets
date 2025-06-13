package br.com.brunocarvalhs.group.app.details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.group.app.details.GroupDetailsUiState
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderComponent(
    uiState: GroupDetailsUiState,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    exitGroup: KFunction1<GroupEntities, Unit>,
    deleteGroup: KFunction1<GroupEntities, Unit>,
    onDraw: KFunction1<GroupEntities, Unit>,
    onShareGroup: KFunction1<GroupEntities, Unit>
) {
    LargeTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
    ), title = {
        if (uiState is GroupDetailsUiState.Success) {
            Text(uiState.group.name)
        }
    }, actions = {

    }, navigationIcon = {
        NavigationBackIconButton(navController = navController)
    }, scrollBehavior = scrollBehavior
    )
}

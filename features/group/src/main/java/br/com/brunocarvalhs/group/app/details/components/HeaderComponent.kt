package br.com.brunocarvalhs.group.app.details.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
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
    TODO("Not yet implemented")
}

package br.com.brunocarvalhs.group.app.list.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import br.com.brunocarvalhs.group.R
import br.com.brunocarvalhs.group.app.list.HomeUiState
import br.com.brunocarvalhs.group.commons.navigation.GroupCreateScreenRoute

@Composable
internal fun HomeFloatingActionButton(
    navController: NavController,
    uiState: HomeUiState.Success,
    isCreateGroupEnabled: Boolean = true,
){
    if (uiState.list.isEmpty()) return

    if (isCreateGroupEnabled) {
        ExtendedFloatingActionButton(onClick = {
            navController.navigate(GroupCreateScreenRoute)
        }) {
            Icon(Icons.Filled.Add, "Add")
            Text(stringResource(R.string.home_action_create_group))
        }
    }
}
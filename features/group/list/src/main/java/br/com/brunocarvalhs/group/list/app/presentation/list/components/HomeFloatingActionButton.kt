package br.com.brunocarvalhs.group.list.app.presentation.list.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.brunocarvalhs.group.list.R
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel

@Composable
internal fun HomeFloatingActionButton(
    list: List<GroupModel>,
    onEvent: () -> Unit = {},
    isCreateGroupEnabled: Boolean = true,
){
    if (list.isEmpty()) return

    if (isCreateGroupEnabled) {
        ExtendedFloatingActionButton(onClick = onEvent) {
            Icon(Icons.Filled.Add, "Add")
            Text(stringResource(R.string.home_action_create_group))
        }
    }
}
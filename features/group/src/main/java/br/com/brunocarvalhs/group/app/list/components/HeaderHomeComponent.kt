package br.com.brunocarvalhs.group.app.list.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.group.R
import br.com.brunocarvalhs.group.app.list.HomeIntent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
internal fun HeaderHomeComponent(
    session: UserEntities? = null,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    isSettingsEnabled: Boolean = true,
    isJoinGroupEnabled: Boolean = true,
    onEvent: (HomeIntent) -> Unit = {},
    onShowBottomSheet: (Boolean) -> Unit = {},
    notificationsCount: Int = 0,
) {
    var expanded by remember { mutableStateOf(false) }

    LargeTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
    ), title = {
        val text = session?.let {
            "${stringResource(R.string.home_title)} ${it.name}"
        } ?: stringResource(R.string.home_title)
        Text(
            text = text, style = MaterialTheme.typography.titleLarge
        )
    }, actions = {
        IconButton(onClick = { onShowBottomSheet(true) }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add"
            )
        }
        IconButton(onClick = { expanded = true }) {
            BadgedBox(badge = {
                if (notificationsCount > 0) {
                    Badge()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    }, scrollBehavior = scrollBehavior
    )
}

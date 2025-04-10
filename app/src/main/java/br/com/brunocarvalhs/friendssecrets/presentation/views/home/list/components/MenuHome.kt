package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.presentation.Screen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.LoginNavigation

sealed class MenuItem(
    val title: @Composable () -> String,
    val route: String? = null,
    val icon: ImageVector? = null,
    val isEnabled: Boolean
) {

    data object JoinGroup : MenuItem(
        title = { stringResource(id = R.string.home_drop_menu_item_text_join_a_group) },
        icon = Icons.Outlined.Edit,
        isEnabled = true,
    )

    data object Settings : MenuItem(
        title = { stringResource(R.string.home_drop_menu_item_text_settings) },
        route = Screen.Settings.route,
        icon = Icons.Outlined.Settings,
        isEnabled = true
    )

    data object Profile : MenuItem(
        title = { stringResource(id = R.string.home_drop_menu_item_text_profile) },
        route = LoginNavigation.Profile.route,
        icon = Icons.Outlined.Person,
        isEnabled = true
    )

    data object Logout : MenuItem(
        title = { stringResource(id = R.string.home_drop_menu_item_text_logout) },
        icon = Icons.AutoMirrored.Outlined.Logout,
        isEnabled = true
    )

    companion object {
        val values = listOf(
            Profile,
            JoinGroup,
            Settings,
            Logout
        )
    }
}

@Composable
fun MenuHome(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClick: (MenuItem) -> Unit,
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        MenuItem.values.forEach {
            if (!it.isEnabled) return@forEach

            DropdownMenuItem(
                text = { Text(it.title()) },
                onClick = {
                    onDismissRequest.invoke()
                    onClick(it)
                },
                leadingIcon = {
                    it.icon?.let { icon ->
                        Icon(
                            icon,
                            contentDescription = it.title()
                        )
                    }
                }
            )
        }
    }
}

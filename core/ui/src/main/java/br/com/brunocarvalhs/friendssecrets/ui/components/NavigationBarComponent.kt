package br.com.brunocarvalhs.friendssecrets.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewTimeline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import br.com.brunocarvalhs.friendssecrets.ui.R

sealed class BottomNavItem(val label: Int, val icon: ImageVector) {
    data object Groups :
        BottomNavItem(R.string.home_drop_menu_item_text_profile, Icons.Outlined.GridView)

    data object Profile :
        BottomNavItem(R.string.home_drop_menu_item_text_profile, Icons.Outlined.Person)
}


@Composable
fun NavigationComponent(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    NavigationBar {
        listOf(
            BottomNavItem.Groups,
            BottomNavItem.Profile
        ).forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(stringResource(id = item.label)) },
                selected = item == selectedItem,
                onClick = {
                    if (item != selectedItem) {
                        onItemSelected(item)
                    }
                }
            )
        }
    }
}


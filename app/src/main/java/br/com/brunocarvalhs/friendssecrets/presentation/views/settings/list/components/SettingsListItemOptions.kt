package br.com.brunocarvalhs.friendssecrets.presentation.views.settings.list.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ForwardToInbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@Composable
fun SettingsListItemOptions(
    navController: NavHostController,
    title: String,
    icon: ImageVector,
    onClick: (Boolean) -> Unit,
) {
    var checked by rememberSaveable { mutableStateOf(value = false) }

    ListItem(
        modifier = Modifier.clickable { onClick.invoke(checked.not()) },
        headlineContent = { Text(title) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = {
                    onClick.invoke(it)
                    checked = it
                }
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = title,
            )
        }
    )
    HorizontalDivider()
}

@Composable
@Preview
private fun SettingsListItemOptionsPreview() {
    FriendsSecretsTheme {
        SettingsListItemOptions(
            navController = rememberNavController(),
            icon = Icons.AutoMirrored.Filled.ForwardToInbox,
            title = "Notifications",
            onClick = {}
        )
    }
}
package br.com.brunocarvalhs.friendssecrets.presentation.views.settings.list.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.SettingsNavigation

@Composable
fun SettingsListItemNavigation(
    navController: NavHostController,
    settings: SettingsNavigation,
) {
    ListItem(
        modifier = Modifier.clickable { navController.navigate(route = settings.route) },
        headlineContent = { Text(stringResource(settings.title)) },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Localized description",
            )
        },
        leadingContent = {
            Icon(
                imageVector = settings.icon,
                contentDescription = stringResource(settings.title),
            )
        }
    )
    HorizontalDivider()
}

@Composable
@Preview
private fun SettingsListItemNavigationPreview() {
    FriendsSecretsTheme {
        SettingsListItemNavigation(
            navController = rememberNavController(),
            settings = SettingsNavigation.Appearance
        )
    }
}
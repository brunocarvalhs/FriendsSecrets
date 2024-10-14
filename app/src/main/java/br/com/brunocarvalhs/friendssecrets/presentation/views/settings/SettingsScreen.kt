package br.com.brunocarvalhs.friendssecrets.presentation.views.settings

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme

@Composable
fun SettingsScreen(
    navController: NavHostController,
    generalRouters: List<SettingsNavigation>? = null,
    supportRouters: List<SettingsNavigation>? = null,
) {
    SettingsContent(
        navController = navController,
        generalRouters = generalRouters,
        supportRouters = supportRouters
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    navController: NavHostController,
    generalRouters: List<SettingsNavigation>? = null,
    supportRouters: List<SettingsNavigation>? = null
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = SettingsNavigation.Settings.title)
                },
                navigationIcon = {
                    NavigationBackIconButton(navController = navController)
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(modifier = Modifier.padding(it).padding(16.dp)) {
            generalRouters?.let {
                Column {
                    Text(text = "General", modifier = Modifier.padding(top = 16.dp))
                    generalRouters.forEach {
                        ListItem(
                            modifier = Modifier.clickable { navController.navigate(it.route) },
                            headlineContent = { Text(it.title) },
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Localized description",
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = it.icon,
                                    contentDescription = it.title,
                                )
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }

            supportRouters?.let {
                Column {
                    Text(text = "Support", modifier = Modifier.padding(top = 16.dp))
                    supportRouters.forEach {
                        ListItem(
                            modifier = Modifier.clickable { navController.navigate(it.route) },
                            headlineContent = { Text(it.title) },
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Localized description",
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = it.icon,
                                    contentDescription = it.title,
                                )
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
private fun SettingsContentPreview() {
    FriendsSecretsTheme {
        SettingsContent(
            navController = rememberNavController(),
            generalRouters = listOf(
                SettingsNavigation.Appearance,
            ),
            supportRouters = listOf(
                SettingsNavigation.ReportIssue,
                SettingsNavigation.FAQ
            )
        )
    }
}
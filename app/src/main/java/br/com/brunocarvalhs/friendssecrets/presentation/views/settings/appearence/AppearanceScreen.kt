package br.com.brunocarvalhs.friendssecrets.presentation.views.settings.appearence

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
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
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.SettingsNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.appearence.components.Theme
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.appearence.components.ThemeSelect

@Composable
fun AppearanceScreen(navController: NavHostController) {
    AppearanceContent(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppearanceContent(
    navController: NavHostController,
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
                    Text(text = SettingsNavigation.Appearance.title)
                },
                navigationIcon = {
                    NavigationBackIconButton(navController = navController)
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            item {
                Text(text = "Selecting a particular option will change the appearance (coloring) of the application according to your preferences.")
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                ThemeSelect(
                    modifier = Modifier.fillMaxWidth(),
                    selected = "LIGHT",
                    onClick = { theme ->
                        when (theme) {
                            Theme.LIGHT.name -> {

                            }
                            Theme.DARK.name -> {

                            }
                            Theme.SYSTEM.name -> {

                            }
                        }
                    },
                )
            }
            item {

            }
        }
    }
}



@Composable
@Preview
private fun AppearanceContentPreview() {
    FriendsSecretsTheme {
        AppearanceContent(
            navController = rememberNavController()
        )
    }
}
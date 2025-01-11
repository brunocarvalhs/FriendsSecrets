package br.com.brunocarvalhs.friendssecrets.presentation.views.settings.faq

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.SettingsNavigation

@Composable
fun FAQScreen(navController: NavHostController) {
    FAQContent(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FAQContent(
    navController: NavHostController
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
                    Text(text = stringResource(SettingsNavigation.FAQ.title))
                },
                navigationIcon = {
                    NavigationBackIconButton(navController = navController)
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(modifier = Modifier.padding(it).padding(16.dp)) {

        }
    }
}

@Composable
@Preview
private fun FAQContentPreview() {
    FriendsSecretsTheme {
        FAQContent(
            navController = rememberNavController()
        )
    }
}
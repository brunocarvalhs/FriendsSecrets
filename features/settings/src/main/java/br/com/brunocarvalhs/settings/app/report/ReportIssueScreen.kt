package br.com.brunocarvalhs.settings.app.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.ui.components.WebViewContainer
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.SettingsNavigation

@Composable
fun ReportIssueScreen(navController: NavHostController) {
    ReportIssueContent(
        navController = navController
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportIssueContent(
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = stringResource(SettingsNavigation.ReportIssue.title))
                },
                navigationIcon = {
                    NavigationBackIconButton(navController = navController)
                },
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            WebViewContainer(
                url = "https://github.com/brunocarvalhs/FriendsSecrets/issues/new",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@Preview
private fun ReportIssuePreview() {
    FriendsSecretsTheme {
        ReportIssueContent(
            navController = rememberNavController()
        )
    }
}
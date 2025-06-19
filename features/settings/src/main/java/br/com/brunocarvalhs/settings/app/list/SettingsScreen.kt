package br.com.brunocarvalhs.settings.app.list

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.sharp.Fingerprint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleKeys
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.settings.R
import br.com.brunocarvalhs.settings.app.list.components.SettingsListItemNavigation
import br.com.brunocarvalhs.settings.app.list.components.SettingsListItemOptions
import br.com.brunocarvalhs.settings.commons.navigation.AppearanceScreenRoute
import br.com.brunocarvalhs.settings.commons.navigation.FAQScreenRoute
import br.com.brunocarvalhs.settings.commons.navigation.ReportIssueScreenRoute
import br.com.brunocarvalhs.settings.commons.toggles.getToggles

@Composable
fun SettingsScreen(
    navController: NavHostController,
    toggleManager: ToggleManager,
    viewModel: SettingsViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SettingsContent(
        navController = navController,
        state = state,
        onIntent = viewModel::onEvent,
        toggle = getToggles(toggleManager),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    navController: NavHostController,
    toggle: Map<ToggleKeys, Boolean>,
    onIntent: (SettingsIntent) -> Unit = {},
    state: SettingsState = SettingsState(),
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        LargeTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ), title = {
            Text(text = stringResource(R.string.title_settings))
        }, navigationIcon = {
            NavigationBackIconButton(navController = navController)
        }, scrollBehavior = scrollBehavior
        )
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            Column {
                if (
                    toggle[ToggleKeys.SETTINGS_IS_APPEARANCE_ENABLED] == true ||
                    toggle[ToggleKeys.SETTINGS_IS_FINGERPRINT_ENABLED] == true
                ) {
                    Text(
                        text = stringResource(R.string.settings_screen_general),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                if (toggle[ToggleKeys.SETTINGS_IS_FINGERPRINT_ENABLED] == true) {
                    SettingsListItemOptions(
                        selected = state.isBiometricPromptEnabled,
                        title = stringResource(R.string.settings_screen_security),
                        icon = Icons.Sharp.Fingerprint,
                        onClick = { state -> onIntent(SettingsIntent.SetBiometricPromptEnabled(state)) })
                }
                if (toggle[ToggleKeys.SETTINGS_IS_APPEARANCE_ENABLED] == true) {
                    SettingsListItemNavigation(
                        title = R.string.title_appearance,
                        icon = Icons.Outlined.Palette,
                        onClick = { navController.navigate(AppearanceScreenRoute) }
                    )
                }
            }

            Column {
                if (toggle[ToggleKeys.SETTINGS_IS_REPORT_ISSUE_ENABLED] == true ||
                    toggle[ToggleKeys.SETTINGS_IS_FAQ_ENABLED] == true
                ) {
                    Text(
                        text = stringResource(R.string.settings_screen_support),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                if (toggle[ToggleKeys.SETTINGS_IS_REPORT_ISSUE_ENABLED] == true) {
                    SettingsListItemNavigation(
                        title = R.string.title_report_an_issue,
                        icon = Icons.Outlined.Report,
                        onClick = { navController.navigate(ReportIssueScreenRoute) }
                    )
                }
                if (toggle[ToggleKeys.SETTINGS_IS_FAQ_ENABLED] == true) {
                    SettingsListItemNavigation(
                        title = R.string.title_faq,
                        icon = Icons.Outlined.Info,
                        onClick = { navController.navigate(FAQScreenRoute) }
                    )
                }
            }
        }
    }
}


@Preview(
    name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO
)
@Composable
private fun SettingsContentPreview() {
    FriendsSecretsTheme {
        SettingsContent(
            navController = rememberNavController(),
            toggle = mapOf(
                ToggleKeys.SETTINGS_IS_FINGERPRINT_ENABLED to true,
                ToggleKeys.SETTINGS_IS_APPEARANCE_ENABLED to true,
                ToggleKeys.SETTINGS_IS_REPORT_ISSUE_ENABLED to true,
            ),
            state = SettingsState(),
            onIntent = {

            }
        )
    }
}
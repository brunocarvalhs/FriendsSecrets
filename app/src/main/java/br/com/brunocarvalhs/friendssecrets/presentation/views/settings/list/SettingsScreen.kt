package br.com.brunocarvalhs.friendssecrets.presentation.views.settings.list

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Fingerprint
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
import br.com.brunocarvalhs.friendssecrets.commons.security.BiometricManager
import br.com.brunocarvalhs.friendssecrets.commons.toggle.ToggleKeys
import br.com.brunocarvalhs.friendssecrets.commons.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.presentation.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.SettingsNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.list.components.SettingsListItemNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.list.components.SettingsListItemOptions

@Composable
fun SettingsScreen(
    navController: NavHostController,
    toggleManager: ToggleManager,
) {
    SettingsContent(
        navController = navController,
        isFingerprintEnabled = toggleManager
            .isFeatureEnabled(ToggleKeys.SETTINGS_IS_FINGERPRINT_ENABLED),
        isAppearanceEnabled = toggleManager
            .isFeatureEnabled(ToggleKeys.SETTINGS_IS_APPEARANCE_ENABLED),
        isReportIssueEnabled = toggleManager
            .isFeatureEnabled(ToggleKeys.SETTINGS_IS_REPORT_ISSUE_ENABLED),
        isFAQEnabled = toggleManager
            .isFeatureEnabled(ToggleKeys.SETTINGS_IS_FAQ_ENABLED),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    navController: NavHostController,
    isFingerprintEnabled: Boolean = true,
    isAppearanceEnabled: Boolean = true,
    isReportIssueEnabled: Boolean = true,
    isFAQEnabled: Boolean = true,
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
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            Column {
                if (isAppearanceEnabled || isFingerprintEnabled) {
                    Text(text = "General", modifier = Modifier.padding(top = 16.dp))
                }
                if (isFingerprintEnabled) {
                    SettingsListItemOptions(
                        selected = BiometricManager.isBiometricPromptEnabled(),
                        title = "Fingerprint",
                        icon = Icons.Sharp.Fingerprint,
                        onClick = { state -> BiometricManager.setBiometricPromptEnabled(state) }
                    )
                }
                if (isAppearanceEnabled) {
                    SettingsListItemNavigation(navController, SettingsNavigation.Appearance)
                }
            }

            Column {
                if (isReportIssueEnabled || isFAQEnabled) {
                    Text(text = "Support", modifier = Modifier.padding(top = 16.dp))
                }
                if (isReportIssueEnabled) {
                    SettingsListItemNavigation(navController, SettingsNavigation.ReportIssue)
                }
                if (isFAQEnabled) {
                    SettingsListItemNavigation(navController, SettingsNavigation.FAQ)
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
        )
    }
}
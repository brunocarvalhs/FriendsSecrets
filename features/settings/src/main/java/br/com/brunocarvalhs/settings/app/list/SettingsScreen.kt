package br.com.brunocarvalhs.settings.app.list

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.Fingerprint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import br.com.brunocarvalhs.friendssecrets.ui.remembers.rememberReviewRequester
import br.com.brunocarvalhs.settings.R
import br.com.brunocarvalhs.settings.app.list.components.SettingsListItemNavigation
import br.com.brunocarvalhs.settings.app.list.components.SettingsListItemOptions

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onAppearance: () -> Unit = {},
    onReportIssue: () -> Unit = {},
    onFAQ: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        isBiometricPromptEnabled = state.isBiometricPromptEnabled,
        onBack = onBack,
        onBiometricPrompt = {
            viewModel.handleIntent(SettingsIntent.SetBiometricPromptEnabled(it))
        },
        onAppearance = onAppearance,
        onReportIssue = onReportIssue,
        onFAQ = onFAQ
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    isBiometricPromptEnabled: Boolean = false,
    onBack: () -> Unit = {},
    onBiometricPrompt: (Boolean) -> Unit = {},
    onAppearance: () -> Unit = {},
    onReportIssue: () -> Unit = {},
    onFAQ: () -> Unit = {},
) {
    val requestReview = rememberReviewRequester()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        LargeTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
            ), title = {
                Text(text = stringResource(R.string.title_settings))
            }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }, scrollBehavior = scrollBehavior
        )
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.settings_screen_general),
                    modifier = Modifier.padding(top = 16.dp)
                )
                SettingsListItemOptions(
                    selected = isBiometricPromptEnabled,
                    title = stringResource(R.string.settings_screen_security),
                    icon = Icons.Sharp.Fingerprint,
                    onClick = onBiometricPrompt
                )
                SettingsListItemNavigation(
                    title = R.string.title_appearance,
                    icon = Icons.Outlined.Palette,
                    onClick = onAppearance
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.settings_screen_support),
                    modifier = Modifier.padding(top = 16.dp)
                )
                SettingsListItemNavigation(
                    title = R.string.title_report_an_issue,
                    icon = Icons.Outlined.Report,
                    onClick = onReportIssue
                )
                SettingsListItemNavigation(
                    title = R.string.title_faq,
                    icon = Icons.Outlined.Info,
                    onClick = onFAQ
                )
                SettingsListItemNavigation(
                    title = R.string.title_review,
                    icon = Icons.Outlined.Star,
                    onClick = { requestReview() }
                )
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
    SettingsContent(
        isBiometricPromptEnabled = true,
        onBack = {},
        onBiometricPrompt = {},
        onAppearance = {},
        onReportIssue = {},
        onFAQ = {}
    )
}

@Preview(
    name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO
)
@Composable
private fun SettingsContentIsBiometricPromptDisabledPreview() {
    SettingsContent(
        isBiometricPromptEnabled = false,
        onBack = {},
        onBiometricPrompt = {},
        onAppearance = {},
        onReportIssue = {},
        onFAQ = {}
    )
}
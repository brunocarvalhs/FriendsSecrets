package br.com.brunocarvalhs.settings.app.list

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.Fingerprint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.settings.R
import br.com.brunocarvalhs.settings.app.list.components.SettingsListItemNavigation
import br.com.brunocarvalhs.settings.app.list.components.SettingsListItemOptions
import br.com.brunocarvalhs.settings.commons.remembers.rememberReviewRequester

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onAppearance: () -> Unit = {},
    onPrivacyPolicy: () -> Unit = {},
    onTermsAndConditions: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    SettingsContent(
        isBiometricPromptEnabled = state.isBiometricPromptEnabled,
        isBiometricSupported = state.isBiometricSupported && state.isFingerprintFeatureEnabled,
        isAppearanceEnabled = state.isAppearanceEnabled,
        versionName = state.versionName,
        versionCode = state.versionCode,
        onBack = onBack,
        onBiometricPrompt = {
            viewModel.handleIntent(SettingsIntent.SetBiometricPromptEnabled(it))
        },
        onAppearance = onAppearance,
        onPrivacyPolicy = onPrivacyPolicy,
        onTermsAndConditions = onTermsAndConditions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    isBiometricPromptEnabled: Boolean = false,
    isBiometricSupported: Boolean = false,
    isAppearanceEnabled: Boolean = true,
    versionName: String = "",
    versionCode: String = "",
    onBack: () -> Unit = {},
    onBiometricPrompt: (Boolean) -> Unit = {},
    onAppearance: () -> Unit = {},
    onPrivacyPolicy: () -> Unit = {},
    onTermsAndConditions: () -> Unit = {}
) {
    val requestReview = rememberReviewRequester()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = { Text(text = stringResource(R.string.title_settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            GeneralSection(
                isBiometricSupported = isBiometricSupported,
                isBiometricPromptEnabled = isBiometricPromptEnabled,
                isAppearanceEnabled = isAppearanceEnabled,
                onBiometricPrompt = onBiometricPrompt,
                onAppearance = onAppearance
            )

            SupportSection(
                onReview = { requestReview() },
                onPrivacyPolicy = onPrivacyPolicy,
                onTermsAndConditions = onTermsAndConditions
            )

            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null
                    )
                },
                headlineContent = {
                    Text(
                        text = stringResource(R.string.settings_version_name, versionName),
                    )
                },
                supportingContent = {
                    Text(
                        text = stringResource(R.string.settings_version_code, versionCode),
                    )
                }
            )
        }
    }
}

@Composable
private fun GeneralSection(
    isBiometricSupported: Boolean,
    isBiometricPromptEnabled: Boolean,
    isAppearanceEnabled: Boolean,
    onBiometricPrompt: (Boolean) -> Unit,
    onAppearance: () -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.settings_screen_general),
            modifier = Modifier.padding(top = 16.dp)
        )
        if (isBiometricSupported) {
            SettingsListItemOptions(
                title = stringResource(R.string.settings_screen_security),
                icon = Icons.Sharp.Fingerprint,
                selected = isBiometricPromptEnabled,
                onClick = onBiometricPrompt
            )
        }
        if (isAppearanceEnabled) {
            SettingsListItemNavigation(
                title = R.string.title_appearance,
                icon = Icons.Outlined.Palette,
                onClick = onAppearance
            )
        }
    }
}

@Composable
private fun SupportSection(
    onReview: () -> Unit,
    onPrivacyPolicy: () -> Unit,
    onTermsAndConditions: () -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.settings_screen_support),
            modifier = Modifier.padding(top = 16.dp)
        )
        SettingsListItemNavigation(
            title = R.string.title_review,
            icon = Icons.Outlined.Star,
            onClick = onReview
        )
        Text(
            text = stringResource(R.string.settings_screen_legal),
            modifier = Modifier.padding(top = 16.dp)
        )
        SettingsListItemNavigation(
            title = R.string.title_privacy_policy,
            icon = Icons.Outlined.PrivacyTip,
            onClick = onPrivacyPolicy
        )
        SettingsListItemNavigation(
            title = R.string.title_terms_and_conditions,
            icon = Icons.Outlined.Description,
            onClick = onTermsAndConditions
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
internal fun SettingsContentPreview() {
    SettingsContent(
        isBiometricPromptEnabled = true,
        isBiometricSupported = true,
        versionName = "1.0.0",
        versionCode = "1",
        onBack = {},
        onBiometricPrompt = {},
        onAppearance = {},
    )
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
internal fun SettingsContentIsBiometricPromptDisabledPreview() {
    SettingsContent(
        isBiometricPromptEnabled = false,
        isBiometricSupported = false,
        versionName = "1.0.0",
        versionCode = "1",
        onBack = {},
        onBiometricPrompt = {},
        onAppearance = {},
    )
}

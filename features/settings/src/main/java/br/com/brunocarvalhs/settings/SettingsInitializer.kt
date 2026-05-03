package br.com.brunocarvalhs.settings

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.core.navigation.routers.SettingsGraph
import br.com.brunocarvalhs.settings.app.appearence.AppearanceScreen
import br.com.brunocarvalhs.settings.app.appearence.AppearanceViewModel
import br.com.brunocarvalhs.settings.app.faq.FAQScreen
import br.com.brunocarvalhs.settings.app.list.SettingsScreen
import br.com.brunocarvalhs.settings.app.privacyPolicy.PrivacyPolicyScreen
import br.com.brunocarvalhs.settings.app.report.ReportIssueScreen
import br.com.brunocarvalhs.settings.app.termsAndConditions.TermsAndConditionScreen
import br.com.brunocarvalhs.settings.commons.navigation.AppearanceScreenRoute
import br.com.brunocarvalhs.settings.commons.navigation.FAQScreenRoute
import br.com.brunocarvalhs.settings.commons.navigation.PrivacyPolicyScreenRoute
import br.com.brunocarvalhs.settings.commons.navigation.ReportIssueScreenRoute
import br.com.brunocarvalhs.settings.commons.navigation.SettingsScreenRoute
import br.com.brunocarvalhs.settings.commons.navigation.TermsAndConditionsScreenRoute
import kotlin.properties.Delegates

class SettingsInitializer(private val builder: Builder) {

    private fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<SettingsGraph>(startDestination = SettingsScreenRoute) {
            composable<SettingsScreenRoute> {
                SettingsScreen(
                    viewModel = hiltViewModel(),
                    onBack = builder.onBack,
                    onAppearance = { builder.navController.navigate(AppearanceScreenRoute) },
                    onPrivacyPolicy = { builder.navController.navigate(PrivacyPolicyScreenRoute) },
                    onTermsAndConditions = { builder.navController.navigate(TermsAndConditionsScreenRoute) }
                )
            }
            composable<AppearanceScreenRoute> {
                val viewModel = hiltViewModel<AppearanceViewModel>()
                AppearanceScreen(
                    viewModel = viewModel,
                    onBack = builder.onBack
                )
            }
            composable<FAQScreenRoute> {
                FAQScreen(
                    onBack = builder.onBack
                )
            }
            composable<ReportIssueScreenRoute> {
                ReportIssueScreen(
                    onBack = builder.onBack
                )
            }
            composable<PrivacyPolicyScreenRoute> {
                PrivacyPolicyScreen(
                    onBack = builder.onBack
                )
            }
            composable<TermsAndConditionsScreenRoute> {
                TermsAndConditionScreen(
                    onBack = builder.onBack
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onBack: () -> Unit = {}

        fun navController(navController: NavHostController) =
            apply { this.navController = navController }

        fun onBack(onBack: () -> Unit) =
            apply { this.onBack = onBack }

        fun build(navGraphBuilder: NavGraphBuilder): SettingsInitializer =
            SettingsInitializer(this).also { it.build(navGraphBuilder) }
    }
}

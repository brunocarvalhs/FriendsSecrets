package br.com.brunocarvalhs.settings

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.settings.app.appearence.AppearanceScreen
import br.com.brunocarvalhs.settings.app.appearence.AppearanceViewModel
import br.com.brunocarvalhs.settings.app.faq.FAQScreen
import br.com.brunocarvalhs.settings.app.list.SettingsScreen
import br.com.brunocarvalhs.settings.app.report.ReportIssueScreen
import br.com.brunocarvalhs.settings.commons.navigation.AppearanceScreenRoute
import br.com.brunocarvalhs.settings.commons.navigation.FAQScreenRoute
import br.com.brunocarvalhs.settings.commons.navigation.ReportIssueScreenRoute
import br.com.brunocarvalhs.settings.commons.navigation.SettingsGraphRoute
import br.com.brunocarvalhs.settings.commons.navigation.SettingsScreenRoute
import javax.inject.Inject

class SettingsInitializerImpl @Inject constructor() : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.navigation<SettingsGraphRoute>(startDestination = SettingsScreenRoute) {
            composable<SettingsScreenRoute> {
                SettingsScreen(
                    viewModel = hiltViewModel(),
                    onBack = { navController.popBackStack() },
                    onAppearance = { navController.navigate(AppearanceScreenRoute) },
                    onReportIssue = { navController.navigate(ReportIssueScreenRoute) },
                    onFAQ = { navController.navigate(FAQScreenRoute) }
                )
            }
            composable<AppearanceScreenRoute> {
                val viewModel = hiltViewModel<AppearanceViewModel>()
                AppearanceScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<FAQScreenRoute> {
                FAQScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable<ReportIssueScreenRoute> {
                ReportIssueScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
package br.com.brunocarvalhs.settings.commons.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import br.com.brunocarvalhs.friendssecrets.common.navigation.SettingsGraphRoute
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleManager
import br.com.brunocarvalhs.settings.app.appearence.AppearanceScreen
import br.com.brunocarvalhs.settings.app.faq.FAQScreen
import br.com.brunocarvalhs.settings.app.list.SettingsScreen
import br.com.brunocarvalhs.settings.app.report.ReportIssueScreen
import kotlinx.serialization.Serializable

@Serializable
internal data object SettingsScreenRoute

@Serializable
internal data object AppearanceScreenRoute

@Serializable
internal data object FAQScreenRoute

@Serializable
internal data object ReportIssueScreenRoute

internal fun NavGraphBuilder.settingsGraph(navController: NavHostController, toggleManager: ToggleManager) {
    navigation<SettingsGraphRoute>(startDestination = SettingsScreenRoute) {
        composable<SettingsScreenRoute>() {
            SettingsScreen(
                navController = navController,
                toggleManager = toggleManager
            )
        }
        composable<AppearanceScreenRoute>() {
            AppearanceScreen(navController = navController)
        }
        composable<FAQScreenRoute>() {
            FAQScreen(navController = navController)
        }
        composable<ReportIssueScreenRoute>() {
            ReportIssueScreen(navController = navController)
        }
    }
}
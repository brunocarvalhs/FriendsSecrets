package br.com.brunocarvalhs.friendssecrets.presentation.views.settings

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.navigation.NavigationBase
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.appearence.AppearanceScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.faq.FAQScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.list.SettingsScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.report.ReportIssueScreen

sealed class SettingsNavigation(
    @StringRes override val title: Int,
    override val icon: ImageVector,
    override val route: String,
    override val arguments: List<NamedNavArgument> = emptyList(),
    override val deepLinks: List<NavDeepLink> = emptyList(),
) : NavigationBase, OptionsSettings {
    data object Settings : SettingsNavigation(
        title = R.string.title_settings,
        icon = Icons.Outlined.Settings,
        route = "list"
    )

    data object Appearance : SettingsNavigation(
        title = R.string.title_appearance,
        icon = Icons.Outlined.Palette,
        route = "appearance"
    )

    data object FAQ : SettingsNavigation(
        title = R.string.title_faq,
        icon = Icons.Outlined.Info,
        route = "faq"
    )

    data object ReportIssue : SettingsNavigation(
        title = R.string.title_report_an_issue,
        icon = Icons.Outlined.Report,
        route = "report_issue"
    )

    companion object {
        val START_DESTINATION = Settings.route
    }
}

fun NavGraphBuilder.settingsGraph(navController: NavHostController, route: String, toggleManager: ToggleManager) {
    navigation(startDestination = SettingsNavigation.START_DESTINATION, route = route) {
        composable(SettingsNavigation.Settings.route) {
            SettingsScreen(
                navController = navController,
                toggleManager = toggleManager
            )
        }
        composable(SettingsNavigation.Appearance.route) {
            AppearanceScreen(navController = navController)
        }
        composable(SettingsNavigation.FAQ.route) {
            FAQScreen(navController = navController)
        }
        composable(SettingsNavigation.ReportIssue.route) {
            ReportIssueScreen(navController = navController)
        }
    }
}
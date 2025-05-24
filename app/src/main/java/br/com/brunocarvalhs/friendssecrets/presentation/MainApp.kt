package br.com.brunocarvalhs.friendssecrets.presentation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.loginGraph
import br.com.brunocarvalhs.friendssecrets.presentation.views.generative.generativeGraph
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.groupGraph
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.homeGraph
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.settingsGraph

@Composable
fun MainApp(
    activity: ComponentActivity,
    navController: NavHostController,
    toggleManager: ToggleManager
) {

    NavHost(navController = navController, startDestination = Screen.START_DESTINATION) {
        loginGraph(
            activity = activity,
            navController = navController,
            route = Screen.Auth.route,
            toggleManager = toggleManager
        )
        homeGraph(
            navController = navController,
            route = Screen.Home.route,
            toggleManager = toggleManager
        )
        groupGraph(
            navController = navController,
            toggleManager = toggleManager
        )
        generativeGraph(
            navController = navController,
            route = Screen.Generative.route,
            toggleManager = toggleManager
        )
        settingsGraph(
            navController = navController,
            route = Screen.Settings.route,
            toggleManager = toggleManager
        )
    }
}

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Home : Screen("home")
    data object Group : Screen("group")
    data object Generative : Screen("generative")
    data object Settings : Screen("settings")

    companion object {
        val START_DESTINATION = if (SessionManager.getInstance().isUserLoggedIn()) Home.route
        else Auth.route
    }
}
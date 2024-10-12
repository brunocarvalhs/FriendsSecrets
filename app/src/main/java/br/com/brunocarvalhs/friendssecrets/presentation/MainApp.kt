package br.com.brunocarvalhs.friendssecrets.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.friendssecrets.presentation.views.generative.generativeGraph
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.groupGraph
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.homeGraph

@Composable
fun MainApp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.START_DESTINATION) {
        homeGraph(navController = navController, route = Screen.Home.route)
        groupGraph(navController = navController, route = Screen.Group.route)
        generativeGraph(navController = navController, route = Screen.Generative.route)
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Group : Screen("group")
    data object Generative : Screen("generative")

    companion object {
        val START_DESTINATION = Home.route
    }
}
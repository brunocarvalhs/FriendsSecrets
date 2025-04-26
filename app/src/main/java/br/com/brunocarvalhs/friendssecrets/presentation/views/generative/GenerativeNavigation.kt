package br.com.brunocarvalhs.friendssecrets.presentation.views.generative

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import br.com.brunocarvalhs.friendssecrets.commons.navigation.NavigationBase
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.presentation.views.generative.chat.ChatGenerativeScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.generative.chat.ChatGenerativeViewModel

sealed class GenerativeNavigation(
    override val route: String,
    override val arguments: List<NamedNavArgument> = emptyList(),
    override val deepLinks: List<NavDeepLink> = emptyList(),
) : NavigationBase {

    data object Chat : GenerativeNavigation(
        route = "chat?data={data}",
        arguments = listOf(navArgument(name = "data") {
            type = NavType.StringType
            defaultValue = ""
            nullable = true
        })
    ) {
        fun createRoute(question: String) = "chat?data=$question"
    }

    companion object {
        val START_DESTINATION = Chat.route
    }
}

fun NavGraphBuilder.generativeGraph(
    navController: NavHostController,
    route: String,
    toggleManager: ToggleManager,
) {
    navigation(startDestination = GenerativeNavigation.START_DESTINATION, route = route) {
        composable(GenerativeNavigation.Chat.route) {
            val data = it.arguments?.getString("data")
            val viewModel: ChatGenerativeViewModel = hiltViewModel()
            ChatGenerativeScreen(
                data = data,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
package br.com.brunocarvalhs.friendssecrets.presentation.views.home

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import br.com.brunocarvalhs.friendssecrets.commons.navigation.NavigationBase
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.HomeScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.HomeViewModel

sealed class HomeNavigation(
    override val route: String,
    override val arguments: List<NamedNavArgument> = emptyList(),
    override val deepLinks: List<NavDeepLink> = emptyList(),
) : NavigationBase {

    data object Home : HomeNavigation("list")

    companion object {
        val START_DESTINATION = Home.route
    }
}

fun NavGraphBuilder.homeGraph(navController: NavHostController, route: String) {
    navigation(startDestination = HomeNavigation.START_DESTINATION, route = route) {
        composable(HomeNavigation.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }
    }
}
package br.com.brunocarvalhs.home.commons.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import br.com.brunocarvalhs.friendssecrets.commons.navigation.NavigationBase
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.commons.security.BiometricManager
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.biometric.BiometricScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.HomeScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.list.HomeViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard.OnboardViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard.OnboardingScreen

sealed class HomeNavigation(
    override val route: String,
    override val arguments: List<NamedNavArgument> = emptyList(),
    override val deepLinks: List<NavDeepLink> = emptyList(),
) : NavigationBase {

    data object Home : HomeNavigation("list")
    data object Onboarding : HomeNavigation("onboarding}")
    data object Biometric : HomeNavigation("biometric")

    companion object {
        val START_DESTINATION =
            if (BiometricManager.isBiometricPromptEnabled()) Biometric.route else Home.route
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    route: String,
    toggleManager: ToggleManager,
) {
    navigation(startDestination = HomeNavigation.START_DESTINATION, route = route) {
        composable(HomeNavigation.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel,
                toggleManager = toggleManager
            )
        }
    }
}
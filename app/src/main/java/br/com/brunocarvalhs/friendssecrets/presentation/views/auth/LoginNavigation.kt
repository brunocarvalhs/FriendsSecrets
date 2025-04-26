package br.com.brunocarvalhs.friendssecrets.presentation.views.auth

import androidx.activity.ComponentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.commons.navigation.NavigationBase
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.login.LoginScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.login.LoginViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.multLogin.MultiLoginViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend.PhoneSendScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend.PhoneSendViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneVerify.PhoneVerifyScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneVerify.PhoneVerifyViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile.ProfileScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile.ProfileViewModel

sealed class LoginNavigation(
    override val route: String,
    override val arguments: List<NamedNavArgument> = emptyList(),
    override val deepLinks: List<NavDeepLink> = emptyList(),
) : NavigationBase {

    data object Login : LoginNavigation("login")
    data object PhoneSend : LoginNavigation("phone_send")

    data object PhoneVerification : LoginNavigation(
        route = "phone_verification?phoneNumber={phoneNumber}",
        arguments = listOf(navArgument("phoneNumber") { defaultValue = "" })
    ) {
        const val phoneNumber = "phoneNumber"

        fun createRoute(phoneNumber: String) = "phone_verification?phoneNumber=$phoneNumber"
    }

    data object Profile : LoginNavigation("profile")

    companion object {
        val START_DESTINATION = Login.route
    }
}

fun NavGraphBuilder.loginGraph(
    activity: ComponentActivity,
    navController: NavHostController,
    route: String,
    toggleManager: ToggleManager
) {
    navigation(startDestination = LoginNavigation.START_DESTINATION, route = route) {
        composable(LoginNavigation.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            val multiLoginViewModel: MultiLoginViewModel = hiltViewModel()
            LoginScreen(
                navController = navController,
                viewModel = viewModel,
                multiLoginViewModel = multiLoginViewModel
            )
        }
        composable(LoginNavigation.PhoneSend.route) {
            val viewModel: PhoneSendViewModel = hiltViewModel()
            PhoneSendScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(LoginNavigation.PhoneVerification.route) {
            val viewModel: PhoneVerifyViewModel = hiltViewModel()
            PhoneVerifyScreen(
                phoneNumber = it.arguments?.getString(LoginNavigation.PhoneVerification.phoneNumber)
                    .orEmpty(),
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(LoginNavigation.Profile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
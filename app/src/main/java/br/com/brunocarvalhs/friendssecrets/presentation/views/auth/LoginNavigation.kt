package br.com.brunocarvalhs.friendssecrets.presentation.views.auth

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
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
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend.PhoneSendScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend.PhoneSendViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneVerify.PhoneVerifyScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneVerify.PhoneVerifyViewModel

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

    companion object {
        val START_DESTINATION = Login.route
    }
}

@SuppressLint("ContextCastToActivity")
fun NavGraphBuilder.loginGraph(
    activity: ComponentActivity,
    navController: NavHostController,
    route: String,
    toggleManager: ToggleManager
) {

    navigation(startDestination = LoginNavigation.START_DESTINATION, route = route) {
        composable(LoginNavigation.Login.route) {
            val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(LoginNavigation.PhoneSend.route) {
            val viewModel: PhoneSendViewModel = viewModel(factory = PhoneSendViewModel.Factory(activity))
            PhoneSendScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(LoginNavigation.PhoneVerification.route) {
            val viewModel: PhoneVerifyViewModel = viewModel(factory = PhoneVerifyViewModel.Factory)
            PhoneVerifyScreen(
                phoneNumber = it.arguments?.getString(LoginNavigation.PhoneVerification.phoneNumber)
                    ?: "",
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
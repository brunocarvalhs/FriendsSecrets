package br.com.brunocarvalhs.friendssecrets.presentation.views.auth

import androidx.activity.ComponentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.login.LoginScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.login.LoginViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend.PhoneSendScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneSend.PhoneSendViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneVerify.PhoneVerifyScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.phoneVerify.PhoneVerifyViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile.ProfileScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.auth.profile.ProfileViewModel
import kotlinx.serialization.Serializable

@Serializable
object LoginGraphRoute

@Serializable
object LoginScreenRoute

@Serializable
object PhoneSendScreenRoute

@Serializable
data class PhoneVerificationScreenRoute(val phoneNumber: String)

@Serializable
object ProfileScreenRoute

fun NavGraphBuilder.loginGraph(
    activity: ComponentActivity,
    navController: NavHostController,
    route: String,
    toggleManager: ToggleManager
) {
    navigation<LoginGraphRoute>(startDestination = LoginScreenRoute) {
        composable<LoginScreenRoute> {
            val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<PhoneSendScreenRoute> {
            val viewModel: PhoneSendViewModel =
                viewModel(factory = PhoneSendViewModel.Factory(activity))
            PhoneSendScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<PhoneVerificationScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<PhoneVerificationScreenRoute>()
            val viewModel: PhoneVerifyViewModel = viewModel(factory = PhoneVerifyViewModel.Factory)
            PhoneVerifyScreen(
                phoneNumber = args.phoneNumber,
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<ProfileScreenRoute> {
            val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
            ProfileScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
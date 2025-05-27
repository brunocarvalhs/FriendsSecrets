package br.com.brunocarvalhs.auth.commons.navigation

import androidx.activity.ComponentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.brunocarvalhs.auth.app.biometric.BiometricScreen
import br.com.brunocarvalhs.auth.app.login.LoginScreen
import br.com.brunocarvalhs.auth.app.login.LoginViewModel
import br.com.brunocarvalhs.auth.app.phoneSend.PhoneSendScreen
import br.com.brunocarvalhs.auth.app.phoneSend.PhoneSendViewModel
import br.com.brunocarvalhs.auth.app.phoneVerify.PhoneVerifyScreen
import br.com.brunocarvalhs.auth.app.phoneVerify.PhoneVerifyViewModel
import br.com.brunocarvalhs.auth.app.profile.ProfileScreen
import br.com.brunocarvalhs.auth.app.profile.ProfileViewModel
import br.com.brunocarvalhs.friendssecrets.common.navigation.AuthGraphRoute
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard.OnboardViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard.OnboardingScreen
import kotlinx.serialization.Serializable

@Serializable
internal data object LoginScreenRoute

@Serializable
internal data object PhoneSendScreenRoute

@Serializable
internal data class PhoneVerificationScreenRoute(val phoneNumber: String)

@Serializable
internal data object ProfileScreenRoute

@Serializable
internal data object OnboardingScreenRoute

@Serializable
internal data object BiometricScreenRoute

internal fun NavGraphBuilder.loginGraph(
    activity: ComponentActivity,
    navController: NavHostController,
) {
    navigation<AuthGraphRoute>(startDestination = LoginScreenRoute) {
        composable<LoginScreenRoute> {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<PhoneSendScreenRoute> {
            val viewModel: PhoneSendViewModel = hiltViewModel()
            PhoneSendScreen(
                activity = activity,
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<PhoneVerificationScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<PhoneVerificationScreenRoute>()
            val viewModel: PhoneVerifyViewModel = hiltViewModel()
            PhoneVerifyScreen(
                phoneNumber = args.phoneNumber,
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<ProfileScreenRoute> {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<OnboardingScreenRoute> {
            val onboardingViewModel: OnboardViewModel = hiltViewModel()
            OnboardingScreen(
                navController = navController,
                viewModel = onboardingViewModel
            )
        }
        composable<BiometricScreenRoute> {
            BiometricScreen(navController = navController)
        }
    }
}
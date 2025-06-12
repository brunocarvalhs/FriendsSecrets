package br.com.brunocarvalhs.auth.commons.navigation

import androidx.activity.ComponentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import br.com.brunocarvalhs.auth.app.biometric.BiometricScreen
import br.com.brunocarvalhs.auth.app.create_profile.CreateProfileScreen
import br.com.brunocarvalhs.auth.app.login.LoginScreen
import br.com.brunocarvalhs.auth.app.onboard.OnboardingScreen
import br.com.brunocarvalhs.auth.app.phoneSend.PhoneSendScreen
import br.com.brunocarvalhs.auth.app.phoneVerify.PhoneVerifyScreen
import br.com.brunocarvalhs.auth.app.profiler.UserProfileScreen
import br.com.brunocarvalhs.friendssecrets.common.navigation.AuthGraphRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.ProfileGraphRoute
import kotlinx.serialization.Serializable

@Serializable
internal data object LoginScreenRoute

@Serializable
internal data object PhoneSendScreenRoute

@Serializable
internal data class PhoneVerificationScreenRoute(
    val phoneNumber: String,
    val countryCode: String
)

@Serializable
internal data object OnboardingScreenRoute

@Serializable
internal data object BiometricScreenRoute

@Serializable
internal data object CreateProfileScreenRoute

internal fun NavGraphBuilder.loginGraph(
    activity: ComponentActivity,
    navController: NavHostController,
) {
    navigation<AuthGraphRoute>(startDestination = LoginScreenRoute) {
        composable<LoginScreenRoute> {
            LoginScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable<PhoneSendScreenRoute> {
            PhoneSendScreen(
                activity = activity,
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable<PhoneVerificationScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<PhoneVerificationScreenRoute>()
            PhoneVerifyScreen(
                activity = activity,
                phoneNumber = args.phoneNumber,
                countryCode = args.countryCode,
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable<CreateProfileScreenRoute> {
            CreateProfileScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable<ProfileGraphRoute> {
            UserProfileScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable<OnboardingScreenRoute> {
            OnboardingScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable<BiometricScreenRoute> {
            BiometricScreen(navController = navController)
        }
    }
}
package br.com.brunocarvalhs.auth.commons.navigation

import androidx.activity.ComponentActivity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.common.navigation.AuthGraphRoute
import kotlinx.serialization.Serializable

@Serializable
internal data object LoginScreenRoute

@Serializable
internal data object PhoneSendScreenRoute

@Serializable
internal data class PhoneVerificationScreenRoute(val phoneNumber: String)

@Serializable
internal data object ProfileScreenRoute

internal fun NavGraphBuilder.loginGraph(
    activity: ComponentActivity,
    navController: NavHostController,
) {
    navigation<AuthGraphRoute>(startDestination = LoginScreenRoute) {
        composable<LoginScreenRoute> {

        }
        composable<PhoneSendScreenRoute> {

        }
        composable<PhoneVerificationScreenRoute> { backStackEntry ->

        }
        composable<ProfileScreenRoute> {

        }
    }
}
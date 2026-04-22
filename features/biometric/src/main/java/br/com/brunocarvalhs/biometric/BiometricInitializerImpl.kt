package br.com.brunocarvalhs.biometric

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.biometric.app.presentation.BiometricScreen
import br.com.brunocarvalhs.biometric.app.presentation.BiometricViewModel
import br.com.brunocarvalhs.biometric.commons.navigation.BiometricGraphRouter
import br.com.brunocarvalhs.biometric.commons.navigation.BiometricRouter
import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import javax.inject.Inject

class BiometricInitializerImpl @Inject constructor(
    private val commonNavigator: CommonNavigator
) : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.navigation<BiometricGraphRouter>(startDestination = BiometricRouter) {
            composable<BiometricRouter> {
                val viewModel = hiltViewModel<BiometricViewModel>()
                BiometricScreen(
                    viewModel = viewModel,
                    onSuccess = {
                        commonNavigator.navigateToGroupList(
                            navController = navController,
                            popUpTo = BiometricGraphRouter::class,
                            inclusive = true
                        )
                    }
                )
            }
        }
    }
}

package br.com.brunocarvalhs.biometric

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.biometric.app.presentation.BiometricScreen
import br.com.brunocarvalhs.biometric.app.presentation.BiometricViewModel
import br.com.brunocarvalhs.biometric.commons.navigation.BiometricRouter
import br.com.brunocarvalhs.core.navigation.routers.BiometricGraph
import kotlin.properties.Delegates

class BiometricInitializer(private val builder: Builder) {

    fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation<BiometricGraph>(startDestination = BiometricRouter) {
            composable<BiometricRouter> {
                val viewModel = hiltViewModel<BiometricViewModel>()
                val state by viewModel.state.collectAsState()

                BiometricScreen(
                    state = state,
                    handleIntent = viewModel::handleIntent,
                    onSuccess = builder.onSuccess
                )
            }
        }
    }

    class Builder {
        internal var onSuccess: () -> Unit = {}
        internal var navController: NavHostController by Delegates.notNull()

        fun navController(navController: NavHostController) = apply { this.navController = navController }
        fun onSuccess(onSuccess: () -> Unit) = apply { this.onSuccess = onSuccess }

        fun build(navGraphBuilder: NavGraphBuilder): BiometricInitializer =
            BiometricInitializer(this).also { it.build(navGraphBuilder) }
    }
}

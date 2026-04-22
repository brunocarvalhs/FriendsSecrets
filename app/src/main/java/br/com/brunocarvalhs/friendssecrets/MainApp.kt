package br.com.brunocarvalhs.friendssecrets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.biometric.commons.navigation.BiometricGraphRouter
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.list.commons.navigation.GroupListRouter

@Composable
fun NavHostController.MainApp(
    isBiometric: Boolean = false,
    initializers: Set<FeatureInitializer> = emptySet()
) {
    val startRoute: Any = remember(isBiometric) {
        if (isBiometric) {
            BiometricGraphRouter
        } else {
            GroupListRouter
        }
    }

    NavHost(
        navController = this@MainApp,
        startDestination = startRoute
    ) {
        initializers.forEach {
            it.register(this, this@MainApp)
        }
    }
}

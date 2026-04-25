package br.com.brunocarvalhs.friendssecrets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.BiometricGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.GroupListGraph

@Composable
fun NavHostController.mainApp(
    isBiometric: Boolean = false,
    initializers: Set<FeatureInitializer> = emptySet()
) {
    val startRoute: Any = remember(isBiometric) {
        if (isBiometric) {
            BiometricGraph
        } else {
            GroupListGraph
        }
    }

    NavHost(
        navController = this@mainApp,
        startDestination = startRoute
    ) {
        initializers.forEach {
            it.register(this, this@mainApp)
        }
    }
}

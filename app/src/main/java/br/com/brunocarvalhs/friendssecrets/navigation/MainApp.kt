package br.com.brunocarvalhs.friendssecrets.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.auth.AuthInitializer
import br.com.brunocarvalhs.friendssecrets.common.navigation.AuthGraphRoute

@Composable
fun MainApp(
    activity: ComponentActivity,
    navController: NavHostController,
) {

    NavHost(navController = navController, startDestination = AuthGraphRoute) {
        AuthInitializer.Builder()
            .activity(activity = activity)
            .navController(navController = navController)
            .build(navGraphBuilder = this)
    }
}
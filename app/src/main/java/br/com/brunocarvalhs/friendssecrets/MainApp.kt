package br.com.brunocarvalhs.friendssecrets

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.auth.AuthInitializer
import br.com.brunocarvalhs.friendssecrets.common.navigation.AuthGraphRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.GroupGraphRoute
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService
import br.com.brunocarvalhs.group.GroupInitializer
import kotlinx.coroutines.delay

@Composable
fun MainAppWithSplash(
    navController: NavHostController,
    activity: ComponentActivity,
    splashScreen: SplashScreen,
    session: SessionService<UserEntities>
) {
    var isLoading by remember { mutableStateOf(true) }

    splashScreen.setKeepOnScreenCondition { isLoading }

    MainApp(activity = activity, navController = navController)

    LaunchedEffect(Unit) {
        val loggedIn = session.isUserLoggedIn()

        if (loggedIn) {
            navController.navigate(GroupGraphRoute) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        } else {
            navController.navigate(AuthGraphRoute) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }

        isLoading = false
    }
}

@Composable
private fun MainApp(
    activity: ComponentActivity,
    navController: NavHostController,
) {

    NavHost(navController = navController, startDestination = AuthGraphRoute) {
        AuthInitializer.Builder()
            .activity(activity = activity)
            .navController(navController = navController)
            .build(navGraphBuilder = this)

        GroupInitializer.Builder()
            .navController(navController = navController)
            .build(navGraphBuilder = this)
    }
}
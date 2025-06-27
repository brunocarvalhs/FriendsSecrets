package br.com.brunocarvalhs.friendssecrets

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.auth.AuthInitializer
import br.com.brunocarvalhs.friendssecrets.common.navigation.AuthGraphRoute
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleManager
import br.com.brunocarvalhs.group.GroupInitializer
import br.com.brunocarvalhs.settings.SettingsInitializer

@Composable
fun MainApp(
    activity: ComponentActivity,
    toggleManager: ToggleManager,
    navController: NavHostController,
) {

    NavHost(navController = navController, startDestination = AuthGraphRoute) {
        AuthInitializer.Builder()
            .activity(activity = activity)
            .toggleManager(toggleManager = toggleManager)
            .navController(navController = navController)
            .build(navGraphBuilder = this)

        GroupInitializer.Builder()
            .navController(navController = navController)
            .toggleManager(toggleManager = toggleManager)
            .build(navGraphBuilder = this)

        SettingsInitializer.Builder()
            .navController(navController = navController)
            .toggleManager(toggleManager = toggleManager)
            .build(navGraphBuilder = this)
    }
}
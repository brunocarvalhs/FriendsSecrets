package br.com.brunocarvalhs.friendssecrets

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.friendssecrets.common.navigation.GroupGraphRoute
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleManager
import br.com.brunocarvalhs.group.GroupInitializer
import br.com.brunocarvalhs.settings.SettingsInitializer

@Composable
fun MainApp(
    toggleManager: ToggleManager,
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = GroupGraphRoute) {
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
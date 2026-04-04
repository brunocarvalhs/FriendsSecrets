package br.com.brunocarvalhs.friendssecrets

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.friendssecrets.commons.navigation.GroupGraphRoute
import br.com.brunocarvalhs.group.create.GroupCreateInitializer

@Composable
fun NavHostController.MainApp() {
    NavHost(navController = this@MainApp, startDestination = GroupGraphRoute) {
        GroupCreateInitializer.Builder()
            .navController(navController = this@MainApp)
            .build(navGraphBuilder = this)
    }
}
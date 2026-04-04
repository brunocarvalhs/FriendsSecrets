package br.com.brunocarvalhs.friendssecrets

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.group.create.GroupCreateInitializer
import br.com.brunocarvalhs.group.create.commons.navigation.GroupCreateRouter

@Composable
fun NavHostController.MainApp() {
    NavHost(navController = this@MainApp, startDestination = GroupCreateRouter) {
        GroupCreateInitializer.Builder()
            .navController(navController = this@MainApp)
            .build(navGraphBuilder = this)
    }
}
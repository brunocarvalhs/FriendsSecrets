package br.com.brunocarvalhs.friendssecrets

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.group.create.GroupCreateInitializer
import br.com.brunocarvalhs.group.create.commons.navigation.GroupCreateRouter
import br.com.brunocarvalhs.group.list.GroupListInitializer
import br.com.brunocarvalhs.group.list.commons.navigation.GroupListRouter

@Composable
fun NavHostController.MainApp() {
    NavHost(navController = this@MainApp, startDestination = GroupListRouter) {
        GroupCreateInitializer.Builder()
            .navController(navController = this@MainApp)
            .onFinish {
                this@MainApp.navigate(GroupListRouter)
            }
            .build(navGraphBuilder = this)

        GroupListInitializer.Builder()
            .navController(navController = this@MainApp)
            .onGroupToEnter {

            }
            .onGroupToCreate {
                this@MainApp.navigate(GroupCreateRouter)
            }
            .build(navGraphBuilder = this)
    }
}
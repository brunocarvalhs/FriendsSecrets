package br.com.brunocarvalhs.friendssecrets

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.group.create.GroupCreateInitializer
import br.com.brunocarvalhs.group.create.commons.navigation.GroupCreateRouter
import br.com.brunocarvalhs.group.details.GroupDetailsInitializer
import br.com.brunocarvalhs.group.details.commons.navigation.GroupDetailsRouter
import br.com.brunocarvalhs.group.draw.DrawInitializer
import br.com.brunocarvalhs.group.draw.commons.navigation.DrawGraphRouter
import br.com.brunocarvalhs.group.list.GroupListInitializer
import br.com.brunocarvalhs.group.list.commons.navigation.GroupListRouter

@Composable
fun NavHostController.MainApp() {
    NavHost(navController = this@MainApp, startDestination = GroupListRouter) {
        GroupCreateInitializer.Builder()
            .navController(navController = this@MainApp)
            .onFinish { this@MainApp.navigate(GroupListRouter) }
            .build(navGraphBuilder = this)

        GroupListInitializer.Builder()
            .navController(navController = this@MainApp)
            .onGroupToCreate { this@MainApp.navigate(GroupCreateRouter) }
            .onGroupToDetails { this@MainApp.navigate(GroupDetailsRouter(it)) }
            .build(navGraphBuilder = this)

        GroupDetailsInitializer.Builder()
            .navController(navController = this@MainApp)
            .onBack { this@MainApp.popBackStack() }
            .onDraw { this@MainApp.navigate(DrawGraphRouter(it)) }
            .build(navGraphBuilder = this)

        DrawInitializer.Builder()
            .navController(navController = this@MainApp)
            .build(navGraphBuilder = this)
    }
}
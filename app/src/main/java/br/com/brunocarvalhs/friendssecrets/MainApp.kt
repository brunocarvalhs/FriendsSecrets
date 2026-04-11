package br.com.brunocarvalhs.friendssecrets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.brunocarvalhs.chat.ChatInitializer
import br.com.brunocarvalhs.chat.commons.navigation.ChatGraphRouter
import br.com.brunocarvalhs.group.create.GroupCreateInitializer
import br.com.brunocarvalhs.group.create.commons.navigation.GroupCreateRouter
import br.com.brunocarvalhs.group.details.GroupDetailsInitializer
import br.com.brunocarvalhs.group.details.commons.navigation.GroupDetailsRouter
import br.com.brunocarvalhs.group.draw.DrawInitializer
import br.com.brunocarvalhs.group.draw.commons.navigation.DrawGraphRouter
import br.com.brunocarvalhs.group.list.GroupListInitializer
import br.com.brunocarvalhs.group.list.commons.navigation.GroupListRouter
import br.com.brunocarvalhs.group.list.commons.options.OptionsMore
import br.com.brunocarvalhs.settings.SettingsInitializer
import br.com.brunocarvalhs.settings.commons.navigation.SettingsGraphRoute

@Composable
fun NavHostController.MainApp(
    isBiometric: Boolean = false,
) {
    val route = remember {
        if (isBiometric) {
            GroupListRouter
        } else {
            GroupListRouter
        }
    }

    NavHost(navController = this@MainApp, startDestination = route) {

        GroupCreateInitializer.Builder()
            .navController(navController = this@MainApp)
            .onFinish { this@MainApp.navigate(GroupListRouter) }
            .build(navGraphBuilder = this)

        GroupListInitializer.Builder()
            .navController(navController = this@MainApp)
            .onGroupToCreate { this@MainApp.navigate(GroupCreateRouter) }
            .onGroupToDetails { this@MainApp.navigate(GroupDetailsRouter(it)) }
            .setMoreOptions(
                options = listOf(
                OptionsMore(
                    lambda = { this@MainApp.navigate(SettingsGraphRoute) },
                    icon = Icons.Default.Settings,
                    contentDescription = {
                        stringResource(br.com.brunocarvalhs.settings.R.string.title_settings)
                    },
                    name = {
                        stringResource(br.com.brunocarvalhs.settings.R.string.title_settings)
                    }
                ),
            ))
            .build(navGraphBuilder = this)

        GroupDetailsInitializer.Builder()
            .navController(navController = this@MainApp)
            .onBack { this@MainApp.popBackStack() }
            .onDraw { this@MainApp.navigate(DrawGraphRouter(it)) }
            .onChat { this@MainApp.navigate(ChatGraphRouter) }
            .build(navGraphBuilder = this)

        DrawInitializer.Builder()
            .navController(navController = this@MainApp)
            .onBack { this@MainApp.popBackStack() }
            .build(navGraphBuilder = this)

        SettingsInitializer.Builder()
            .navController(navController = this@MainApp)
            .onBack { this@MainApp.popBackStack() }
            .build(navGraphBuilder = this)

        ChatInitializer.Builder()
            .navController(navController = this@MainApp)
            .onBack { this@MainApp.popBackStack() }
            .build(navGraphBuilder = this)
    }
}
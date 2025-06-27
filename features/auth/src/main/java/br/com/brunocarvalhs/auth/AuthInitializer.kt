package br.com.brunocarvalhs.auth

import androidx.activity.ComponentActivity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.auth.commons.navigation.loginGraph
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleManager
import kotlin.properties.Delegates

class AuthInitializer(private val builder: Builder) {

    private fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.loginGraph(
            activity = builder.activity,
            navController = builder.navController
        )
    }

    class Builder {
        internal var activity: ComponentActivity by Delegates.notNull()
        internal var toggleManager: ToggleManager by Delegates.notNull()
        internal var navController: NavHostController by Delegates.notNull()

        fun activity(activity: ComponentActivity) =
            apply { this.activity = activity }

        fun navController(navController: NavHostController) =
            apply { this.navController = navController }

        fun toggleManager(toggleManager: ToggleManager) =
            apply { this.toggleManager = toggleManager }

        fun build(navGraphBuilder: NavGraphBuilder): AuthInitializer =
            AuthInitializer(this).also { it.build(navGraphBuilder) }
    }
}
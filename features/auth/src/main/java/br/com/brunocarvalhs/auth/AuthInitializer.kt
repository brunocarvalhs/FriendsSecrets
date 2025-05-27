package br.com.brunocarvalhs.auth

import androidx.activity.ComponentActivity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.auth.commons.navigation.loginGraph
import kotlin.properties.Delegates

class AuthInitializer(private val builder: Builder) {

    private fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.loginGraph(builder.activity, builder.navController)
    }

    class Builder {
        internal var activity: ComponentActivity by Delegates.notNull()
        internal var navController: NavHostController by Delegates.notNull()

        fun activity(activity: ComponentActivity) =
            apply { this.activity = activity }

        fun navController(navController: NavHostController) =
            apply { this.navController = navController }

        fun build(navGraphBuilder: NavGraphBuilder): AuthInitializer =
            AuthInitializer(this).also { it.build(navGraphBuilder) }
    }
}
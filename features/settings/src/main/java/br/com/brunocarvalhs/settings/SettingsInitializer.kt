package br.com.brunocarvalhs.settings

import androidx.activity.ComponentActivity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import kotlin.properties.Delegates

class SettingsInitializer(private val builder: Builder) {

    private fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.settingsGraph(builder.activity, builder.navController)
    }

    class Builder {
        internal var activity: ComponentActivity by Delegates.notNull()
        internal var navController: NavHostController by Delegates.notNull()

        fun activity(activity: ComponentActivity) =
            apply { this.activity = activity }

        fun navController(navController: NavHostController) =
            apply { this.navController = navController }

        fun build(navGraphBuilder: NavGraphBuilder): SettingsInitializer =
            SettingsInitializer(this).also { it.build(navGraphBuilder) }
    }
}
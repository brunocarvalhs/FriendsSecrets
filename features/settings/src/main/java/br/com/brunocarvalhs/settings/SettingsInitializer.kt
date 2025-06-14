package br.com.brunocarvalhs.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleManager
import br.com.brunocarvalhs.settings.commons.navigation.settingsGraph
import kotlin.properties.Delegates

class SettingsInitializer(private val builder: Builder) {

    private fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.settingsGraph(
            navController = builder.navController,
            toggleManager = builder.toggleManager
        )
    }

    class Builder {
        internal var toggleManager: ToggleManager by Delegates.notNull()
        internal var navController: NavHostController by Delegates.notNull()

        fun navController(navController: NavHostController) =
            apply { this.navController = navController }

        fun toggleManager(toggleManager: ToggleManager) =
            apply { this.toggleManager = toggleManager }

        fun build(navGraphBuilder: NavGraphBuilder): SettingsInitializer =
            SettingsInitializer(this).also { it.build(navGraphBuilder) }
    }
}
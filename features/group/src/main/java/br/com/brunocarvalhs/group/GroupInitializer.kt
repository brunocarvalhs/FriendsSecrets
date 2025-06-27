package br.com.brunocarvalhs.group

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleManager
import br.com.brunocarvalhs.group.commons.navigation.groupGraph
import kotlin.properties.Delegates

class GroupInitializer(private val builder: Builder) {

    private fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.groupGraph(
            navController = builder.navController
        )
    }

    class Builder {
        internal var toggleManager: ToggleManager by Delegates.notNull()
        internal var navController: NavHostController by Delegates.notNull()

        fun navController(navController: NavHostController) =
            apply { this.navController = navController }

        fun toggleManager(toggleManager: ToggleManager) =
            apply { this.toggleManager = toggleManager }

        fun build(navGraphBuilder: NavGraphBuilder): GroupInitializer =
            GroupInitializer(this).also { it.build(navGraphBuilder) }
    }
}
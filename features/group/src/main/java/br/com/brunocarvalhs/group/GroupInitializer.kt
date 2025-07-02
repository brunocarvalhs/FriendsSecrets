package br.com.brunocarvalhs.group

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.common.remote.toggle.ToggleManager
import br.com.brunocarvalhs.group.commons.navigation.groupGraph
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class GroupInitializer(private val builder: Builder) {

    @AddTrace(name = "GroupInitializer.build", enabled = true)
    private fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.groupGraph(
            navController = builder.navController
        )
    }

    class Builder {
        internal var toggleManager: ToggleManager by Delegates.notNull()
        internal var navController: NavHostController by Delegates.notNull()

        @AddTrace(name = "GroupInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) =
            apply { this.navController = navController }

        @AddTrace(name = "GroupInitializer.Builder.toggleManager", enabled = true)
        fun toggleManager(toggleManager: ToggleManager) =
            apply { this.toggleManager = toggleManager }

        @AddTrace(name = "GroupInitializer.Builder.build", enabled = true)
        fun build(navGraphBuilder: NavGraphBuilder): GroupInitializer =
            GroupInitializer(this).also { it.build(navGraphBuilder) }
    }
}
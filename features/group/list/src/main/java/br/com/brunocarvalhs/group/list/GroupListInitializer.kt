package br.com.brunocarvalhs.group.list

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.group.list.app.presentation.GroupListScreen
import br.com.brunocarvalhs.group.list.app.presentation.GroupListViewModel
import br.com.brunocarvalhs.group.list.commons.navigation.GroupListRouter
import br.com.brunocarvalhs.group.list.commons.navigation.ListRouter
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class GroupListInitializer(private val builder: Builder) {

    @AddTrace(name = "GroupListInitializer.build", enabled = true)
    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<GroupListRouter>(startDestination = ListRouter) {
            composable<ListRouter>() {
                val viewModel = hiltViewModel<GroupListViewModel>()
                GroupListScreen(
                    viewModel = viewModel,
                    onGroupToEnter = { builder.onGroupToEnter(it) },
                    onGroupToCreate = { builder.onGroupToCreate() }
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onGroupToEnter: (String) -> Unit = {}
        internal var onGroupToCreate: () -> Unit = {}

        @AddTrace(name = "GroupListInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) = apply {
            this.navController = navController
        }

        @AddTrace(name = "GroupListInitializer.Builder.onGroupToEnter", enabled = true)
        fun onGroupToEnter(onGroupToEnter: (String) -> Unit) = apply {
            this.onGroupToEnter = onGroupToEnter
        }

        @AddTrace(name = "GroupListInitializer.Builder.onGroupToCreate", enabled = true)
        fun onGroupToCreate(onGroupToCreate: () -> Unit) = apply {
            this.onGroupToCreate = onGroupToCreate
        }

        @AddTrace(name = "GroupListInitializer.Builder.build", enabled = true)
        fun build(navGraphBuilder: NavGraphBuilder): GroupListInitializer =
            GroupListInitializer(this).also { it.build(navGraphBuilder) }
    }
}
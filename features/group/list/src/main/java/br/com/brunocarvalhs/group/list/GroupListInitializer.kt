package br.com.brunocarvalhs.group.list

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.group.list.app.presentation.details.GroupDetailsScreen
import br.com.brunocarvalhs.group.list.app.presentation.details.GroupDetailsViewModel
import br.com.brunocarvalhs.group.list.app.presentation.list.GroupListScreen
import br.com.brunocarvalhs.group.list.app.presentation.list.GroupListViewModel
import br.com.brunocarvalhs.group.list.commons.navigation.DetailRouter
import br.com.brunocarvalhs.group.list.commons.navigation.GroupListRouter
import br.com.brunocarvalhs.group.list.commons.navigation.ListRouter
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class GroupListInitializer(private val builder: Builder) {

    @AddTrace(name = "GroupListInitializer.build", enabled = true)
    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<GroupListRouter>(startDestination = ListRouter) {

            composable<ListRouter> {
                val viewModel = hiltViewModel<GroupListViewModel>()
                GroupListScreen(
                    viewModel = viewModel,
                    onGroupToEnter = {
                        builder.navController.navigate(route = DetailRouter(it))
                    },
                    onGroupToCreate = { builder.onGroupToCreate() }
                )
            }

            composable<DetailRouter>(typeMap = DetailRouter.typeMap) {
                val viewModel = hiltViewModel<GroupDetailsViewModel>()
                GroupDetailsScreen(
                    viewModel = viewModel,
                    onBack = { builder.navController.popBackStack() },
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onGroupToCreate: () -> Unit = {}

        @AddTrace(name = "GroupListInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) = apply {
            this.navController = navController
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
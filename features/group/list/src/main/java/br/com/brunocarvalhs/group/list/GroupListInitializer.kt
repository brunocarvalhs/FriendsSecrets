package br.com.brunocarvalhs.group.list

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.GroupListGraph
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.list.app.presentation.GroupListScreen
import br.com.brunocarvalhs.group.list.app.presentation.GroupListViewModel
import br.com.brunocarvalhs.group.list.commons.navigation.ListRouter
import br.com.brunocarvalhs.group.list.commons.options.OptionsMore
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class GroupListInitializer(private val builder: Builder) {

    @AddTrace(name = "GroupListInitializer.build", enabled = true)
    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<GroupListGraph>(startDestination = ListRouter) {
            composable<ListRouter> {
                val viewModel = hiltViewModel<GroupListViewModel>()
                GroupListScreen(
                    viewModel = viewModel,
                    onGroupToEnter = builder.onGroupToDetails,
                    onGroupToCreate = builder.onGroupToCreate,
                    moreOptions = builder.moreOptions
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onGroupToCreate: () -> Unit = {}
        internal var onGroupToDetails: (GroupModel) -> Unit = {}
        internal var moreOptions: List<OptionsMore> = emptyList()

        @AddTrace(name = "GroupListInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) = apply {
            this.navController = navController
        }

        @AddTrace(name = "GroupListInitializer.Builder.onGroupToCreate", enabled = true)
        fun onGroupToCreate(onGroupToCreate: () -> Unit) = apply {
            this.onGroupToCreate = onGroupToCreate
        }

        @AddTrace(name = "GroupListInitializer.Builder.onGroupToDetails", enabled = true)
        fun onGroupToDetails(onGroupToDetails: (GroupModel) -> Unit) = apply {
            this.onGroupToDetails = onGroupToDetails
        }

        @AddTrace(name = "GroupListInitializer.Builder.onMoreOptions", enabled = true)
        fun setMoreOptions(options: List<OptionsMore>) = apply {
            this.moreOptions = options
        }

        @AddTrace(name = "GroupListInitializer.Builder.build", enabled = true)
        fun build(navGraphBuilder: NavGraphBuilder): GroupListInitializer =
            GroupListInitializer(this).also { it.build(navGraphBuilder) }
    }
}
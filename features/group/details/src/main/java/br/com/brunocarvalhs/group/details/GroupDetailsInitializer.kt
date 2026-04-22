package br.com.brunocarvalhs.group.details

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.core.navigation.GroupDetailsGraph
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.presentation.GroupDetailsScreen
import br.com.brunocarvalhs.group.details.app.presentation.GroupDetailsViewModel
import br.com.brunocarvalhs.group.details.commons.navigation.DetailRouter
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class GroupDetailsInitializer(private val builder: Builder) {

    @AddTrace(name = "GroupDetailsInitializer.build", enabled = true)
    fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation<GroupDetailsGraph>(
            startDestination = DetailRouter::class,
            typeMap = GroupDetailsGraph.typeMap
        ) {
            composable<DetailRouter>(
                typeMap = DetailRouter.typeMap
            ) {
                val viewModel = hiltViewModel<GroupDetailsViewModel>()
                GroupDetailsScreen(
                    viewModel = viewModel,
                    onBack = builder.onBack,
                    onDraw = { builder.onDraw(viewModel.uiState.value.group) },
                    onChat = { builder.onChat(viewModel.uiState.value.group) },
                    onEdit = { builder.onEdit(viewModel.uiState.value.group) }
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onBack: () -> Unit = {}
        internal var onDraw: (GroupModel) -> Unit = {}
        internal var onChat: (GroupModel) -> Unit = {}
        internal var onEdit: (GroupModel) -> Unit = {}

        @AddTrace(name = "GroupListInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) = apply {
            this.navController = navController
        }

        @AddTrace(name = "GroupListInitializer.Builder.onBack", enabled = true)
        fun onBack(onBack: () -> Unit) = apply {
            this.onBack = onBack
        }

        @AddTrace(name = "GroupListInitializer.Builder.onDraw", enabled = true)
        fun onDraw(onDraw: (GroupModel) -> Unit) = apply {
            this.onDraw = onDraw
        }

        @AddTrace(name = "GroupListInitializer.Builder.onChat", enabled = true)
        fun onChat(onChat: (GroupModel) -> Unit) = apply {
            this.onChat = onChat
        }

        @AddTrace(name = "GroupListInitializer.Builder.onEdit", enabled = true)
        fun onEdit(onEdit: (GroupModel) -> Unit) = apply {
            this.onEdit = onEdit
        }

        @AddTrace(name = "GroupListInitializer.Builder.build", enabled = true)
        fun build(navGraphBuilder: NavGraphBuilder): GroupDetailsInitializer =
            GroupDetailsInitializer(this).also { it.build(navGraphBuilder) }
    }
}

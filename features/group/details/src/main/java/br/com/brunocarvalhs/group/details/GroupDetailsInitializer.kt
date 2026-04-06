package br.com.brunocarvalhs.group.details

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.group.details.commons.navigation.GroupDetailsRouter
import br.com.brunocarvalhs.group.details.commons.navigation.DetailRouter
import br.com.brunocarvalhs.group.list.app.presentation.details.GroupDetailsScreen
import br.com.brunocarvalhs.group.list.app.presentation.details.GroupDetailsViewModel
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class GroupDetailsInitializer(private val builder: Builder) {

    @AddTrace(name = "GroupDetailsInitializer.build", enabled = true)
    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<GroupDetailsRouter>(startDestination = DetailRouter::class, typeMap = DetailRouter.typeMap) {
            composable<DetailRouter> {
                val viewModel = hiltViewModel<GroupDetailsViewModel>()
                GroupDetailsScreen(
                    viewModel = viewModel,
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onBack: () -> Unit = {}
        internal var onDraw: () -> Unit = {}

        @AddTrace(name = "GroupListInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) = apply {
            this.navController = navController
        }

        @AddTrace(name = "GroupListInitializer.Builder.onBack", enabled = true)
        fun onBack(onBack: () -> Unit) = apply {
            this.onBack = onBack
        }

        @AddTrace(name = "GroupListInitializer.Builder.onDraw", enabled = true)
        fun onDraw(onDraw: () -> Unit) = apply {
            this.onDraw = onDraw
        }

        @AddTrace(name = "GroupListInitializer.Builder.build", enabled = true)
        fun build(navGraphBuilder: NavGraphBuilder): GroupDetailsInitializer =
            GroupDetailsInitializer(this).also { it.build(navGraphBuilder) }
    }
}
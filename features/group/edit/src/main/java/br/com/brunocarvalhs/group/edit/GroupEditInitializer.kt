package br.com.brunocarvalhs.group.edit

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.group.app.presentation.edit.GroupEditViewModel
import br.com.brunocarvalhs.group.edit.app.presentation.GroupEditScreen
import br.com.brunocarvalhs.group.edit.commons.navigation.GroupEditGraphRouter
import br.com.brunocarvalhs.group.edit.commons.navigation.GroupEditRouter

class GroupEditInitializer(private val builder: Builder) {

    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<GroupEditGraphRouter>(
            startDestination = GroupEditRouter,
        ) {
            composable<GroupEditRouter> {
                val viewModel = hiltViewModel<GroupEditViewModel>()
                GroupEditScreen(
                    viewModel = viewModel,
                    onBack = builder.onBack
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController? = null
        internal var onBack: () -> Unit = {}
        internal var onFinish: () -> Unit = {}

        fun navController(navController: NavHostController) =
            apply { this.navController = navController }

        fun onBack(onBack: () -> Unit) = apply { this.onBack = onBack }

        fun onFinish(onFinish: () -> Unit) = apply { this.onFinish = onFinish }

        fun build(navGraphBuilder: NavGraphBuilder): GroupEditInitializer =
            GroupEditInitializer(this).also { it.build(navGraphBuilder) }
    }
}
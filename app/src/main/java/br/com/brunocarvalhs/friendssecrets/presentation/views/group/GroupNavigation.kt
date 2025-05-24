package br.com.brunocarvalhs.friendssecrets.presentation.views.group

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation // Use navigation-compose import
import androidx.navigation.toRoute // Import for type-safe argument access
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.create.GroupCreateScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.create.GroupCreateViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw.DrawScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw.DrawViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.edit.GroupEditScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.edit.GroupEditViewModel
import kotlinx.serialization.Serializable


@Serializable
object GroupGraphRoute

@Serializable
object GroupCreateScreenRoute

@Serializable
data class GroupDetailsScreenRoute(val groupId: String)

@Serializable
data class GroupEditScreenRoute(val groupId: String)

@Serializable
data class GroupRevelationScreenRoute(val groupId: String, val code: String? = null)

fun NavGraphBuilder.groupGraph(
    navController: NavController,
    toggleManager: ToggleManager,
) {
    navigation<GroupGraphRoute>(
        startDestination = GroupCreateScreenRoute,
    ) {
        composable<GroupCreateScreenRoute> {
            val viewModel: GroupCreateViewModel = viewModel(factory = GroupCreateViewModel.Factory)
            GroupCreateScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }

        composable<GroupDetailsScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<GroupDetailsScreenRoute>()
            val viewModel: GroupDetailsViewModel =
                viewModel(factory = GroupDetailsViewModel.Factory)
            GroupDetailsScreen(
                navController = navController,
                viewModel = viewModel,
                groupId = args.groupId
            )
        }

        composable<GroupEditScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<GroupEditScreenRoute>()
            val viewModel: GroupEditViewModel = viewModel(factory = GroupEditViewModel.Factory)
            GroupEditScreen(
                navController = navController,
                viewModel = viewModel,
                groupId = args.groupId
            )
        }

        composable<GroupRevelationScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<GroupRevelationScreenRoute>()
            val viewModel: DrawViewModel = viewModel(factory = DrawViewModel.Factory)
            DrawScreen(
                navController = navController,
                viewModel = viewModel,
                groupId = args.groupId,
                code = args.code,
                toggleManager = toggleManager
            )
        }
    }
}
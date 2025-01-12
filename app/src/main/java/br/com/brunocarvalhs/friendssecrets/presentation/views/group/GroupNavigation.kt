package br.com.brunocarvalhs.friendssecrets.presentation.views.group

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import br.com.brunocarvalhs.friendssecrets.commons.navigation.NavigationBase
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.create.GroupCreateScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.create.GroupCreateViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw.DrawScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw.DrawViewModel
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.edit.GroupEditScreen
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.edit.GroupEditViewModel

sealed class GroupNavigation(
    override val route: String,
    override val arguments: List<NamedNavArgument> = emptyList(),
    override val deepLinks: List<NavDeepLink> = emptyList(),
) : NavigationBase {

    data object Create : GroupNavigation(
        route = "group/create",
    )

    data object Read : GroupNavigation(
        route = "group/{groupId}",
        arguments = listOf(navArgument(name = "groupId") { type = NavType.StringType })
    ) {
        fun createRoute(groupId: String) = "group/$groupId"
    }

    data object Edit : GroupNavigation(
        route = "group/{groupId}/edit",
        arguments = listOf(navArgument(name = "groupId") { type = NavType.StringType })
    ) {
        fun createRoute(groupId: String) = "group/$groupId/edit"
    }

    data object Revelation : GroupNavigation(
        route = "group/{groupId}/revelation",
        arguments = listOf(navArgument(name = "groupId") { type = NavType.StringType })
    ) {
        fun createRoute(groupId: String) = "group/$groupId/revelation"
    }

    companion object {
        val START_DESTINATION = Create.route
    }
}

fun NavGraphBuilder.groupGraph(
    navController: NavController,
    route: String,
    toggleManager: ToggleManager,
) {
    navigation(
        startDestination = GroupNavigation.START_DESTINATION,
        route = route
    ) {
        composable(
            route = GroupNavigation.Create.route,
            arguments = GroupNavigation.Create.arguments,
            deepLinks = GroupNavigation.Create.deepLinks
        ) {
            val viewModel: GroupCreateViewModel = viewModel(factory = GroupCreateViewModel.Factory)
            GroupCreateScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = GroupNavigation.Read.route,
            arguments = GroupNavigation.Read.arguments,
            deepLinks = GroupNavigation.Read.deepLinks
        ) {
            val groupId = it.arguments?.getString("groupId") ?: ""
            val viewModel: GroupDetailsViewModel =
                viewModel(factory = GroupDetailsViewModel.Factory)
            GroupDetailsScreen(
                navController = navController,
                viewModel = viewModel,
                groupId = groupId
            )
        }
        composable(
            route = GroupNavigation.Edit.route,
            arguments = GroupNavigation.Edit.arguments,
            deepLinks = GroupNavigation.Edit.deepLinks
        ) {
            val groupId = it.arguments?.getString("groupId") ?: ""
            val viewModel: GroupEditViewModel =
                viewModel(factory = GroupEditViewModel.Factory)
            GroupEditScreen(
                navController = navController,
                viewModel = viewModel,
                groupId = groupId
            )
        }
        composable(
            route = GroupNavigation.Revelation.route,
            arguments = GroupNavigation.Revelation.arguments,
            deepLinks = GroupNavigation.Revelation.deepLinks
        ) {
            val groupId = it.arguments?.getString("groupId") ?: ""
            val viewModel: DrawViewModel = viewModel(factory = DrawViewModel.Factory)
            DrawScreen(
                navController = navController,
                viewModel = viewModel,
                groupId = groupId,
                toggleManager = toggleManager
            )
        }
    }
}
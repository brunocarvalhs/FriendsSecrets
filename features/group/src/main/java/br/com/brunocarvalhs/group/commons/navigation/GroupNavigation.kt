package br.com.brunocarvalhs.group.commons.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import br.com.brunocarvalhs.friendssecrets.common.navigation.GroupGraphRoute
import br.com.brunocarvalhs.group.app.create.GroupCreateScreen
import br.com.brunocarvalhs.group.app.details.GroupDetailsScreen
import br.com.brunocarvalhs.group.app.draw.DrawScreen
import br.com.brunocarvalhs.group.app.edit.GroupEditScreen
import br.com.brunocarvalhs.group.app.list.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object GroupListScreenRoute

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
) {
    navigation<GroupGraphRoute>(
        startDestination = GroupListScreenRoute,
    ) {
        composable<GroupCreateScreenRoute> {
            GroupCreateScreen(
                navController = navController,
                viewModel = hiltViewModel(),
            )
        }

        composable<GroupDetailsScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<GroupDetailsScreenRoute>()
            GroupDetailsScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                groupId = args.groupId
            )
        }

        composable<GroupEditScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<GroupEditScreenRoute>()
            GroupEditScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                groupId = args.groupId
            )
        }

        composable<GroupRevelationScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<GroupRevelationScreenRoute>()
            DrawScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                groupId = args.groupId,
                code = args.code,
            )
        }

        composable<GroupListScreenRoute> {
            HomeScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
    }
}
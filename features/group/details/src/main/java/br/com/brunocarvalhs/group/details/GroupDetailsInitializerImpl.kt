package br.com.brunocarvalhs.group.details

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.details.app.presentation.GroupDetailsScreen
import br.com.brunocarvalhs.group.details.app.presentation.GroupDetailsViewModel
import br.com.brunocarvalhs.group.details.commons.navigation.DetailRouter
import br.com.brunocarvalhs.group.details.commons.navigation.GroupDetailsRouter
import javax.inject.Inject

class GroupDetailsInitializerImpl @Inject constructor(
    private val navigator: CommonNavigator
) : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.navigation<GroupDetailsRouter>(
            startDestination = DetailRouter::class,
            typeMap = GroupDetailsRouter.typeMap
        ) {
            composable<DetailRouter>(
                typeMap = DetailRouter.typeMap
            ) {
                val viewModel = hiltViewModel<GroupDetailsViewModel>()
                GroupDetailsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onDraw = { group ->
                        navigator.navigateToDraw(navController, group)
                    },
                    onChat = { group ->
                        navigator.navigateToChat(navController, group)
                    },
                    onEdit = { group ->
                        navigator.navigateToEditGroup(navController, group)
                    }
                )
            }
        }
    }
}

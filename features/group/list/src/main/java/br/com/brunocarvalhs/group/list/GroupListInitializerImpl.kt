package br.com.brunocarvalhs.group.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.list.app.presentation.GroupListScreen
import br.com.brunocarvalhs.group.list.app.presentation.GroupListViewModel
import br.com.brunocarvalhs.group.list.commons.navigation.GroupListRouter
import br.com.brunocarvalhs.group.list.commons.navigation.ListRouter
import br.com.brunocarvalhs.group.list.commons.options.OptionsMore
import javax.inject.Inject

class GroupListInitializerImpl @Inject constructor(
    private val navigator: CommonNavigator
) : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.navigation<GroupListRouter>(startDestination = ListRouter) {
            composable<ListRouter> {
                val viewModel = hiltViewModel<GroupListViewModel>()
                GroupListScreen(
                    viewModel = viewModel,
                    onGroupToEnter = { navigator.navigateToGroupDetails(navController, it) },
                    onGroupToCreate = { navigator.navigateToGroupCreate(navController) },
                    moreOptions = listOf(
                        OptionsMore(
                            lambda = { navigator.navigateToSettings(navController) },
                            icon = Icons.Default.Settings,
                            contentDescription = {
                                stringResource(R.string.title_settings)
                            },
                            name = {
                                stringResource(R.string.title_settings)
                            }
                        ),
                    )
                )
            }
        }
    }
}
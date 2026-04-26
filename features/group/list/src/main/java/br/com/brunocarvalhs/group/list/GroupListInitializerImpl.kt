package br.com.brunocarvalhs.group.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.core.navigation.CommonNavigator
import br.com.brunocarvalhs.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.group.list.commons.options.OptionsMore
import javax.inject.Inject

class GroupListInitializerImpl @Inject constructor(
    private val navigator: CommonNavigator
) : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        GroupListInitializer.Builder()
            .navController(navController)
            .onGroupToCreate { navigator.navigateToGroupCreate(navController) }
            .onGroupToDetails { navigator.navigateToGroupDetails(navController, it) }
            .setMoreOptions(
                listOf(
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
            ))
            .build(navGraphBuilder)
    }
}

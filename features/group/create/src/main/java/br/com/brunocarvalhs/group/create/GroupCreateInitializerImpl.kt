package br.com.brunocarvalhs.group.create

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.GroupCreateGraph
import javax.inject.Inject

class GroupCreateInitializerImpl @Inject constructor(
    private val navigator: CommonNavigator
) : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        GroupCreateInitializer.Builder()
            .navController(navController)
            .onFinish {
                navigator.navigateToGroupList(
                    navController = navController,
                    popUpTo = GroupCreateGraph::class,
                    inclusive = true
                )
            }
            .build(navGraphBuilder)
    }
}

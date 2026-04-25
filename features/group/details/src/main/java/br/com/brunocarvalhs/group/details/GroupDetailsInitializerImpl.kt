package br.com.brunocarvalhs.group.details

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
import javax.inject.Inject

class GroupDetailsInitializerImpl @Inject constructor(
    private val navigator: CommonNavigator
) : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        GroupDetailsInitializer.Builder()
            .navController(navController)
            .onBack { navController.popBackStack() }
            .onDraw { group ->
                navigator.navigateToDraw(navController, group)
            }
            .onChat { group ->
                navigator.navigateToChat(navController, group)
            }
            .onEdit { group ->
                navigator.navigateToEditGroup(navController, group)
            }
            .onAddMembers { group ->
                navigator.navigateToContacts(navController, group)
            }
            .build(navGraphBuilder)
    }
}

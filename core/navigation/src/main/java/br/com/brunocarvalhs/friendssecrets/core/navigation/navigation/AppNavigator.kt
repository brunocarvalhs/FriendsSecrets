package br.com.brunocarvalhs.friendssecrets.core.navigation.navigation

import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.ChatGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.ContactsRouter
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.DrawGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.EditFormsGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.GroupCreateGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.GroupDetailsGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.GroupListGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.SettingsGraph
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class AppNavigator @Inject constructor() : CommonNavigator {

    override fun navigateToGroupDetails(navController: NavHostController, group: GroupModel) {
        navController.navigate(GroupDetailsGraph(group))
    }

    override fun navigateToGroupCreate(navController: NavHostController) {
        navController.navigate(GroupCreateGraph)
    }

    override fun navigateToSettings(navController: NavHostController) {
        navController.navigate(SettingsGraph) {
            launchSingleTop = true
        }
    }

    override fun navigateToGroupList(navController: NavHostController, popUpTo: KClass<*>?, inclusive: Boolean) {
        navController.navigate(GroupListGraph) {
            popUpTo?.let {
                popUpTo(it) {
                    this.inclusive = inclusive
                }
            }
            launchSingleTop = true
        }
    }

    override fun navigateToDraw(navController: NavHostController, group: GroupModel) {
        navController.navigate(DrawGraph(group)) {
            launchSingleTop = true
        }
    }

    override fun navigateToChat(navController: NavHostController, group: GroupModel) {
        navController.navigate(ChatGraph(group)) {
            launchSingleTop = true
        }
    }

    override fun navigateToEditGroup(navController: NavHostController, group: GroupModel) {
        navController.navigate(EditFormsGraph(group)) {
            launchSingleTop = true
        }
    }

    override fun navigateToContacts(navController: NavHostController, group: GroupModel) {
        navController.navigate(ContactsRouter(group)) {
            launchSingleTop = true
        }
    }
}

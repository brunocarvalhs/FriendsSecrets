package br.com.brunocarvalhs.friendssecrets.core.navigation.navigation

import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.core.navigation.ChatGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import br.com.brunocarvalhs.friendssecrets.core.navigation.ContactsRouter
import br.com.brunocarvalhs.friendssecrets.core.navigation.DrawGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.EditFormsGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.GroupCreateGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.GroupDetailsGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.GroupListGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.SettingsGraph
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

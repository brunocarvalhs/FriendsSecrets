package br.com.brunocarvalhs.friendssecrets.navigation

import androidx.navigation.NavHostController
import br.com.brunocarvalhs.chat.commons.navigation.ChatGraphRouter
import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.create.commons.navigation.EditFormsRouter
import br.com.brunocarvalhs.group.create.commons.navigation.GroupCreateRouter
import br.com.brunocarvalhs.group.details.commons.navigation.GroupDetailsRouter
import br.com.brunocarvalhs.group.draw.commons.navigation.DrawGraphRouter
import br.com.brunocarvalhs.group.list.commons.navigation.GroupListRouter
import br.com.brunocarvalhs.settings.commons.navigation.SettingsGraphRoute
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class AppNavigator @Inject constructor() : CommonNavigator {

    override fun navigateToGroupDetails(navController: NavHostController, group: GroupModel) {
        navController.navigate(GroupDetailsRouter(group))
    }

    override fun navigateToGroupCreate(navController: NavHostController) {
        navController.navigate(GroupCreateRouter)
    }

    override fun navigateToSettings(navController: NavHostController) {
        navController.navigate(SettingsGraphRoute) {
            launchSingleTop = true
        }
    }

    override fun navigateToGroupList(navController: NavHostController, popUpTo: KClass<*>?, inclusive: Boolean) {
        navController.navigate(GroupListRouter) {
            popUpTo?.let {
                popUpTo(it) {
                    this.inclusive = inclusive
                }
            }
            launchSingleTop = true
        }
    }

    override fun navigateToDraw(navController: NavHostController, group: GroupModel) {
        navController.navigate(DrawGraphRouter(group)) {
            launchSingleTop = true
        }
    }

    override fun navigateToChat(navController: NavHostController, group: GroupModel) {
        navController.navigate(ChatGraphRouter(group)) {
            launchSingleTop = true
        }
    }

    override fun navigateToEditGroup(navController: NavHostController, group: GroupModel) {
        navController.navigate(EditFormsRouter(group)) {
            launchSingleTop = true
        }
    }
}

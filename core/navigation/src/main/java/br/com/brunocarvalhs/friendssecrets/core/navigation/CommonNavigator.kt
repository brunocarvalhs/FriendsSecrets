package br.com.brunocarvalhs.friendssecrets.core.navigation

import androidx.navigation.NavHostController
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlin.reflect.KClass

interface CommonNavigator {
    fun navigateToGroupDetails(navController: NavHostController, group: GroupModel)
    fun navigateToGroupCreate(navController: NavHostController)
    fun navigateToSettings(navController: NavHostController)
    fun navigateToGroupList(navController: NavHostController, popUpTo: KClass<*>? = null, inclusive: Boolean = false)
    fun navigateToDraw(navController: NavHostController, group: GroupModel)
    fun navigateToChat(navController: NavHostController, group: GroupModel)
    fun navigateToEditGroup(navController: NavHostController, group: GroupModel)
}

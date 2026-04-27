package br.com.brunocarvalhs.core.navigation.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import br.com.brunocarvalhs.core.navigation.routers.ChatGraph
import br.com.brunocarvalhs.core.navigation.routers.ContactsRouter
import br.com.brunocarvalhs.core.navigation.routers.DrawGraph
import br.com.brunocarvalhs.core.navigation.routers.EditFormsGraph
import br.com.brunocarvalhs.core.navigation.routers.GroupCreateGraph
import br.com.brunocarvalhs.core.navigation.routers.GroupDetailsGraph
import br.com.brunocarvalhs.core.navigation.routers.GroupListGraph
import br.com.brunocarvalhs.core.navigation.routers.SettingsGraph
import br.com.brunocarvalhs.core.domain.model.GroupModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AppNavigatorTest {

    private lateinit var navController: NavHostController
    private lateinit var navigator: AppNavigator

    @Before
    fun setup() {
        navController = mockk(relaxed = true)
        navigator = AppNavigator()
    }

    @Test
    fun shouldNavigateToGroupDetails() {
        val group = GroupModel()

        navigator.navigateToGroupDetails(navController, group)

        verify { navController.navigate(GroupDetailsGraph(group)) }
    }

    @Test
    fun shouldNavigateToGroupCreate() {
        navigator.navigateToGroupCreate(navController)

        verify { navController.navigate(GroupCreateGraph) }
    }

    @Test
    fun shouldNavigateToSettingsWithSingleTop() {
        navigator.navigateToSettings(navController)

        verify {
            navController.navigate(
                route = SettingsGraph,
                builder = any<NavOptionsBuilder.() -> Unit>()
            )
        }
    }

    @Test
    fun shouldNavigateToGroupListWithPopUpTo() {
        navigator.navigateToGroupList(
            navController = navController,
            popUpTo = GroupDetailsGraph::class,
            inclusive = true
        )

        verify {
            navController.navigate(
                route = GroupListGraph,
                builder = any<NavOptionsBuilder.() -> Unit>()
            )
        }
    }

    @Test
    fun shouldNavigateToDraw() {
        val group = GroupModel()

        navigator.navigateToDraw(navController, group)

        verify {
            navController.navigate(
                route = DrawGraph(group),
                builder = any<NavOptionsBuilder.() -> Unit>()
            )
        }
    }

    @Test
    fun shouldNavigateToChat() {
        val group = GroupModel()

        navigator.navigateToChat(navController, group)

        verify {
            navController.navigate(
                route = ChatGraph(group),
                builder = any<NavOptionsBuilder.() -> Unit>()
            )
        }
    }

    @Test
    fun shouldNavigateToEditGroup() {
        val group = GroupModel()

        navigator.navigateToEditGroup(navController, group)

        verify {
            navController.navigate(
                route = EditFormsGraph(group),
                builder = any<NavOptionsBuilder.() -> Unit>()
            )
        }
    }

    @Test
    fun shouldNavigateToContacts() {
        val group = GroupModel()

        navigator.navigateToContacts(navController, group)

        verify {
            navController.navigate(
                route = ContactsRouter(group),
                builder = any<NavOptionsBuilder.() -> Unit>()
            )
        }
    }
}

package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupByTokenUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupListUseCase
import br.com.brunocarvalhs.friendssecrets.presentation.MainActivity
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.GroupNavigation
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.create.GroupCreateScreen
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeScreen_displaysEmptyGroupComponent_whenGroupListIsEmpty() {
        // Start the app
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") { HomeScreen(navController) }
            }
        }

        composeTestRule.onNodeWithText("Você ainda não participa de nenhum grupo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Criar grupo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Entrar em um grupo").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysGroupCards_whenGroupListIsNotEmpty() {
        val mockGroup = mockk<GroupModel>() {
            every { name } returns "Group 1"
            every { description } returns "Description 1"
        }

        val groupListUseCase = mockk<GroupListUseCase>()
        val groupByTokenUseCase = mockk<GroupByTokenUseCase>()

        coEvery { groupListUseCase.invoke() } returns Result.success(listOf(mockGroup))

        val viewModel = HomeViewModel(
            groupListUseCase = groupListUseCase,
            groupByTokenUseCase = groupByTokenUseCase
        )

        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") { HomeScreen(navController, viewModel) }
            }
        }

        // Check if the GroupCard composables are displayed
        composeTestRule.onNodeWithTag("GroupCard").assertIsDisplayed() // Assuming you have a tag for the GroupCard
        composeTestRule.onNodeWithText("Group 1").assertIsDisplayed() // Assuming "Group 1" is a group name
    }

    @Test
    fun homeScreen_navigatesToCreateGroupScreen_whenCreateGroupButtonClicked() {
        var navController: NavHostController? = null
        // Start the app
        composeTestRule.setContent {
            navController = rememberNavController()
            navController?.let { navController ->
                NavHost(navController = navController, startDestination = "home") {
                    composable(route = "home") { HomeScreen(navController) }
                    composable(route = GroupNavigation.Create.route) {
                        GroupCreateScreen(navController)
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("Criar grupo").performClick()

        assert(navController?.currentDestination?.route == GroupNavigation.Create.route)
    }

    @Test
    fun homeScreen_displaysBottomSheet_whenGroupToEnterButtonClicked() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") { HomeScreen(navController) }
            }
        }

        composeTestRule.onNodeWithText("Entrar em um grupo").performClick()

        composeTestRule.onNodeWithContentDescription("GroupToEnterBottomSheet").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysMoreOptionsDropdown_whenMoreOptionsButtonClicked() {
        // Start the app
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") { HomeScreen(navController) }
            }
        }

        // Click the More Options button (usually an icon with content description "More")
        composeTestRule.onNodeWithContentDescription("More").performClick()

        // Check if the DropdownMenu is displayed
        composeTestRule.onNodeWithTag("DropdownMenu").assertIsDisplayed()
    }
}
package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class HomeScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun createLayout(uiState: HomeUiState = HomeUiState.Loading) {
        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeContent(
                session = UserModel(
                    id = "1",
                    name = "Test User",
                    photoUrl = null,
                    phoneNumber = "+1234567890",
                    isPhoneNumberVerified = true
                ),
                navController = navController,
                uiState = uiState,
                isSettingsEnabled = true,
                isJoinGroupEnabled = true,
                isCreateGroupEnabled = true
            )
        }
    }

    @Test
    fun testTopBar_isDisplayed() {
        createLayout(HomeUiState.Success(emptyList()))
        
        // Title should be displayed with user name
        composeTestRule.onNodeWithText(
            "${composeTestRule.activity.getString(R.string.home_title)} Test User"
        ).assertIsDisplayed()
        
        // More options button should be displayed
        composeTestRule.onNodeWithContentDescription("More").assertIsDisplayed()
    }

    @Test
    fun testEmptyState_showsEmptyComponent() {
        createLayout(HomeUiState.Success(emptyList()))
        
        // Empty state title should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.home_empty_title)
        ).assertIsDisplayed()
        
        // Empty state description should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.home_empty_description)
        ).assertIsDisplayed()
        
        // Create group button should be displayed and clickable
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.home_empty_create_group)
        ).assertIsDisplayed().assertHasClickAction()
        
        // Join group button should be displayed and clickable
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.home_empty_join_group)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testSuccessState_showsGroupList() {
        val groups = listOf(
            GroupModel(
                id = "1",
                name = "Test Group",
                description = "Test Description",
                members = mutableMapOf("member1" to "secret1")
            )
        )
        
        createLayout(HomeUiState.Success(groups))
        
        // Group name should be displayed
        composeTestRule.onNodeWithText("Test Group").assertIsDisplayed()
        
        // Floating action button should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.home_action_create_group)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testErrorState_showsErrorComponent() {
        val errorMessage = "Error loading groups"
        createLayout(HomeUiState.Error(errorMessage))
        
        // Error message should be displayed
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        
        // Retry button should be displayed and clickable
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.error_retry)
        ).assertIsDisplayed().assertHasClickAction()
    }
}
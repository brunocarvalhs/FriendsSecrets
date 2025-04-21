package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class GroupCreateScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun createLayout(uiState: GroupCreateUiState = GroupCreateUiState.Idle(emptyList())) {
        composeTestRule.setContent {
            val navController = rememberNavController()
            GroupCreateContent(
                navController = navController,
                uiState = uiState,
                onHome = {},
                onBack = {},
                onSave = { _, _, _ -> }
            )
        }
    }

    @Test
    fun testTopBar_isDisplayed() {
        createLayout()
        
        // Back button should be displayed
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.navigation_back)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testIdleState_showsFormFields() {
        createLayout()
        
        // Name field should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.group_create_input_name)
        ).assertIsDisplayed()
        
        // Description field should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.group_create_input_description)
        ).assertIsDisplayed()
        
        // Add member button should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.group_create_text_button_member)
        ).assertIsDisplayed().assertHasClickAction()
        
        // Save button should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.group_create_action_save_group)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testIdleState_withContacts_showsContactsList() {
        val contacts = listOf(
            UserModel(
                name = "Test Contact",
                id = "1",
                phoneNumber = "123456789",
                photoUrl = "",
                isPhoneNumberVerified = false,
                likes = listOf("Item 1", "Item 2")
            ),
            UserModel(
                name = "Another Contact",
                id = "2",
                phoneNumber = "987654321",
                photoUrl = "",
                isPhoneNumberVerified = true,
                likes = listOf("Item 3", "Item 4")
            )
        )
        
        createLayout(GroupCreateUiState.Idle(contacts))
        
        // Contact names should be displayed
        composeTestRule.onNodeWithText("Test Contact").assertIsDisplayed()
        composeTestRule.onNodeWithText("Another Contact").assertIsDisplayed()
    }

    @Test
    fun testLoadingState_showsLoadingIndicator() {
        createLayout(GroupCreateUiState.Loading)
        
        // Loading progress should be displayed
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.loading_description)
        ).assertIsDisplayed()
    }

    @Test
    fun testSuccessState_showsSuccessComponent() {
        createLayout(GroupCreateUiState.Success)
        
        // Success message should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.success_message)
        ).assertIsDisplayed()
        
        // Continue button should be displayed and clickable
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.success_button)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testErrorState_showsErrorComponent() {
        val errorMessage = "Error creating group"
        createLayout(GroupCreateUiState.Error(errorMessage))
        
        // Error message should be displayed
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        
        // Retry button should be displayed and clickable
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.error_retry)
        ).assertIsDisplayed().assertHasClickAction()
        
        // Back button should be displayed and clickable
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.error_back)
        ).assertIsDisplayed().assertHasClickAction()
    }
}
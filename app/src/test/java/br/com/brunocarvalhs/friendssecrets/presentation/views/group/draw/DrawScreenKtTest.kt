package br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw

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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class DrawScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testTopBar_isDisplayed() {
        // When
        composeTestRule.setContent {
            DrawContent(
                navController = rememberNavController(),
                uiState = DrawUiState.Idle,
                eventIntent = {}
            )
        }

        // Then
        // Title should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.draw_screen_title)
        ).assertIsDisplayed()
        
        // Back button should be displayed
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.navigation_back)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testIdleState_showsCodeInputAndDrawButton() {
        // When
        composeTestRule.setContent {
            DrawContent(
                navController = rememberNavController(),
                uiState = DrawUiState.Idle,
                eventIntent = {}
            )
        }

        // Then
        // Code input field should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.draw_screen_code_secret)
        ).assertIsDisplayed()
        
        // Draw button should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.draw_screen_action_title)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testLoadingState_showsLoadingIndicator() {
        // When
        composeTestRule.setContent {
            DrawContent(
                navController = rememberNavController(),
                uiState = DrawUiState.Loading,
                eventIntent = {}
            )
        }

        // Then
        // Loading indicator should be displayed
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.loading_description)
        ).assertIsDisplayed()
    }

    @Test
    fun testSuccessState_showsDrawResultAndLikes() {
        // Given
        val draw = mapOf("John Doe" to "Books|Movies|Games")
        val group = GroupModel(name = "Test Group")
        
        // When
        composeTestRule.setContent {
            DrawContent(
                navController = rememberNavController(),
                uiState = DrawUiState.Success(draw = draw, group = group),
                eventIntent = {},
                isGenerativeEnabled = true
            )
        }

        // Then
        // Secret name should be displayed
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        
        // Likes title should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.draw_screen_title_like)
        ).assertIsDisplayed()
        
        // Individual likes should be displayed
        composeTestRule.onNodeWithText("Books").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movies").assertIsDisplayed()
        composeTestRule.onNodeWithText("Games").assertIsDisplayed()
        
        // Generative button should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.draw_screen_action_generative)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testSuccessState_withMultipleLikes_displaysAllLikes() {
        // Given
        val draw = mapOf("John Doe" to "Books|Movies|Games|Music|Art|Sports")
        val group = GroupModel(name = "Test Group")
        
        // When
        composeTestRule.setContent {
            DrawContent(
                navController = rememberNavController(),
                uiState = DrawUiState.Success(draw = draw, group = group),
                eventIntent = {},
                isGenerativeEnabled = true
            )
        }

        // Then
        // All likes should be displayed
        composeTestRule.onNodeWithText("Books").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movies").assertIsDisplayed()
        composeTestRule.onNodeWithText("Games").assertIsDisplayed()
        composeTestRule.onNodeWithText("Music").assertIsDisplayed()
        composeTestRule.onNodeWithText("Art").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sports").assertIsDisplayed()
    }

    @Test
    fun testSuccessState_withGenerativeDisabled_hidesGenerativeButton() {
        // Given
        val draw = mapOf("John Doe" to "Books|Movies|Games")
        val group = GroupModel(name = "Test Group")
        
        // When
        composeTestRule.setContent {
            DrawContent(
                navController = rememberNavController(),
                uiState = DrawUiState.Success(draw = draw, group = group),
                eventIntent = {},
                isGenerativeEnabled = false
            )
        }

        // Then
        // Secret name should be displayed
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        
        // Likes should be displayed
        composeTestRule.onNodeWithText("Books").assertIsDisplayed()
        
        // Generative button should NOT be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.draw_screen_action_generative)
        ).assertDoesNotExist()
    }

    @Test
    fun testErrorState_showsErrorComponent() {
        // Given
        val errorMessage = "Error loading draw"
        
        // When
        composeTestRule.setContent {
            DrawContent(
                navController = rememberNavController(),
                uiState = DrawUiState.Error(error = errorMessage),
                eventIntent = {}
            )
        }

        // Then
        // Error message should be displayed
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        
        // Retry button should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.error_retry)
        ).assertIsDisplayed().assertHasClickAction()
    }
}
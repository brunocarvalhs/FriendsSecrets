package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class ErrorComponentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testErrorComponent_displaysErrorMessage() {
        // Given
        val errorMessage = "Something went wrong"
        var refreshCalled = false
        var backCalled = false

        // When
        composeTestRule.setContent {
            ErrorComponent(
                message = errorMessage,
                onRefresh = { refreshCalled = true },
                onBack = { backCalled = true }
            )
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun testErrorComponent_withRetryButton_callsOnRefresh() {
        // Given
        val errorMessage = "Network error"
        var refreshCalled = false

        // When
        composeTestRule.setContent {
            ErrorComponent(
                message = errorMessage,
                onRefresh = { refreshCalled = true }
            )
        }

        // Then
        val retryButtonText = composeTestRule.activity.getString(R.string.error_component_button_try_again)
        composeTestRule.onNodeWithText(retryButtonText).assertIsDisplayed()
        composeTestRule.onNodeWithText(retryButtonText).assertHasClickAction()
        
        // Click retry button
        composeTestRule.onNodeWithText(retryButtonText).performClick()
        
        // Verify onRefresh was called
        assert(refreshCalled) { "onRefresh was not called" }
    }

    @Test
    fun testErrorComponent_withBackButton_callsOnBack() {
        // Given
        val errorMessage = "Permission denied"
        var backCalled = false

        // When
        composeTestRule.setContent {
            ErrorComponent(
                message = errorMessage,
                onBack = { backCalled = true }
            )
        }

        // Then
        val backButtonText = composeTestRule.activity.getString(R.string.error_component_button_to_home)
        composeTestRule.onNodeWithText(backButtonText).assertIsDisplayed()
        composeTestRule.onNodeWithText(backButtonText).assertHasClickAction()
        
        // Click back button
        composeTestRule.onNodeWithText(backButtonText).performClick()
        
        // Verify onBack was called
        assert(backCalled) { "onBack was not called" }
    }

    @Test
    fun testErrorComponent_withBothButtons_showsBothButtons() {
        // Given
        val errorMessage = "Data loading failed"
        var refreshCalled = false
        var backCalled = false

        // When
        composeTestRule.setContent {
            ErrorComponent(
                message = errorMessage,
                onRefresh = { refreshCalled = true },
                onBack = { backCalled = true }
            )
        }

        // Then
        val retryButtonText = composeTestRule.activity.getString(R.string.error_component_button_try_again)
        val backButtonText = composeTestRule.activity.getString(R.string.error_component_button_to_home)
        
        composeTestRule.onNodeWithText(retryButtonText).assertIsDisplayed()
        composeTestRule.onNodeWithText(backButtonText).assertIsDisplayed()
    }

    @Test
    fun testErrorComponent_withNoButtons_hidesButtons() {
        // Given
        val errorMessage = "Critical error"

        // When
        composeTestRule.setContent {
            ErrorComponent(
                message = errorMessage
            )
        }

        // Then
        val retryButtonText = composeTestRule.activity.getString(R.string.error_component_button_try_again)
        val backButtonText = composeTestRule.activity.getString(R.string.error_component_button_to_home)
        
        composeTestRule.onNodeWithText(retryButtonText).assertDoesNotExist()
        composeTestRule.onNodeWithText(backButtonText).assertDoesNotExist()
    }
}
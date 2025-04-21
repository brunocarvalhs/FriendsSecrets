package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class ErrorComponentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun errorComponent_displaysErrorMessage() {
        // Given
        val errorMessage = "Test error message"
        
        // When
        composeTestRule.setContent {
            ErrorComponent(message = errorMessage)
        }
        
        // Then
        composeTestRule.onNodeWithText("Ops!").assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun errorComponent_withRefreshCallback_showsRetryButton() {
        // Given
        val errorMessage = "Test error message"
        val refreshCalled = AtomicBoolean(false)
        
        // When
        composeTestRule.setContent {
            ErrorComponent(
                message = errorMessage,
                onRefresh = { refreshCalled.set(true) }
            )
        }
        
        // Then
        val retryButtonText = composeTestRule.activity.getString(R.string.error_component_button_try_again)
        composeTestRule.onNodeWithText(retryButtonText).assertIsDisplayed().assertHasClickAction()
        
        // When clicking the retry button
        composeTestRule.onNodeWithText(retryButtonText).performClick()
        
        // Then the refresh callback should be called
        assert(refreshCalled.get())
    }

    @Test
    fun errorComponent_withBackCallback_showsBackButton() {
        // Given
        val errorMessage = "Test error message"
        val backCalled = AtomicBoolean(false)
        
        // When
        composeTestRule.setContent {
            ErrorComponent(
                message = errorMessage,
                onBack = { backCalled.set(true) }
            )
        }
        
        // Then
        val backButtonText = composeTestRule.activity.getString(R.string.error_component_button_to_home)
        composeTestRule.onNodeWithText(backButtonText).assertIsDisplayed().assertHasClickAction()
        
        // When clicking the back button
        composeTestRule.onNodeWithText(backButtonText).performClick()
        
        // Then the back callback should be called
        assert(backCalled.get())
    }

    @Test
    fun errorComponent_withBothCallbacks_showsBothButtons() {
        // Given
        val errorMessage = "Test error message"
        val refreshCalled = AtomicBoolean(false)
        val backCalled = AtomicBoolean(false)
        
        // When
        composeTestRule.setContent {
            ErrorComponent(
                message = errorMessage,
                onRefresh = { refreshCalled.set(true) },
                onBack = { backCalled.set(true) }
            )
        }
        
        // Then
        val retryButtonText = composeTestRule.activity.getString(R.string.error_component_button_try_again)
        val backButtonText = composeTestRule.activity.getString(R.string.error_component_button_to_home)
        
        composeTestRule.onNodeWithText(retryButtonText).assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithText(backButtonText).assertIsDisplayed().assertHasClickAction()
        
        // When clicking the retry button
        composeTestRule.onNodeWithText(retryButtonText).performClick()
        
        // Then the refresh callback should be called
        assert(refreshCalled.get())
        
        // When clicking the back button
        composeTestRule.onNodeWithText(backButtonText).performClick()
        
        // Then the back callback should be called
        assert(backCalled.get())
    }

    @Test
    fun errorComponent_withOnlyBackCallback_showsBackAsMainButton() {
        // Given
        val errorMessage = "Test error message"
        val backCalled = AtomicBoolean(false)
        
        // When
        composeTestRule.setContent {
            ErrorComponent(
                message = errorMessage,
                onBack = { backCalled.set(true) }
            )
        }
        
        // Then
        val backButtonText = composeTestRule.activity.getString(R.string.error_component_button_to_home)
        composeTestRule.onNodeWithText(backButtonText).assertIsDisplayed().assertHasClickAction()
        
        // The retry button should not be displayed
        val retryButtonText = composeTestRule.activity.getString(R.string.error_component_button_try_again)
        composeTestRule.onNodeWithText(retryButtonText).assertDoesNotExist()
    }
}
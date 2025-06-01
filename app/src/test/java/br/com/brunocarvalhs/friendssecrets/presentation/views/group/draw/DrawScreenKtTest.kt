package br.com.brunocarvalhs.friendssecrets.presentation.views.group.draw

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.data.model.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.ToggleManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class DrawScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val navController = mock<NavController>()

    @Test
    fun testDrawScreen_idleState_displaysCodeInputAndDrawButton() {
        // Given
        val viewModel = FakeDrawViewModel(DrawUiState.Idle)
        val toggleManager = FakeToggleManager(isGenerativeEnabled = true)

        // When
        composeTestRule.setContent {
            DrawScreen(
                groupId = "group-123",
                navController = navController,
                viewModel = viewModel,
                toggleManager = toggleManager
            )
        }

        // Then
        val titleText = composeTestRule.activity.getString(R.string.draw_title)
        composeTestRule.onNodeWithText(titleText).assertIsDisplayed()
        
        val codeHintText = composeTestRule.activity.getString(R.string.draw_code_hint)
        composeTestRule.onNodeWithText(codeHintText).assertIsDisplayed()
        
        val drawButtonText = composeTestRule.activity.getString(R.string.draw_button)
        composeTestRule.onNodeWithText(drawButtonText).assertIsDisplayed()
        composeTestRule.onNodeWithText(drawButtonText).assertIsEnabled()
    }

    @Test
    fun testDrawScreen_loadingState_displaysLoadingIndicator() {
        // Given
        val viewModel = FakeDrawViewModel(DrawUiState.Loading)
        val toggleManager = FakeToggleManager(isGenerativeEnabled = true)

        // When
        composeTestRule.setContent {
            DrawScreen(
                groupId = "group-123",
                navController = navController,
                viewModel = viewModel,
                toggleManager = toggleManager
            )
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Loading").assertIsDisplayed()
    }

    @Test
    fun testDrawScreen_successState_displaysDrawResult() {
        // Given
        val contact = UserEntities(
            id = "user-123",
            name = "John Doe",
            phone = "123456789",
            likes = listOf("Books", "Movies")
        )
        val viewModel = FakeDrawViewModel(DrawUiState.Success(contact))
        val toggleManager = FakeToggleManager(isGenerativeEnabled = true)

        // When
        composeTestRule.setContent {
            DrawScreen(
                groupId = "group-123",
                navController = navController,
                viewModel = viewModel,
                toggleManager = toggleManager
            )
        }

        // Then
        val resultText = composeTestRule.activity.getString(R.string.draw_result)
        composeTestRule.onNodeWithText(resultText).assertIsDisplayed()
        
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("123456789").assertIsDisplayed()
        
        // Likes should be displayed
        composeTestRule.onNodeWithText("Books").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movies").assertIsDisplayed()
    }

    @Test
    fun testDrawScreen_withGenerativeEnabled_showsGenerativeButton() {
        // Given
        val viewModel = FakeDrawViewModel(DrawUiState.Idle)
        val toggleManager = FakeToggleManager(isGenerativeEnabled = true)

        // When
        composeTestRule.setContent {
            DrawScreen(
                groupId = "group-123",
                navController = navController,
                viewModel = viewModel,
                toggleManager = toggleManager
            )
        }

        // Then
        val generativeButtonText = composeTestRule.activity.getString(R.string.draw_generative_button)
        composeTestRule.onNodeWithText(generativeButtonText).assertIsDisplayed()
    }

    @Test
    fun testDrawScreen_withGenerativeDisabled_hidesGenerativeButton() {
        // Given
        val viewModel = FakeDrawViewModel(DrawUiState.Idle)
        val toggleManager = FakeToggleManager(isGenerativeEnabled = false)

        // When
        composeTestRule.setContent {
            DrawScreen(
                groupId = "group-123",
                navController = navController,
                viewModel = viewModel,
                toggleManager = toggleManager
            )
        }

        // Then
        val generativeButtonText = composeTestRule.activity.getString(R.string.draw_generative_button)
        composeTestRule.onNodeWithText(generativeButtonText).assertDoesNotExist()
    }

    @Test
    fun testDrawScreen_errorState_displaysErrorComponent() {
        // Given
        val errorMessage = "Failed to perform draw"
        val viewModel = FakeDrawViewModel(DrawUiState.Error(errorMessage))
        val toggleManager = FakeToggleManager(isGenerativeEnabled = true)

        // When
        composeTestRule.setContent {
            DrawScreen(
                groupId = "group-123",
                navController = navController,
                viewModel = viewModel,
                toggleManager = toggleManager
            )
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        
        val retryButtonText = composeTestRule.activity.getString(R.string.error_component_button_try_again)
        composeTestRule.onNodeWithText(retryButtonText).assertIsDisplayed()
    }
}

class FakeDrawViewModel(initialState: DrawUiState) : DrawViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    override val uiState: StateFlow<DrawUiState> = _uiState

    override fun eventIntent(intent: DrawIntent) {
        // No-op for tests
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FakeDrawViewModel(DrawUiState.Idle) as T
            }
        }
    }
}

class FakeToggleManager(private val isGenerativeEnabled: Boolean) : ToggleManager {
    override fun isEnabled(feature: String): Boolean {
        return when (feature) {
            "generative_ai" -> isGenerativeEnabled
            else -> false
        }
    }
}
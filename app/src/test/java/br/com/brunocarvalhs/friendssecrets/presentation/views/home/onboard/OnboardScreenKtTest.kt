package br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class OnboardScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val navController = mock<NavHostController>()

    @Test
    fun testTopBar_isDisplayed() {
        // Given
        val viewModel = FakeOnboardViewModel()

        // When
        composeTestRule.setContent {
            OnboardScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
        // Close button should be displayed
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.onboarding_screen_close)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testFirstPage_showsCorrectContent() {
        // Given
        val viewModel = FakeOnboardViewModel(initialPage = 0)

        // When
        composeTestRule.setContent {
            OnboardScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
        // First page title should be displayed
        composeTestRule.onNodeWithText(OnboardViewModel.default[0].title)
            .assertIsDisplayed()
        
        // First page description should be displayed
        composeTestRule.onNodeWithText(OnboardViewModel.default[0].description)
            .assertIsDisplayed()
        
        // Next button should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.onboarding_next)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testMiddlePage_showsCorrectContent() {
        // Given
        val middlePageIndex = OnboardViewModel.default.size / 2
        val viewModel = FakeOnboardViewModel(initialPage = middlePageIndex)

        // When
        composeTestRule.setContent {
            OnboardScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
        // Middle page title should be displayed
        composeTestRule.onNodeWithText(OnboardViewModel.default[middlePageIndex].title)
            .assertIsDisplayed()
        
        // Middle page description should be displayed
        composeTestRule.onNodeWithText(OnboardViewModel.default[middlePageIndex].description)
            .assertIsDisplayed()
        
        // Next button should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.onboarding_next)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testLastPage_showsFinishButton() {
        // Given
        val lastPageIndex = OnboardViewModel.default.size - 1
        val viewModel = FakeOnboardViewModel(initialPage = lastPageIndex)

        // When
        composeTestRule.setContent {
            OnboardScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
        // Last page title should be displayed
        composeTestRule.onNodeWithText(OnboardViewModel.default[lastPageIndex].title)
            .assertIsDisplayed()
        
        // Last page description should be displayed
        composeTestRule.onNodeWithText(OnboardViewModel.default[lastPageIndex].description)
            .assertIsDisplayed()
        
        // Finish button should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.onboarding_finish)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testCustomPages_showsCorrectContent() {
        // Given
        val customPages = listOf(
            OnboardViewModel.Onboarding(
                title = "Custom Title 1",
                description = "Custom Description 1",
                imageSource = OnboardViewModel.ImageSource.Resource(R.drawable.ic_launcher_foreground)
            ),
            OnboardViewModel.Onboarding(
                title = "Custom Title 2",
                description = "Custom Description 2",
                imageSource = OnboardViewModel.ImageSource.Resource(R.drawable.ic_launcher_foreground)
            )
        )
        
        val viewModel = FakeOnboardViewModel(pages = customPages)

        // When
        composeTestRule.setContent {
            OnboardScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
        // Custom page title should be displayed
        composeTestRule.onNodeWithText("Custom Title 1")
            .assertIsDisplayed()
        
        // Custom page description should be displayed
        composeTestRule.onNodeWithText("Custom Description 1")
            .assertIsDisplayed()
    }
}

class FakeOnboardViewModel(
    private val initialPage: Int = 0,
    private val pages: List<OnboardViewModel.Onboarding> = OnboardViewModel.default
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardUiState(initialPage = initialPage, pages = pages))
    val uiState: StateFlow<OnboardUiState> = _uiState

    fun onEvent(event: OnboardViewIntent) {
        // No-op for tests
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FakeOnboardViewModel() as T
            }
        }
    }
}
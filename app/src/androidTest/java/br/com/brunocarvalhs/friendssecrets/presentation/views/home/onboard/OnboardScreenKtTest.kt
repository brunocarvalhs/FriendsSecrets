package br.com.brunocarvalhs.friendssecrets.presentation.views.home.onboard

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnboardScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun createLayout(
        pages: List<OnboardViewModel.Onboarding> = OnboardViewModel.default,
        initialPage: Int = 0
    ) {
        composeTestRule.setContent {
            val navController = rememberNavController()
            OnboardingContent(
                navController = navController,
                initialPage = initialPage,
                pages = pages
            )
        }
    }

    @Test
    fun testTopBar_isDisplayed() {
        createLayout()
        
        // Close button should be displayed
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.onboarding_screen_close)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testFirstPage_showsCorrectContent() {
        createLayout(initialPage = 0)
        
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
    fun testLastPage_showsFinishButton() {
        val lastPageIndex = OnboardViewModel.default.size - 1
        createLayout(initialPage = lastPageIndex)
        
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
    fun testLoadingState_showsLoadingIndicator() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            OnboardingScreen(
                navController = navController,
                viewModel = FakeOnboardViewModel(OnboardUiState.Loading)
            )
        }
        
        // Loading indicator should be displayed
        composeTestRule.onNodeWithContentDescription("Loading")
            .assertExists()
    }
}

class FakeOnboardViewModel(private val state: OnboardUiState) : OnboardViewModel() {
    override val uiState = kotlinx.coroutines.flow.MutableStateFlow(state)
    
    override fun onEvent(event: OnboardViewIntent) {
        // Do nothing
    }
}
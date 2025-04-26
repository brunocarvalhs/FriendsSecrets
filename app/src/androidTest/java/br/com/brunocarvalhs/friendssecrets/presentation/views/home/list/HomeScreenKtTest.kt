package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val mockToggleManager = mockk<ToggleManager>(relaxed = true)
    private val mockViewModel = mockk<HomeViewModel>(relaxed = true)

    private fun createLayout(uiState: HomeUiState = HomeUiState.Loading) {
        every { mockViewModel.uiState } returns MutableStateFlow(uiState)
        every { mockToggleManager.isFeatureEnabled(any()) } returns true

        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeContent(
                navController = navController,
                uiState = uiState,
                isSettingsEnabled = true,
                isJoinGroupEnabled = true,
                isCreateGroupEnabled = true
            )
        }
    }

    @Test
    fun testLoadingState_showsLoadingIndicator() {
        createLayout(HomeUiState.Loading)
        
        // Loading progress should be displayed
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.loading_description)
        ).assertIsDisplayed()
    }

    @Test
    fun testEmptyState_showsEmptyComponent() {
        createLayout(HomeUiState.Success(emptyList()))
        
        // Empty state text should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.home_empty_title)
        ).assertIsDisplayed()
        
        // Create group button should be displayed and clickable
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.home_empty_create_group)
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
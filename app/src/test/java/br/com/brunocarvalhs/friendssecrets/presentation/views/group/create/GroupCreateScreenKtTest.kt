package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.data.model.UserEntities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class GroupCreateScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val navController = mock<NavController>()

    @Test
    fun testTopBar_isDisplayed() {
        // Given
        val viewModel = FakeGroupCreateViewModel(GroupCreateUiState.Idle(emptyList()))

        // When
        composeTestRule.setContent {
            GroupCreateScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
        // Back button should be displayed
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.navigation_back)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testIdleState_showsFormFields() {
        // Given
        val viewModel = FakeGroupCreateViewModel(GroupCreateUiState.Idle(emptyList()))

        // When
        composeTestRule.setContent {
            GroupCreateScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
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
        // Given
        val contacts = listOf(
            UserEntities(
                name = "Test Contact",
                id = "1",
                phone = "123456789",
                likes = listOf("Item 1", "Item 2")
            ),
            UserEntities(
                name = "Another Contact",
                id = "2",
                phone = "987654321",
                likes = listOf("Item 3", "Item 4")
            )
        )
        
        val viewModel = FakeGroupCreateViewModel(GroupCreateUiState.Idle(contacts))

        // When
        composeTestRule.setContent {
            GroupCreateScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
        // Contact names should be displayed
        composeTestRule.onNodeWithText("Test Contact").assertIsDisplayed()
        composeTestRule.onNodeWithText("Another Contact").assertIsDisplayed()
    }

    @Test
    fun testLoadingState_showsLoadingIndicator() {
        // Given
        val viewModel = FakeGroupCreateViewModel(GroupCreateUiState.Loading)

        // When
        composeTestRule.setContent {
            GroupCreateScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
        // Loading progress should be displayed
        composeTestRule.onNodeWithContentDescription("Loading").assertIsDisplayed()
    }

    @Test
    fun testSuccessState_showsSuccessComponent() {
        // Given
        val viewModel = FakeGroupCreateViewModel(GroupCreateUiState.Success)

        // When
        composeTestRule.setContent {
            GroupCreateScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
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
        // Given
        val errorMessage = "Error creating group"
        val viewModel = FakeGroupCreateViewModel(GroupCreateUiState.Error(errorMessage))

        // When
        composeTestRule.setContent {
            GroupCreateScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        
        // Then
        // Error message should be displayed
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        
        // Retry button should be displayed and clickable
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.error_component_button_try_again)
        ).assertIsDisplayed().assertHasClickAction()
    }
}

class FakeGroupCreateViewModel(initialState: GroupCreateUiState) : GroupCreateViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    override val uiState: StateFlow<GroupCreateUiState> = _uiState

    override fun eventIntent(intent: GroupCreateIntent) {
        // No-op for tests
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FakeGroupCreateViewModel(GroupCreateUiState.Idle(emptyList())) as T
            }
        }
    }
}
package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.data.model.GroupEntities
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
class HomeScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val navController = mock<NavController>()
    private val testUser = UserEntities(
        id = "1",
        name = "Test User",
        phone = "+1234567890",
        likes = emptyList()
    )

    @Test
    fun testTopBar_isDisplayed() {
        // Given
        val viewModel = FakeHomeViewModel(HomeUiState.Success(emptyList()))
        val toggleManager = FakeToggleManager(
            isSettingsEnabled = true,
            isJoinGroupEnabled = true,
            isCreateGroupEnabled = true
        )
        val sessionViewModel = FakeSessionViewModel(testUser)

        // When
        composeTestRule.setContent {
            HomeScreen(
                navController = navController,
                viewModel = viewModel,
                toggleManager = toggleManager,
                sessionViewModel = sessionViewModel
            )
        }
        
        // Then
        // Title should be displayed with user name
        composeTestRule.onNodeWithText(
            "${composeTestRule.activity.getString(R.string.home_title)} Test User"
        ).assertIsDisplayed()
        
        // More options button should be displayed
        composeTestRule.onNodeWithContentDescription("More").assertIsDisplayed()
    }

    @Test
    fun testEmptyState_showsEmptyComponent() {
        // Given
        val viewModel = FakeHomeViewModel(HomeUiState.Success(emptyList()))
        val toggleManager = FakeToggleManager(
            isSettingsEnabled = true,
            isJoinGroupEnabled = true,
            isCreateGroupEnabled = true
        )
        val sessionViewModel = FakeSessionViewModel(testUser)

        // When
        composeTestRule.setContent {
            HomeScreen(
                navController = navController,
                viewModel = viewModel,
                toggleManager = toggleManager,
                sessionViewModel = sessionViewModel
            )
        }
        
        // Then
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
        // Given
        val groups = listOf(
            GroupEntities(
                id = "1",
                name = "Test Group",
                description = "Test Description",
                members = mutableMapOf("member1" to "secret1")
            )
        )
        
        val viewModel = FakeHomeViewModel(HomeUiState.Success(groups))
        val toggleManager = FakeToggleManager(
            isSettingsEnabled = true,
            isJoinGroupEnabled = true,
            isCreateGroupEnabled = true
        )
        val sessionViewModel = FakeSessionViewModel(testUser)

        // When
        composeTestRule.setContent {
            HomeScreen(
                navController = navController,
                viewModel = viewModel,
                toggleManager = toggleManager,
                sessionViewModel = sessionViewModel
            )
        }
        
        // Then
        // Group name should be displayed
        composeTestRule.onNodeWithText("Test Group").assertIsDisplayed()
        
        // Floating action button should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.home_action_create_group)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testErrorState_showsErrorComponent() {
        // Given
        val errorMessage = "Error loading groups"
        val viewModel = FakeHomeViewModel(HomeUiState.Error(errorMessage))
        val toggleManager = FakeToggleManager(
            isSettingsEnabled = true,
            isJoinGroupEnabled = true,
            isCreateGroupEnabled = true
        )
        val sessionViewModel = FakeSessionViewModel(testUser)

        // When
        composeTestRule.setContent {
            HomeScreen(
                navController = navController,
                viewModel = viewModel,
                toggleManager = toggleManager,
                sessionViewModel = sessionViewModel
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

class FakeHomeViewModel(initialState: HomeUiState) : HomeViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    override val uiState: StateFlow<HomeUiState> = _uiState

    override fun eventIntent(intent: HomeIntent) {
        // No-op for tests
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FakeHomeViewModel(HomeUiState.Loading) as T
            }
        }
    }
}

class FakeSessionViewModel(private val user: UserEntities?) : SessionViewModel() {
    override val session: StateFlow<UserEntities?> = MutableStateFlow(user)

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FakeSessionViewModel(null) as T
            }
        }
    }
}

class FakeToggleManager(
    private val isSettingsEnabled: Boolean = false,
    private val isJoinGroupEnabled: Boolean = false,
    private val isCreateGroupEnabled: Boolean = false
) : ToggleManager {
    override fun isEnabled(feature: String): Boolean {
        return when (feature) {
            "settings" -> isSettingsEnabled
            "join_group" -> isJoinGroupEnabled
            "create_group" -> isCreateGroupEnabled
            else -> false
        }
    }
}
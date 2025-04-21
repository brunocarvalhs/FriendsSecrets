package br.com.brunocarvalhs.friendssecrets.presentation.views.settings.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.domain.services.ToggleManager
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.SettingsNavigation
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class SettingsScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val navController = mock<NavHostController>()

    @Test
    fun testTopBar_isDisplayed() {
        // Given
        val toggleManager = FakeToggleManager(
            isFingerprintEnabled = true,
            isAppearanceEnabled = true,
            isReportIssueEnabled = true,
            isFAQEnabled = true
        )

        // When
        composeTestRule.setContent {
            SettingsScreen(
                navController = navController,
                toggleManager = toggleManager
            )
        }
        
        // Then
        // Title should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(SettingsNavigation.Settings.title)
        ).assertIsDisplayed()
        
        // Back button should be displayed
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.navigation_back)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testGeneralSection_isDisplayed() {
        // Given
        val toggleManager = FakeToggleManager(
            isFingerprintEnabled = true,
            isAppearanceEnabled = true,
            isReportIssueEnabled = true,
            isFAQEnabled = true
        )

        // When
        composeTestRule.setContent {
            SettingsScreen(
                navController = navController,
                toggleManager = toggleManager
            )
        }
        
        // Then
        // General section title should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.settings_screen_general)
        ).assertIsDisplayed()
        
        // Security option should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.settings_screen_security)
        ).assertIsDisplayed().assertHasClickAction()
        
        // Appearance option should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(SettingsNavigation.Appearance.title)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testSupportSection_isDisplayed() {
        // Given
        val toggleManager = FakeToggleManager(
            isFingerprintEnabled = true,
            isAppearanceEnabled = true,
            isReportIssueEnabled = true,
            isFAQEnabled = true
        )

        // When
        composeTestRule.setContent {
            SettingsScreen(
                navController = navController,
                toggleManager = toggleManager
            )
        }
        
        // Then
        // Support section title should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.settings_screen_support)
        ).assertIsDisplayed()
        
        // Report issue option should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(SettingsNavigation.ReportIssue.title)
        ).assertIsDisplayed().assertHasClickAction()
        
        // FAQ option should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(SettingsNavigation.FAQ.title)
        ).assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun testPartiallyDisabledFeatures() {
        // Given
        val toggleManager = FakeToggleManager(
            isFingerprintEnabled = false,
            isAppearanceEnabled = true,
            isReportIssueEnabled = false,
            isFAQEnabled = true
        )

        // When
        composeTestRule.setContent {
            SettingsScreen(
                navController = navController,
                toggleManager = toggleManager
            )
        }
        
        // Then
        // General section title should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.settings_screen_general)
        ).assertIsDisplayed()
        
        // Security option should not be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.settings_screen_security)
        ).assertDoesNotExist()
        
        // Appearance option should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(SettingsNavigation.Appearance.title)
        ).assertIsDisplayed().assertHasClickAction()
        
        // Support section title should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.settings_screen_support)
        ).assertIsDisplayed()
        
        // Report issue option should not be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(SettingsNavigation.ReportIssue.title)
        ).assertDoesNotExist()
        
        // FAQ option should be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(SettingsNavigation.FAQ.title)
        ).assertIsDisplayed().assertHasClickAction()
    }
}

class FakeToggleManager(
    private val isFingerprintEnabled: Boolean = false,
    private val isAppearanceEnabled: Boolean = false,
    private val isReportIssueEnabled: Boolean = false,
    private val isFAQEnabled: Boolean = false
) : ToggleManager {
    override fun isEnabled(feature: String): Boolean {
        return when (feature) {
            "fingerprint" -> isFingerprintEnabled
            "appearance" -> isAppearanceEnabled
            "report_issue" -> isReportIssueEnabled
            "faq" -> isFAQEnabled
            else -> false
        }
    }
}
package br.com.brunocarvalhs.friendssecrets.presentation.views.settings.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.SettingsNavigation
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val mockToggleManager = mockk<ToggleManager>(relaxed = true)

    private fun createLayout(
        isFingerprintEnabled: Boolean = true,
        isAppearanceEnabled: Boolean = true,
        isReportIssueEnabled: Boolean = true,
        isFAQEnabled: Boolean = true
    ) {
        composeTestRule.setContent {
            val navController = rememberNavController()
            SettingsContent(
                navController = navController,
                isFingerprintEnabled = isFingerprintEnabled,
                isAppearanceEnabled = isAppearanceEnabled,
                isReportIssueEnabled = isReportIssueEnabled,
                isFAQEnabled = isFAQEnabled
            )
        }
    }

    @Test
    fun testTopBar_isDisplayed() {
        createLayout()
        
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
        createLayout()
        
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
        createLayout()
        
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
    fun testDisabledFeatures_areNotDisplayed() {
        createLayout(
            isFingerprintEnabled = false,
            isAppearanceEnabled = false,
            isReportIssueEnabled = false,
            isFAQEnabled = false
        )
        
        // General section title should not be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.settings_screen_general)
        ).assertDoesNotExist()
        
        // Security option should not be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.settings_screen_security)
        ).assertDoesNotExist()
        
        // Appearance option should not be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(SettingsNavigation.Appearance.title)
        ).assertDoesNotExist()
        
        // Support section title should not be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.settings_screen_support)
        ).assertDoesNotExist()
        
        // Report issue option should not be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(SettingsNavigation.ReportIssue.title)
        ).assertDoesNotExist()
        
        // FAQ option should not be displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(SettingsNavigation.FAQ.title)
        ).assertDoesNotExist()
    }
}
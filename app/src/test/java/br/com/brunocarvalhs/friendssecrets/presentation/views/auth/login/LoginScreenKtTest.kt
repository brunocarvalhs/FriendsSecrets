package br.com.brunocarvalhs.friendssecrets.presentation.views.auth.login

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun createLayout() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    @Test
    fun testLogo_isDisplayed() {
        createLayout()

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.app_name)
        ).assertIsDisplayed()
    }

    @Test
    fun testTitle_isDisplayed() {
        createLayout()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.login_screen_welcome,
                composeTestRule.activity.getString(R.string.app_name)
            )
        ).assertIsDisplayed()
    }

    @Test
    fun testDescription_isDisplayed() {
        createLayout()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.login_screen_description)
        ).assertIsDisplayed()
    }

    @Test
    fun testRegisterButton_isDisplayedAndClickable() {
        createLayout()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.login_screen_button_register)
        )
            .assertIsDisplayed()
            .assertHasClickAction()
    }
}

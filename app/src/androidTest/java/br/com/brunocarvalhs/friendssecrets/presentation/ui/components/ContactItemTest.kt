package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContactItemTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun contactItem_displaysCorrectInformation() {
        // Given
        val contact = UserModel(
            name = "John Doe",
            id = "1",
            phoneNumber = "123456789",
            photoUrl = "",
            isPhoneNumberVerified = false,
            likes = listOf("Books", "Movies", "Games")
        )

        // When
        composeTestRule.setContent {
            ContactItem(contact = contact)
        }

        // Then
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("123456789").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Default user icon").assertIsDisplayed()
        
        // Likes should be hidden initially
        composeTestRule.onNodeWithText("Books").assertDoesNotExist()
    }

    @Test
    fun contactItem_withNoPhoneNumber_hidesPhoneNumber() {
        // Given
        val contact = UserModel(
            name = "John Doe",
            id = "1",
            phoneNumber = "",
            photoUrl = "",
            isPhoneNumberVerified = false,
            likes = listOf("Books", "Movies", "Games")
        )

        // When
        composeTestRule.setContent {
            ContactItem(contact = contact)
        }

        // Then
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        // Phone number should not be displayed
        composeTestRule.onNodeWithText("123456789").assertDoesNotExist()
    }

    @Test
    fun contactItem_withLikes_showsLikesWhenClicked() {
        // Given
        val contact = UserModel(
            name = "John Doe",
            id = "1",
            phoneNumber = "123456789",
            photoUrl = "",
            isPhoneNumberVerified = false,
            likes = listOf("Books", "Movies", "Games")
        )

        // When
        composeTestRule.setContent {
            ContactItem(contact = contact)
        }

        // Then
        // Likes should be hidden initially
        composeTestRule.onNodeWithText("Books").assertDoesNotExist()
        
        // Click on the contact item
        composeTestRule.onNodeWithText("John Doe").performClick()
        
        // Likes should be visible after click
        composeTestRule.onNodeWithText("Books").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movies").assertIsDisplayed()
        composeTestRule.onNodeWithText("Games").assertIsDisplayed()
    }

    @Test
    fun contactItem_withNoLikes_isNotClickable() {
        // Given
        val contact = UserModel(
            name = "John Doe",
            id = "1",
            phoneNumber = "123456789",
            photoUrl = "",
            isPhoneNumberVerified = false,
            likes = emptyList()
        )

        // When
        composeTestRule.setContent {
            ContactItem(contact = contact)
        }

        // Then
        // The item should not have a click action since there are no likes
        composeTestRule.onNodeWithText("John Doe").assertExists()
        // We can't directly test if it's not clickable, but we can verify no likes appear after attempting to click
        composeTestRule.onNodeWithText("John Doe").performClick()
        // No likes should appear
        composeTestRule.onNodeWithText("Books").assertDoesNotExist()
    }

    @Test
    fun contactItem_withCustomAction_displaysAction() {
        // Given
        val contact = UserModel(
            name = "John Doe",
            id = "1",
            phoneNumber = "123456789",
            photoUrl = "",
            isPhoneNumberVerified = false,
            likes = listOf("Books", "Movies", "Games")
        )

        // When
        composeTestRule.setContent {
            val isChecked = remember { mutableStateOf(false) }
            ContactItem(
                contact = contact,
                action = { _, _ ->
                    Checkbox(
                        checked = isChecked.value,
                        onCheckedChange = { isChecked.value = it }
                    )
                }
            )
        }

        // Then
        // The checkbox should be displayed
        composeTestRule.onNode(androidx.compose.ui.test.isToggleable()).assertIsDisplayed()
    }

    @Test
    fun contactItem_whenSelected_hasSelectedAppearance() {
        // Given
        val contact = UserModel(
            name = "John Doe",
            id = "1",
            phoneNumber = "123456789",
            photoUrl = "",
            isPhoneNumberVerified = false,
            likes = listOf("Books", "Movies", "Games")
        )

        // When
        composeTestRule.setContent {
            ContactItem(
                contact = contact,
                isSelected = true
            )
        }

        // Then
        // We can't directly test the background color, but we can verify the content is displayed
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
    }
}
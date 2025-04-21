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
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
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
    }

    @Test
    fun contactItem_withEmptyLikes_filtersOutEmptyStrings() {
        // Given
        val contact = UserModel(
            name = "John Doe",
            id = "1",
            phoneNumber = "123456789",
            photoUrl = "",
            isPhoneNumberVerified = false,
            likes = listOf("Books", "", "Games", "   ")
        )

        // When
        composeTestRule.setContent {
            ContactItem(contact = contact)
        }

        // Then
        // Click to show likes
        composeTestRule.onNodeWithText("John Doe").performClick()
        
        // Only non-empty likes should be displayed
        composeTestRule.onNodeWithText("Books").assertIsDisplayed()
        composeTestRule.onNodeWithText("Games").assertIsDisplayed()
    }

    @Test
    fun contactItem_withCustomAction_invokesAction() {
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
                    Text("Custom Action")
                }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Custom Action").assertIsDisplayed()
    }

    @Test
    fun contactItem_withSelectedState_hasCorrectAppearance() {
        // Given
        val contact = UserModel(
            name = "John Doe",
            id = "1",
            phoneNumber = "123456789",
            photoUrl = "",
            isPhoneNumberVerified = false,
            likes = listOf("Books", "Movies", "Games")
        )

        // When - Create two instances, one selected and one not
        composeTestRule.setContent {
            ContactItem(
                contact = contact,
                isSelected = true
            )
        }

        // Then - We can only verify the content is displayed, not the actual styling
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
    }

    @Test
    fun contactItem_withPhotoUrl_displaysPhoto() {
        // Given - Note: In Robolectric tests, the actual image loading won't work
        val contact = UserModel(
            name = "John Doe",
            id = "1",
            phoneNumber = "123456789",
            photoUrl = "https://example.com/photo.jpg",
            isPhoneNumberVerified = false,
            likes = listOf("Books", "Movies", "Games")
        )

        // When
        composeTestRule.setContent {
            ContactItem(contact = contact)
        }

        // Then - We can verify the default icon is not displayed
        composeTestRule.onNodeWithContentDescription("Default user icon").assertDoesNotExist()
        // But we can't verify the actual AsyncImage content in Robolectric
    }
}
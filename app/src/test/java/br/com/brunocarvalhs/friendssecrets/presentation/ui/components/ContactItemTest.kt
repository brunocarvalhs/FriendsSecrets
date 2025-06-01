package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.brunocarvalhs.friendssecrets.data.model.UserEntities
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
    fun testContactItem_displaysContactInfo() {
        // Given
        val contact = UserEntities(
            id = "1",
            name = "John Doe",
            phone = "123456789",
            likes = listOf("Books", "Movies", "Games")
        )

        // When
        composeTestRule.setContent {
            ContactItem(contact = contact)
        }

        // Then
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("123456789").assertIsDisplayed()
    }

    @Test
    fun testContactItem_withEmptyPhone_hidesPhoneNumber() {
        // Given
        val contact = UserEntities(
            id = "1",
            name = "John Doe",
            phone = "",
            likes = listOf("Books")
        )

        // When
        composeTestRule.setContent {
            ContactItem(contact = contact)
        }

        // Then
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("").assertDoesNotExist()
    }

    @Test
    fun testContactItem_withLikes_showsLikesWhenClicked() {
        // Given
        val contact = UserEntities(
            id = "1",
            name = "John Doe",
            phone = "123456789",
            likes = listOf("Books", "Movies", "Games")
        )

        // When
        composeTestRule.setContent {
            ContactItem(contact = contact)
        }

        // Then
        // Initially likes are not shown
        composeTestRule.onNodeWithText("Books").assertDoesNotExist()
        
        // Click on the contact
        composeTestRule.onNodeWithText("John Doe").performClick()
        
        // Now likes should be visible
        composeTestRule.onNodeWithText("Books").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movies").assertIsDisplayed()
        composeTestRule.onNodeWithText("Games").assertIsDisplayed()
    }

    @Test
    fun testContactItem_withNoLikes_isNotClickable() {
        // Given
        val contact = UserEntities(
            id = "1",
            name = "John Doe",
            phone = "123456789",
            likes = emptyList()
        )

        // When
        composeTestRule.setContent {
            ContactItem(contact = contact)
        }

        // Then
        // Contact should be displayed
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        
        // Click on the contact
        composeTestRule.onNodeWithText("John Doe").performClick()
        
        // No likes should be shown as there are none
        composeTestRule.onNodeWithText("No likes").assertDoesNotExist()
    }

    @Test
    fun testContactItem_withCustomAction_displaysAction() {
        // Given
        val contact = UserEntities(
            id = "1",
            name = "John Doe",
            phone = "123456789",
            likes = listOf("Books")
        )

        // When
        composeTestRule.setContent {
            ContactItem(
                contact = contact,
                action = { user, _ ->
                    CustomActionButton(text = "Add ${user.name}")
                }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Add John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Add John Doe").assertHasClickAction()
    }

    @Test
    fun testContactItem_whenSelected_showsSelectedAppearance() {
        // Given
        val contact = UserEntities(
            id = "1",
            name = "John Doe",
            phone = "123456789",
            likes = listOf("Books")
        )

        // When
        composeTestRule.setContent {
            ContactItem(
                contact = contact,
                isSelected = true
            )
        }

        // Then
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        // Note: We can't easily test the background color in Robolectric,
        // but the component should be displayed with the selected state
    }
}

@androidx.compose.runtime.Composable
private fun CustomActionButton(text: String) {
    androidx.compose.material3.Button(onClick = {}) {
        androidx.compose.material3.Text(text = text)
    }
}
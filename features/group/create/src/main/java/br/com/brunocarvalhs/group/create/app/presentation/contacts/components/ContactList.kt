package br.com.brunocarvalhs.group.create.app.presentation.contacts.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun ContactList(
    contacts: List<ContactModel>,
    selectedMembers: List<ContactModel>,
    onToggleMember: (ContactModel) -> Unit
) {
    val grouped = remember(contacts) {
        contacts.groupBy { it.name.firstOrNull()?.uppercaseChar() ?: '#' }
            .toSortedMap()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        grouped.forEach { (initial, contactsInGroup) ->
            item {
                Text(
                    text = initial.toString(),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column {
                        contactsInGroup.forEachIndexed { index, contact ->
                            ContactListItem(
                                contact = contact,
                                isSelected = selectedMembers.any { it.id == contact.id },
                                onToggle = { onToggleMember(contact) }
                            )
                            if (index < contactsInGroup.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 72.dp),
                                    color = Color(0xFF333333),
                                    thickness = 0.5.dp
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun ContactListPreview() {
    ContactList(
        contacts = listOf(
            ContactModel(
                id = "1",
                name = "John Doe",
                phoneNumber = "1234567890"
            ),
            ContactModel(
                id = "2",
                name = "Jane Smith",
                phoneNumber = "9876543210"
            )
        ),
        selectedMembers = listOf(
            ContactModel(
                id = "1",
                name = "John Doe",
                phoneNumber = "1234567890"
            )
        ),
        onToggleMember = {}
    )
}
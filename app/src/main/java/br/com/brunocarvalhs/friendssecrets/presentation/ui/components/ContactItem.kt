package br.com.brunocarvalhs.friendssecrets.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities


@Composable
internal fun ContactItem(
    contact: UserEntities,
    onAdd: (UserEntities) -> Unit,
    onRemove: (UserEntities) -> Unit
) {
    val isSelect = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = contact.name,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isSelect.value,
                    onCheckedChange = {
                        if (!it) {
                            onRemove.invoke(contact)
                        } else {
                            onAdd.invoke(contact)
                        }
                    },
                )
            }
        }
    }
}

@Composable
@Preview
private fun ContactItemPreview() {
    ContactItem(
        contact = UserModel(
            name = "Produto de Teste",
            id = "1",
            phoneNumber = "123456789",
            photoUrl = "",
            isPhoneNumberVerified = false,
        ),
        onAdd = {},
        onRemove = {}
    )
}

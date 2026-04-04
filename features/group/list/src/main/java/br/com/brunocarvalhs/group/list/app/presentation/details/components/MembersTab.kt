package br.com.brunocarvalhs.group.list.app.presentation.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.list.R
import br.com.brunocarvalhs.group.list.app.domain.entities.UserModel

@Composable
fun MembersTab(
    draws: Map<String, String>,
    members: List<UserModel>,
    isOwner: Boolean,
    onRemove: () -> Unit,
    onEdit: () -> Unit,
    onShare: () -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.group_details_members))
            }
        }

        if (draws.isNotEmpty()) {
            items(draws.keys.toList()) { participant ->
                MemberItem(
                    participant = members.find { it.name == participant }?.name.orEmpty(),
                    draws = draws,
                    isAdministrator = isOwner,
                    onShare = onShare,
                )
            }
        } else {
            items(members) { member ->
                MemberItem(
                    participant = member.name,
                    isAdministrator = isOwner,
                    onEdit = onEdit,
                    onRemove = onRemove,
                    onShare = onShare,
                )
            }
        }
    }
}
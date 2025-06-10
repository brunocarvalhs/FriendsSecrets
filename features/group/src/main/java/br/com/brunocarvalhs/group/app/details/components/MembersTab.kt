package br.com.brunocarvalhs.group.app.details.components

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
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.ui.components.MemberItem
import br.com.brunocarvalhs.group.R
import br.com.brunocarvalhs.group.app.details.GroupDetailsUiState

@Composable
fun MembersTab(
    uiState: GroupDetailsUiState.Success,
    showBottomSheet: Boolean,
    setShowBottomSheet: (Boolean) -> Unit,
    setName: (String) -> Unit,
    setLikes: (List<String>) -> Unit,
    onShare: (UserEntities, String, String) -> Unit,
    onRemove: (GroupEntities, UserEntities) -> Unit
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

        if (uiState.group.draws.isNotEmpty()) {
            items(uiState.group.draws.keys.toList()) { participant ->
                MemberItem(
                    participant = uiState.group.members.find { it.name == participant }
                        ?: UserEntities.create(name = participant),
                    group = uiState.group,
                    isAdministrator = uiState.group.isOwner,
                    onShare = { member, secret, token ->
                        onShare(member, secret, token)
                    },
                )
            }
        } else {
            items(uiState.group.members) { member ->
                MemberItem(
                    participant = member,
                    group = uiState.group,
                    isAdministrator = uiState.group.isOwner,
                    onEdit = {
                        setShowBottomSheet(!showBottomSheet)
                        setName(member.name)
                        setLikes(member.likes)
                    },
                    onRemove = {
                        onRemove(uiState.group, member)
                    },
                )
            }
        }
    }
}
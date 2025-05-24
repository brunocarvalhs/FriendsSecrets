@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.extensions.textWithFormatting
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.presentation.ui.components.MemberItem
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.ExpandableText
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsPreviewProvider
import br.com.brunocarvalhs.friendssecrets.presentation.views.group.details.GroupDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsContentComponent(
    modifier: Modifier = Modifier,
    uiState: GroupDetailsUiState.Success,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    showBottomSheet: Boolean = false,
    setShowBottomSheet: (Boolean) -> Unit = {},
    setName: (String) -> Unit = {},
    setLikes: (List<String>) -> Unit = {},
    onShare: (member: String, secret: String, token: String) -> Unit = { _, _, _ -> },
    onRemove: (group: GroupEntities, participant: String) -> Unit = { _, _ -> }
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        item {
            uiState.group.description?.let {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.group_details_description),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    ExpandableText(
                        text = uiState.group.description?.textWithFormatting()
                            .orEmpty(), maxLines = 3
                    )
                }
            }
        }
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
                    participant = participant,
                    group = uiState.group,
                    isAdministrator = uiState.group.isOwner,
                    onShare = { member, secret, token ->
                        onShare(member, secret, token)
                    },
                    likes = uiState.group.members[participant]?.split("|").orEmpty()
                )
            }
        } else if (uiState.group.members.isNotEmpty()) {
            items(uiState.group.members.keys.toList()) { member ->
                MemberItem(
                    participant = member,
                    group = uiState.group,
                    isAdministrator = uiState.group.isOwner,
                    likes = uiState.group.members[member]?.split("|").orEmpty(),
                    onEdit = {
                        setShowBottomSheet(!showBottomSheet)
                        setName(member)
                        setLikes(uiState.group.members[member]?.split("|").orEmpty())
                    },
                    onRemove = {
                        onRemove(uiState.group, member)
                    },
                )
            }
        }
    }
}

@Composable
@Preview
fun GroupDetailsBodyComponentPreview(
    @PreviewParameter(GroupDetailsPreviewProvider::class) state: GroupDetailsUiState
) {
    if (state is GroupDetailsUiState.Success) {
        GroupDetailsContentComponent(
            uiState = state,
            showBottomSheet = false,
            setShowBottomSheet = {},
            setName = {},
            setLikes = {},
            onShare = { _, _, _ -> },
            onRemove = { _, _ -> }
        )
    }
}
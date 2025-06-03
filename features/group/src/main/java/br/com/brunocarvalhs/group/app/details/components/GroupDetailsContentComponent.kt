package br.com.brunocarvalhs.group.app.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.ui.components.MemberItem
import br.com.brunocarvalhs.group.R
import br.com.brunocarvalhs.group.app.details.ExpandableText
import br.com.brunocarvalhs.group.app.details.GroupDetailsPreviewProvider
import br.com.brunocarvalhs.group.app.details.GroupDetailsUiState

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
    onShare: (member: UserEntities, secret: String, token: String) -> Unit = { _, _, _ -> },
    onRemove: (group: GroupEntities, participant: UserEntities) -> Unit = { _, _ -> }
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabTitles = listOf(
        stringResource(R.string.group_tab_info),
        stringResource(R.string.group_tab_members),
        stringResource(R.string.group_tab_gift_ideas)
    )

    Column(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> GroupInfoTab(uiState)
            1 -> MembersTab(
                uiState,
                showBottomSheet,
                setShowBottomSheet,
                setName,
                setLikes,
                onShare,
                onRemove
            )

            2 -> GiftIdeasTab()
        }
    }
}

@Composable
private fun GroupInfoTab(uiState: GroupDetailsUiState.Success) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            uiState.group.description?.let {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.group_details_description),
                        style = MaterialTheme.typography.titleMedium
                    )
                    ExpandableText(text = it, maxLines = 3)
                }
            }
        }
    }
}

@Composable
private fun MembersTab(
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

@Composable
private fun GiftIdeasTab() {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        item {
            Text(
                text = "Indicações de presentes (em construção)",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun GroupDetailsBodyComponentPreview(
    @PreviewParameter(GroupDetailsPreviewProvider::class) state: GroupDetailsUiState
) {
    if (state is GroupDetailsUiState.Success) {
        GroupDetailsContentComponent(uiState = state,
            showBottomSheet = false,
            setShowBottomSheet = {},
            setName = {},
            setLikes = {},
            onShare = { _, _, _ -> },
            onRemove = { _, _ -> })
    }
}
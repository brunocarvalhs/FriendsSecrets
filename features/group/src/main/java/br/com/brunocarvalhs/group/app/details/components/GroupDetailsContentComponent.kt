package br.com.brunocarvalhs.group.app.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.group.R
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
    onRemove: (group: GroupEntities, participant: UserEntities) -> Unit = { _, _ -> },
    onEdit: (group: GroupEntities) -> Unit = { },
    onDraw: (group: GroupEntities) -> Unit = {},
    onShareGroup: (group: GroupEntities) -> Unit = {},
    onRevelationDraw: (group: GroupEntities) -> Unit = {},
    onInviteMembers: () -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabTitles = listOf(
        stringResource(R.string.group_tab_info),
        stringResource(R.string.group_tab_members),
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
            0 -> GroupInfoTab(
                uiState = uiState,
                onEditGroup = { onEdit(uiState.group) },
                onInviteMembers = { onInviteMembers() },
                onConfigureDraw = { },
                onDraw = { onDraw(uiState.group) },
                revelationDraw = { onRevelationDraw(uiState.group) }
            )

            1 -> MembersTab(
                uiState = uiState,
                showBottomSheet = showBottomSheet,
                setShowBottomSheet = setShowBottomSheet,
                setName = setName,
                setLikes = setLikes,
                onShare = onShare,
                onRemove = onRemove
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
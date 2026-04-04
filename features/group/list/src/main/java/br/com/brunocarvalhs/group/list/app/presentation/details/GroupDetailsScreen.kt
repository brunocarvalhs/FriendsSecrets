package br.com.brunocarvalhs.group.list.app.presentation.details

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.com.brunocarvalhs.group.list.R
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.list.app.domain.entities.UserModel
import br.com.brunocarvalhs.group.list.app.presentation.details.components.GroupInfoTab
import br.com.brunocarvalhs.group.list.app.presentation.details.components.MembersTab

@Composable
fun GroupDetailsScreen(
    viewModel: GroupDetailsViewModel,
    onBack: () -> Unit = {},
    onDraw: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    GroupDetailsContent(
        onBack = onBack,
        token = uiState.group.token,
        description = uiState.group.description,
        date = uiState.group.createdAt,
        draws = uiState.group.draws,
        isOwner = uiState.group.isOwner,
        minPrice = uiState.group.minPrice,
        maxPrice = uiState.group.maxPrice,
        type = uiState.group.type,
        members = uiState.group.members,
        onShowBottomSheet = { },
        onChangeName = { },
        onChangeLikes = { },
        onRemoveMember = { },
        onEditMember = { },
        onShareGroup = { }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupDetailsContent(
    onBack: () -> Unit,
    token: String,
    name: String? = null,
    description: String? = null,
    date: String? = null,
    draws: Map<String, String>,
    isOwner: Boolean = false,
    minPrice: Int? = null,
    maxPrice: Int? = null,
    type: String? = null,
    members: List<UserModel>,
    onShowBottomSheet: () -> Unit,
    onChangeName: (String) -> Unit,
    onChangeLikes: (List<String>) -> Unit,
    onRemoveMember: () -> Unit,
    onEditMember: () -> Unit,
    onShareGroup: () -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ), title = {
                    name?.let { Text(it) }
                }, actions = {

                }, navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }, scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValue ->
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        val tabTitles = listOf(
            stringResource(R.string.group_details_tab_info),
            stringResource(R.string.group_details_tab_members),
        )

        Column(
            modifier = Modifier
                .padding(paddingValue)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
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
                    token = token,
                    description = description,
                    date = date,
                    draws = draws,
                    isOwner = isOwner,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    type = type,
                )

                1 -> MembersTab(
                    draws = draws,
                    members = members,
                    isOwner = isOwner,
                    onShare = onShareGroup,
                    onRemove = onRemoveMember,
                    onEdit = onEditMember,
                )
            }
        }
    }
}

@Composable
fun ExpandableText(
    text: String,
    maxLines: Int = 3,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
            style = MaterialTheme.typography.bodyMedium
        )
        if (text.length > 100) {
            TextButton(onClick = { isExpanded = !isExpanded }) {
                Text(
                    stringResource(
                        id = if (isExpanded) R.string.group_details_text_button_label_see_less
                        else R.string.group_details_text_button_label_see_more
                    )
                )
            }
        }
    }
}

internal class GroupDetailsPreviewProvider : PreviewParameterProvider<GroupDetailsUiState> {
    override val values = sequenceOf(
        GroupDetailsUiState(
            group = GroupModel()
        )
    )
}

@Composable
@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
fun GroupDetailsScreenPreview(
    @PreviewParameter(GroupDetailsPreviewProvider::class) state: GroupDetailsUiState,
) {
    GroupDetailsContent(
        onBack = {},
        token = "token",
        description = "description",
        date = "date",
        draws = mapOf(),
        isOwner = true,
        minPrice = 10,
        maxPrice = 20,
        type = "type",
        members = listOf(),
        onShowBottomSheet = { },
        onChangeName = { },
        onChangeLikes = { },
        onRemoveMember = { },
        onEditMember = { },
        onShareGroup = { }
    )
}

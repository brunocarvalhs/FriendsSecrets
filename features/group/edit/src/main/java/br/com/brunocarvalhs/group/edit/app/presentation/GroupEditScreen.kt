package br.com.brunocarvalhs.group.edit.app.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.edit.R
import br.com.brunocarvalhs.group.edit.app.presentation.components.AddMemberBottomSheet
import br.com.brunocarvalhs.group.edit.app.presentation.components.MemberItem

@Composable
fun GroupEditScreen(
    viewModel: GroupEditViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

//    GroupEditContent(
//        navController = navController,
//        uiState = uiState,
//        onHome = {
//            val destination = GroupListScreenRoute
//            navController.navigate(destination) {
//                popUpTo(destination) {
//                    inclusive = true
//                }
//            }
//        },
//        onBack = {
//            navController.popBackStack()
//        },
//        onEdit = { group ->
//            viewModel.eventIntent(
//                intent = GroupEditIntent.EditGroup(group)
//            )
//        }
//    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GroupEditContent(
    uiState: GroupEditUiState,
    onHome: () -> Unit,
    onBack: () -> Unit,
    onEdit: (GroupModel) -> Unit,
) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    val members = remember { mutableListOf<UserModel>() }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {

            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
        )
    }, floatingActionButton = {
        ExtendedFloatingActionButton(onClick = {
            onEdit.invoke(
                uiState.group.copy(
                    name = name.text,
                    description = description.text,
                    members = members
                )
            )
        }) {
            Icon(Icons.Filled.Check, stringResource(R.string.save_group))
            Text(stringResource(R.string.save_group))
        }
    }) { paddingValues ->
        GroupEditForm(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            name = name,
            onNameChange = { data -> name = data },
            description = description,
            onDescriptionChange = { data -> description = data },
            members = members,
            onMembersChange = { list ->
                members.clear()
                members.addAll(list)
            }
        )
    }
}

@Composable
private fun GroupEditForm(
    uiState: GroupEditUiState,
    modifier: Modifier = Modifier,
    name: TextFieldValue,
    onNameChange: (TextFieldValue) -> Unit = {},
    description: TextFieldValue,
    onDescriptionChange: (TextFieldValue) -> Unit = {},
    members: MutableList<UserModel>,
    onMembersChange: (List<UserModel>) -> Unit = { _ -> }
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onNameChange(TextFieldValue(uiState.group.name))
        onDescriptionChange(TextFieldValue(uiState.group.description.orEmpty()))
        onMembersChange(uiState.group.members)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Row(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text(text = stringResource(R.string.input_name)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }
        item {
            Row(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text(text = stringResource(R.string.input_description)) },
                    modifier = Modifier.weight(1f)
                )
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
                TextButton(onClick = { showBottomSheet = true }) {
                    Text(text = stringResource(R.string.button_member))
                }
                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(
                        Icons.Filled.Add,
                        stringResource(R.string.button_member)
                    )
                }
            }
        }
        items(members) { member ->
            MemberItem(
                participant = member,
                onRemove = {
                    onMembersChange(members.apply { remove(member) })
                },
            )
            HorizontalDivider()
        }
    }
    if (showBottomSheet) {
        AddMemberBottomSheet(
            onDismiss = { showBottomSheet = false },
            onMemberAdded = { member -> members.add(member) }
        )
    }
}


private class GroupEditPreviewProvider : PreviewParameterProvider<GroupEditUiState> {
    override val values = sequenceOf(
        GroupEditUiState(
            group = GroupModel(
                id = "1",
                name = "Group",
                description = "Description",
                members = listOf(
                    UserModel(
                        name = "Member 1",
                        likes = listOf(
                            "Like 1",
                            "Like 2"
                        )
                    )
                )
            )
        ),
        GroupEditUiState(
            group = GroupModel(
                id = "1",
                name = "Group",
                description = "Description",
                members = listOf(
                    UserModel(
                        name = "Member 1",
                        likes = listOf(
                            "Like 1",
                            "Like 2"
                        )
                    )
                )
            ),
            isLoading = true
        ),
        GroupEditUiState(
            group = GroupModel(
                id = "1",
                name = "Group",
                description = "Description",
                members = listOf(
                    UserModel(
                        name = "Member 1",
                        likes = listOf(
                            "Like 1",
                            "Like 2"
                        )
                    )
                )
            ),
            isLoading = true,
            error = "Error"
        ),
    )
}

@Composable
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(showBackground = true)
fun GroupCreateScreenPreview(
    @PreviewParameter(GroupEditPreviewProvider::class) state: GroupEditUiState,
) {
    GroupEditContent(
        uiState = state,
        onBack = { },
        onHome = { },
        onEdit = { _ -> }
    )
}
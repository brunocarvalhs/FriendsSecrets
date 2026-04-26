package br.com.brunocarvalhs.group.list.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.list.R

@Composable
internal fun GroupListItems(
    groups: List<GroupModel>,
    onGroupToEnter: (GroupModel) -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(groups) { item ->
            GroupCard(
                modifier = Modifier.Companion.fillMaxWidth(),
                name = item.name,
                description = item.description,
                date = item.date,
                membersCount = item.members.size,
                onClick = { onGroupToEnter(item) }
            )
        }
    }
}

@Composable
fun GroupListFab(
    onGroupToCreate: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onGroupToCreate,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.new_group)
        )
    }
}

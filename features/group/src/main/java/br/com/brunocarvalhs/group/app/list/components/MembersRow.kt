package br.com.brunocarvalhs.group.app.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.ui.fake.toFake
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme

@Composable
internal fun MembersRow(
    group: GroupEntities,
    shape: RoundedCornerShape = CircleShape,
){
    Column(
        modifier = Modifier.fillMaxHeight(0.8f),
        verticalArrangement = Arrangement.Bottom
    ) {
        val overlapOffset = (-10).dp
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(overlapOffset)
        ) {
            if (group.members.size > 3) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.primary)
                        .border(1.dp, Color.White, shape)
                        .zIndex(group.members.size.toFloat()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+${group.members.size - 3}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            group.members.take(3).forEachIndexed { index, member ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.primary)
                        .border(1.dp, Color.White, shape)
                        .zIndex((group.members.size - index - 1).toFloat()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = member.toString(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun MembersRowPreview() {
    FriendsSecretsTheme {
        MembersRow(group = GroupEntities.toFake())
    }
}
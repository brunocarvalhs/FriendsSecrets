package br.com.brunocarvalhs.group.list.app.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.brunocarvalhs.group.list.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

private val imagesBottom = listOf(
    R.raw.card_bottom_one,
    R.raw.card_bottom_two,
    R.raw.card_top_one,
)

private const val ANIMATION_SIZE = 180
private const val ANIMATION_OFFSET = 30
private const val ANIMATION_ALPHA = 0.15f

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun GroupCard(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    date: String? = null,
    membersCount: Int? = null,
    onLongClick: (() -> Unit)? = null,
) {
    val image = rememberSaveable { imagesBottom.random() }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(image))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val groupDescription = buildString {
        append(name)
        description?.let { append(". $it") }
        date?.let { append(". $it") }
        membersCount?.let { append(". $it ${if (it == 1) "pessoa" else "pessoas"}") }
    }

    Card(
        modifier = modifier
            .height(200.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = groupDescription
                role = Role.Button
            }
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(ANIMATION_SIZE.dp)
                    .alpha(ANIMATION_ALPHA)
                    .align(Alignment.BottomEnd)
                    .offset(x = ANIMATION_OFFSET.dp, y = ANIMATION_OFFSET.dp)
            )

            GroupCardContent(
                name = name,
                description = description,
                date = date,
                membersCount = membersCount
            )
        }
    }
}


@Composable
private fun GroupCardContent(
    name: String,
    description: String?,
    date: String?,
    membersCount: Int?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (date != null) {
                InfoBadge(icon = Icons.Default.CalendarMonth, text = date)
            }
            if (membersCount != null) {
                val text = stringResource(R.string.group_card_members_count, membersCount)
                InfoBadge(icon = Icons.Default.People, text = text)
            }
        }
    }
}


@Composable
private fun InfoBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
private fun GroupCardPreview() {
    MaterialTheme {
        GroupCard(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            name = "Amigo Secreto Família",
            description = "Confraternização de final de ano na casa da Vovó. Limite de R$ 50,00.",
            date = "24 Dez 2024",
            membersCount = 12,
            onClick = {}
        )
    }
}

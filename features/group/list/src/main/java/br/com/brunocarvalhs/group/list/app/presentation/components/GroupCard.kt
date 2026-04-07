package br.com.brunocarvalhs.group.list.app.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.brunocarvalhs.group.list.R
import com.airbnb.lottie.compose.*

private val imagesBottom = listOf(
    R.raw.card_bottom_one,
    R.raw.card_bottom_two,
    R.raw.card_top_one,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupCard(
    modifier: Modifier = Modifier,
    name: String,
    description: String? = null,
    date: String? = null,
    membersCount: Int? = null,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
) {
    // Mantém a lógica da animação original
    val image = rememberSaveable { imagesBottom.random() }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(image))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Card(
        modifier = modifier
            .height(200.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = MaterialTheme.shapes.extraLarge // Bordas mais arredondadas combinam com o tema festivo
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Background Animation (Mantida a sua lógica original de opacidade e posição)
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier
                    .size(180.dp)
                    .alpha(0.15f) // Aumentei levemente a visibilidade
                    .align(Alignment.BottomEnd)
                    .offset(x = 30.dp, y = 30.dp)
            )

            // Conteúdo do Card
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Topo: Nome e Descrição
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

                // Rodapé: Metadados (Data e Membros)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (date != null) {
                        InfoBadge(icon = Icons.Default.CalendarMonth, text = date)
                    }
                    if (membersCount != null) {
                        InfoBadge(icon = Icons.Default.People, text = "$membersCount pessoas")
                    }
                }
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
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            name = "Amigo Secreto Família",
            description = "Confraternização de final de ano na casa da Vovó. Limite de R$ 50,00.",
            date = "24 Dez 2024",
            membersCount = 12,
            onClick = {}
        )
    }
}
package br.com.brunocarvalhs.friendssecrets.ui.components

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.ui.remembers.rememberReviewRequester
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewRequesterBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Gostaria de avaliar nosso app?",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Sua avaliação ajuda muito a melhorar o aplicativo e trazer novidades.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Não, obrigado")
                }
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Avaliar")
                }
            }
        }
    }

    BackHandler(onBack = onDismiss)
}

@Composable
fun RandomReviewRequester(
    probability: Double = 0.3,
    minIntervalDays: Int = 7,
) {
    val reviewRequester = rememberReviewRequester()
    val context = LocalContext.current
    val activity = context as? Activity

    val prefs = remember {
        context.getSharedPreferences("review_prefs", android.content.Context.MODE_PRIVATE)
    }

    var showSheet by remember { mutableStateOf(false) }

    fun canRequestReview(): Boolean {
        val lastRequest = prefs.getLong("last_review_request", 0L)
        val now = System.currentTimeMillis()
        val intervalMillis = minIntervalDays * 24 * 60 * 60 * 1000L
        return now - lastRequest >= intervalMillis
    }

    fun saveRequestTimestamp() {
        prefs.edit().putLong("last_review_request", System.currentTimeMillis()).apply()
    }

    LaunchedEffect(Unit) {
        if (activity != null && canRequestReview() && Random.nextDouble() <= probability) {
            showSheet = true
        }
    }

    if (showSheet) {
        ReviewRequesterBottomSheet(
            onDismiss = {
                showSheet = false
                saveRequestTimestamp()
            },
            onConfirm = {
                showSheet = false
                saveRequestTimestamp()
                reviewRequester()
            }
        )
    }
}

@Composable
@Preview
private fun ReviewRequesterBottomSheetPreview() {
    ReviewRequesterBottomSheet(
        onDismiss = {},
        onConfirm = {}
    )
}

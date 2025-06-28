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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.friendssecrets.ui.R
import br.com.brunocarvalhs.friendssecrets.ui.remembers.rememberReviewRequester
import kotlin.random.Random

private const val REVIEW_PREFERENCES_KEY: String = "review_preferences"
private const val LAST_REVIEW_REQUEST: String = "last_review_request"
private const val PROBABILITY: Double = 0.3
private const val MIN_INTERVAL_DAYS: Int = 7

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
                text = stringResource(R.string.gostaria_de_avaliar_nosso_app),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.sua_avalia_o_ajuda_muito_a_melhorar_o_aplicativo_e_trazer_novidades),
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
                    Text(stringResource(R.string.n_o_obrigado))
                }
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.avaliar))
                }
            }
        }
    }

    BackHandler(onBack = onDismiss)
}

@Composable
fun RandomReviewRequester(
    probability: Double = PROBABILITY,
    minIntervalDays: Int = MIN_INTERVAL_DAYS,
) {
    val reviewRequester = rememberReviewRequester()
    val context = LocalContext.current
    val activity = context as? Activity

    val prefs = remember {
        context.getSharedPreferences(REVIEW_PREFERENCES_KEY, android.content.Context.MODE_PRIVATE)
    }

    var showSheet by remember { mutableStateOf(false) }

    fun canRequestReview(): Boolean {
        val lastRequest = prefs.getLong(LAST_REVIEW_REQUEST, 0L)
        val now = System.currentTimeMillis()
        val intervalMillis = minIntervalDays * 24 * 60 * 60 * 1000L
        return now - lastRequest >= intervalMillis
    }

    fun saveRequestTimestamp() {
        prefs.edit().putLong(LAST_REVIEW_REQUEST, System.currentTimeMillis()).apply()
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

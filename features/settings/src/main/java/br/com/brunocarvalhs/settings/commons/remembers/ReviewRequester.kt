package br.com.brunocarvalhs.settings.commons.remembers

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import br.com.brunocarvalhs.core.review.data.InAppReviewLauncher
import kotlinx.coroutines.launch

@Composable
internal fun rememberReviewRequester(): () -> Unit {
    val activity = LocalActivity.current
    val coroutineScope = rememberCoroutineScope()
    val inAppReviewLauncher = remember { InAppReviewLauncher() }

    return remember {
        {
            if (activity != null) {
                coroutineScope.launch {
                    inAppReviewLauncher.launch(activity)
                }
            }
        }
    }
}

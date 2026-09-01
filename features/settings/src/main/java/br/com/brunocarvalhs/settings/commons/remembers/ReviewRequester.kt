package br.com.brunocarvalhs.settings.commons.remembers

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import br.com.brunocarvalhs.core.review.data.InAppReviewLauncher
import kotlinx.coroutines.launch

@Composable
internal fun rememberReviewRequester(): () -> Unit {
    val context = LocalContext.current
    val activity = context as? Activity
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

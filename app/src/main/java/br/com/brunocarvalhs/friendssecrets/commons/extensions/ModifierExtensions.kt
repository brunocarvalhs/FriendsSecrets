package br.com.brunocarvalhs.friendssecrets.commons.extensions

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsEvents
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsParams
import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsProvider
import timber.log.Timber

@Composable
fun Modifier.track(
    event: AnalyticsEvents,
    params: Map<AnalyticsParams, String> = emptyMap()
): Modifier {
    LaunchedEffect(Unit) {
        AnalyticsProvider.track(event, params)
        Timber.d("Tracked on open event: $event, $params")
    }

    return this
}

fun Modifier.trackClick(
    params: Map<AnalyticsParams, String> = emptyMap(),
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                AnalyticsProvider.track(AnalyticsEvents.CLICK, params)
                Timber.d("Tracked click event: ${AnalyticsEvents.CLICK}, $params")
            }
        )
    }
}

package br.com.brunocarvalhs.group.draw.app.presentation

import android.app.Activity

internal sealed interface DrawIntent {
    data class Share(val secret: String) : DrawIntent
    data object Draw : DrawIntent
    data class RequestReview(val activity: Activity) : DrawIntent
}

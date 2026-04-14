package br.com.brunocarvalhs.group.draw.app.presentation

internal sealed interface DrawIntent {
    data class Share(val secret: String) : DrawIntent
    data object Draw : DrawIntent
}
package br.com.brunocarvalhs.group.draw.app.presentation

sealed interface DrawIntent {
    data object FetchDraw : DrawIntent
}
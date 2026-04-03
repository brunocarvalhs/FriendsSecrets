package br.com.brunocarvalhs.group.app.presentation.draw

import android.content.Context
import androidx.navigation.NavController

sealed interface DrawIntent {
    data object Refresh : DrawIntent
    data class FetchDraw(val group: String, val code: String? = null) : DrawIntent
    data class GenerativeDraw(
        val context: Context,
        val navigation: NavController,
        val group: GroupEntities,
        val secret: String,
        val likes: List<String>,
    ) : DrawIntent
}
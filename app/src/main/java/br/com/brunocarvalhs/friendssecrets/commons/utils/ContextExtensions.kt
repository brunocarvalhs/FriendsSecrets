package br.com.brunocarvalhs.friendssecrets.commons.utils

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.BuildConfig

fun Context.isFistAppOpen(): Boolean {
    val firstAppOpen = getSharedPreferences(
        BuildConfig.APPLICATION_ID,
        Context.MODE_PRIVATE
    ).getBoolean("FIRST_APP_OPEN", true)
    if (firstAppOpen) {
        getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        ).edit().putBoolean("FIRST_APP_OPEN", false).apply()
        return true
    }
    return false
}
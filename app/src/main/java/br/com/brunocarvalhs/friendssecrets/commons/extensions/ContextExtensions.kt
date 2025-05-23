package br.com.brunocarvalhs.friendssecrets.commons.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import com.google.firebase.perf.metrics.AddTrace

@AddTrace(name = "Context.isFistAppOpen")
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

@AddTrace(name = "Context.openUrl")
fun Context.openUrl(url: String) {
    var intent = packageManager.getLaunchIntentForPackage("com.android.chrome")
    if (intent != null) {
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
    } else {
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }

    startActivity(intent)
}
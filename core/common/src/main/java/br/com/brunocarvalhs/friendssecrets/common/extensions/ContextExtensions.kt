package br.com.brunocarvalhs.friendssecrets.common.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings.Secure

fun Context.isFistAppOpen(): Boolean {
    val firstAppOpen = getSharedPreferences(
        this.packageName,
        Context.MODE_PRIVATE
    ).getBoolean("FIRST_APP_OPEN", true)
    if (firstAppOpen) {
        getSharedPreferences(
            this.packageName,
            Context.MODE_PRIVATE
        ).edit().putBoolean("FIRST_APP_OPEN", false).apply()
        return true
    }
    return false
}

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

@SuppressLint("HardwareIds")
fun Context.getId(): String {
    return Secure.getString(this.contentResolver, Secure.ANDROID_ID)
}
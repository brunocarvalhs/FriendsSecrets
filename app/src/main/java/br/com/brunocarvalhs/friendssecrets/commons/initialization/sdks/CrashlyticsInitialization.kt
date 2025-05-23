package br.com.brunocarvalhs.friendssecrets.commons.initialization.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.commons.extensions.getId
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsInitialization : Initializer<FirebaseCrashlytics> {

    override fun create(context: Context): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance().apply {
            isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
            setUserId(context.getId())
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(FirebaseAppInitialization::class.java)
    }
}
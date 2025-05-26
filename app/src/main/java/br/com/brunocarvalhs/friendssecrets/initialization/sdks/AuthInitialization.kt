package br.com.brunocarvalhs.friendssecrets.initialization.sdks

import android.content.Context
import androidx.startup.Initializer
import br.com.brunocarvalhs.friendssecrets.common.session.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.initialization.providers.SessionEventImpl
import com.google.firebase.auth.FirebaseAuth

class AuthInitialization : Initializer<SessionManager<UserEntities>> {
    override fun create(context: Context): SessionManager<UserEntities> {
        val auth = FirebaseAuth.getInstance()
        SessionManager.initialize(SessionEventImpl(auth))
        return SessionManager.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(FirebaseAppInitialization::class.java)
    }
}
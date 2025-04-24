package br.com.brunocarvalhs.friendssecrets

import android.app.Application
import br.com.brunocarvalhs.friendssecrets.commons.initialization.AppInitializationManager

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()
    }

    private fun setup() {
        AppInitializationManager(this.applicationContext).initialize()
    }

    companion object {
        @Volatile
        private var instance: CustomApplication? = null

        fun getInstance(): CustomApplication {
            return instance ?: synchronized(this) {
                instance ?: CustomApplication().also { instance = it }
            }
        }
    }
}
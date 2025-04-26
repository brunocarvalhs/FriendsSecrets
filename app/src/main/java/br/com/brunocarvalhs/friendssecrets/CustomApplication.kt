package br.com.brunocarvalhs.friendssecrets

import android.app.Application
import br.com.brunocarvalhs.friendssecrets.commons.initialization.AppInitializationManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CustomApplication : Application() {

    @Inject
    lateinit var initializationManager: AppInitializationManager

    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()
    }

    private fun setup() {
        initializationManager.initialize()
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
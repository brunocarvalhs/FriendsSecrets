package br.com.brunocarvalhs.friendssecrets

import android.app.Application

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
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
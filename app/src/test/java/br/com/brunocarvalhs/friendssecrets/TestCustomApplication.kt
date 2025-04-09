package br.com.brunocarvalhs.friendssecrets

import android.app.Application

class TestCustomApplication : Application() {

    companion object {
        private lateinit var instance: TestCustomApplication

        fun getInstance(): TestCustomApplication {
            return instance
        }

        fun setInstance(customApplication: TestCustomApplication) {
            instance = customApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
package br.com.brunocarvalhs.friendssecrets

import android.app.Application
import br.com.brunocarvalhs.friendssecrets.di.ServiceLocator

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()
    }

    private fun setup() {
        // Inicializa o ServiceLocator com o contexto da aplicação
        ServiceLocator.initialize(this)
        
        // Inicializa os componentes da aplicação
        ServiceLocator.appInitializationManager.initialize()
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

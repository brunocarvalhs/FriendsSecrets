package br.com.brunocarvalhs.friendssecrets.data.service

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import com.google.gson.Gson
import timber.log.Timber

class StorageService(
    private val context: Application = CustomApplication.getInstance(),
    private val storageName: String = BuildConfig.APPLICATION_ID,
    val gson: Gson = Gson()
) {
    val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(storageName, Context.MODE_PRIVATE)
    }

    fun <T> save(key: String, value: T) {
        try {
            sharedPreferences.edit().putString(key, gson.toJson(value)).apply()
        } catch (e: Exception) {
            Timber.e(e.message, e)
        }
    }

    inline fun <reified T> load(key: String): T? {
        return try {
            val json = this.sharedPreferences.getString(key, null)
            this.gson.fromJson(json, T::class.java)
        } catch (e: Exception) {
            Timber.e(e.message, e)
            null
        }
    }

    fun remove(key: String) {
        try {
            sharedPreferences.edit().remove(key).apply()
        } catch (e: Exception) {
            Timber.e(e.message, e)
        }
    }

    fun clear() {
        try {
            sharedPreferences.edit().clear().apply()
        } catch (e: Exception) {
            Timber.e(e.message, e)
        }
    }

    fun contains(key: String): Boolean {
        return try {
            sharedPreferences.contains(key)
        } catch (e: Exception) {
            Timber.e(e.message, e)
            false
        }
    }
}

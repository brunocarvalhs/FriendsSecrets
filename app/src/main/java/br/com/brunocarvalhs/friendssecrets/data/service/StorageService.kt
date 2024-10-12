package br.com.brunocarvalhs.friendssecrets.data.service

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import br.com.brunocarvalhs.friendssecrets.BuildConfig
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import com.google.gson.Gson

class StorageService(
    private val context: Application = CustomApplication.instance,
    private val storageName: String = BuildConfig.APPLICATION_ID,
    val gson: Gson = Gson()
) {
    val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(storageName, Context.MODE_PRIVATE)
    }

    fun <T> save(key: String, value: T) {
        sharedPreferences.edit().putString(key, gson.toJson(value)).apply()
    }

    inline fun <reified T> load(key: String): T? {
        val json = this.sharedPreferences.getString(key, null)
        return this.gson.fromJson(json, T::class.java)
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
}
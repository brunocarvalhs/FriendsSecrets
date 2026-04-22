package br.com.brunocarvalhs.friendssecrets.core.navigation

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

val navJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

inline fun <reified T : Any> navTypeSerializer() = object : NavType<T>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let { navJson.decodeFromString(it) }
    }

    override fun parseValue(value: String): T {
        return navJson.decodeFromString(URLDecoder.decode(value, StandardCharsets.UTF_8.name()))
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, navJson.encodeToString(value))
    }

    override fun serializeAsValue(value: T): String {
        return URLEncoder.encode(navJson.encodeToString(value), StandardCharsets.UTF_8.name())
    }
}

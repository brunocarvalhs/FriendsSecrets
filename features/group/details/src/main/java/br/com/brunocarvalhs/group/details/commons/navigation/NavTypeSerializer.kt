package br.com.brunocarvalhs.group.details.commons.navigation

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal val navJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
}

internal inline fun <reified T : Any> navTypeSerializer() = object : NavType<T>(isNullableAllowed = false) {
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

internal inline fun <reified T : Any> navTypeListSerializer() = object : NavType<List<T>>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): List<T>? {
        return bundle.getString(key)?.let { navJson.decodeFromString(it) }
    }

    override fun parseValue(value: String): List<T> {
        return navJson.decodeFromString(URLDecoder.decode(value, StandardCharsets.UTF_8.name()))
    }

    override fun put(bundle: Bundle, key: String, value: List<T>) {
        bundle.putString(key, navJson.encodeToString(value))
    }

    override fun serializeAsValue(value: List<T>): String {
        return URLEncoder.encode(navJson.encodeToString(value), StandardCharsets.UTF_8.name())
    }
}
